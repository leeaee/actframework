package act.event.bytecode;

import act.ActComponent;
import act.app.AppByteCodeScannerBase;
import act.app.event.AppEventId;
import act.asm.AnnotationVisitor;
import act.asm.MethodVisitor;
import act.asm.Type;
import act.event.EventBus;
import act.event.On;
import act.event.OnClass;
import act.event.meta.SimpleEventListenerMetaInfo;
import act.job.AppJobManager;
import act.util.AsmTypes;
import act.util.Async;
import act.util.ByteCodeVisitor;
import org.osgl.$;
import org.osgl.util.C;
import org.osgl.util.S;

import java.util.List;

@ActComponent
public class SimpleEventListenerByteCodeScanner extends AppByteCodeScannerBase {

    private List<SimpleEventListenerMetaInfo> metaInfoList = C.newList();

    @Override
    protected void reset(String className) {
        metaInfoList.clear();
    }

    @Override
    protected boolean shouldScan(String className) {
        return true;
    }

    @Override
    public ByteCodeVisitor byteCodeVisitor() {
        return new _ByteCodeVisitor();
    }

    @Override
    public void scanFinished(String className) {
        if (!metaInfoList.isEmpty()) {
            final EventBus eventBus = app().eventBus();
            AppJobManager jobManager = app().jobManager();
            for (final SimpleEventListenerMetaInfo metaInfo : metaInfoList) {
                for (final Object event : metaInfo.events()) {
                    final boolean isStatic = metaInfo.isStatic();
                    jobManager.on(AppEventId.PRE_START, new Runnable() {
                        @Override
                        public void run() {
                            if (metaInfo.isAsync()) {
                                eventBus.bindAsync(event, new ReflectedSimpleEventListener(metaInfo.className(), metaInfo.methodName(), metaInfo.paramTypes(), isStatic));
                            } else {
                                eventBus.bind(event, new ReflectedSimpleEventListener(metaInfo.className(), metaInfo.methodName(), metaInfo.paramTypes(), isStatic));
                            }
                        }
                    });
                }
            }
        }
    }

    private class _ByteCodeVisitor extends ByteCodeVisitor {

        private String className;

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            className = Type.getObjectType(name).getClassName();
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            Type returnType = Type.getReturnType(desc);
            final boolean isVoid = "V".equals(returnType.toString());
            final boolean isPublicNotAbstract = AsmTypes.isPublicNotAbstract(access);
            Type[] arguments = Type.getArgumentTypes(desc);
            final List<String> paramTypes = C.newList();
            if (null != arguments) {
                for (Type type : arguments) {
                    paramTypes.add(type.getClassName());
                }
            }
            final String methodName = name;
            final boolean isStatic = AsmTypes.isStatic(access);
            return new MethodVisitor(ASM5, mv) {

                private List<Object> events = C.newList();

                private boolean isAsync;

                private String asyncMethodName = null;

                @Override
                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    AnnotationVisitor av = super.visitAnnotation(desc, visible);
                    String className = Type.getType(desc).getClassName();
                    final boolean isOn = On.class.getName().equals(className);
                    final boolean isOnC = OnClass.class.getName().equals(className);
                    if (isOn || isOnC) {
                        return new AnnotationVisitor(ASM5, av) {
                            @Override
                            public AnnotationVisitor visitArray(String name) {
                                AnnotationVisitor av0 = super.visitArray(name);
                                if ("value".equals(name)) {
                                    return new AnnotationVisitor(ASM5, av0) {
                                        @Override
                                        public void visit(String name, Object value) {
                                            super.visit(name, value);
                                            if (isOn) {
                                                events.add(S.string(value).intern());
                                            } else {
                                                Type type = (Type) value;
                                                Class<?> c = $.classForName(type.getClassName(), app().classLoader());
                                                events.add(c);
                                            }
                                        }
                                    };
                                } else {
                                    return av0;
                                }
                            }

                            @Override
                            public void visit(String name, Object value) {
                                if ("async".equals(name)) {
                                    isAsync = Boolean.parseBoolean(S.string(value));
                                }
                            }
                        };
                    } else if (Async.class.getName().equals(className)) {
                            if (!isVoid) {
                                logger.warn("Error found in method %s.%s: @Async annotation cannot be used with method that has return type", className, methodName);
                            } else if (!isPublicNotAbstract) {
                                logger.warn("Error found in method %s.%s: @Async annotation cannot be used with method that are not public or abstract method", className, methodName);
                            } else {
                                asyncMethodName = Async.MethodNameTransformer.transform(methodName);
                            }
                        return av;
                    } else {
                        return new AnnotationVisitor(ASM5, av) {
                            @Override
                            public void visit(String name, Object value) {
                                super.visit(name, value);
                            }

                            @Override
                            public void visitEnd() {
                                super.visitEnd();
                            }

                            @Override
                            public void visitEnum(String name, String desc, String value) {
                                super.visitEnum(name, desc, value);
                            }

                            @Override
                            public AnnotationVisitor visitAnnotation(String name, String desc) {
                                return super.visitAnnotation(name, desc);
                            }

                            @Override
                            public AnnotationVisitor visitArray(String name) {
                                return super.visitArray(name);
                            }
                        };
                    }
                }

                @Override
                public void visitEnd() {
                    if (!events.isEmpty()) {
                        SimpleEventListenerMetaInfo metaInfo = new SimpleEventListenerMetaInfo(events, className, methodName, asyncMethodName, paramTypes, isAsync, isStatic, app());
                        metaInfoList.add(metaInfo);
                    }
                    super.visitEnd();
                }
            };
        }
    }
}
