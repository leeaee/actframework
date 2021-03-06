package act.app;

import act.ActComponent;
import act.handler.builtin.controller.AfterInterceptor;
import act.handler.builtin.controller.BeforeInterceptor;
import act.handler.builtin.controller.ExceptionInterceptor;
import act.handler.builtin.controller.FinallyInterceptor;
import act.handler.builtin.controller.RequestHandlerProxy.GroupAfterInterceptor;
import act.handler.builtin.controller.RequestHandlerProxy.GroupExceptionInterceptor;
import act.handler.builtin.controller.RequestHandlerProxy.GroupFinallyInterceptor;
import act.handler.builtin.controller.RequestHandlerProxy.GroupInterceptorWithResult;
import org.osgl.mvc.result.Result;
import org.osgl.util.C;

import java.util.Collections;

import static act.handler.builtin.controller.RequestHandlerProxy.insertInterceptor;

/**
 * Manage interceptors at App level
 */
@ActComponent
public class AppInterceptorManager extends AppServiceBase<AppInterceptorManager> {
    private C.List<BeforeInterceptor> beforeInterceptors = C.newList();
    private C.List<AfterInterceptor> afterInterceptors = C.newList();
    private C.List<ExceptionInterceptor> exceptionInterceptors = C.newList();
    private C.List<FinallyInterceptor> finallyInterceptors = C.newList();

    final GroupInterceptorWithResult BEFORE_INTERCEPTOR = new GroupInterceptorWithResult(beforeInterceptors);
    final GroupAfterInterceptor AFTER_INTERCEPTOR = new GroupAfterInterceptor(afterInterceptors);
    final GroupFinallyInterceptor FINALLY_INTERCEPTOR = new GroupFinallyInterceptor(finallyInterceptors);
    final GroupExceptionInterceptor EXCEPTION_INTERCEPTOR = new GroupExceptionInterceptor(exceptionInterceptors);

    AppInterceptorManager(App app) {
        super(app);
    }

    public Result handleBefore(ActionContext actionContext) throws Exception {
        return BEFORE_INTERCEPTOR.apply(actionContext);
    }

    public Result handleAfter(Result result, ActionContext actionContext) throws Exception {
        return AFTER_INTERCEPTOR.apply(result, actionContext);
    }

    public void handleFinally(ActionContext actionContext) throws Exception {
        FINALLY_INTERCEPTOR.apply(actionContext);
    }


    public Result handleException(Exception ex, ActionContext actionContext) throws Exception {
        return EXCEPTION_INTERCEPTOR.apply(ex, actionContext);
    }

    public void registerInterceptor(BeforeInterceptor interceptor) {
        insertInterceptor(beforeInterceptors, interceptor);
    }

    public void registerInterceptor(AfterInterceptor interceptor) {
        insertInterceptor(afterInterceptors, interceptor);
    }

    public void registerInterceptor(FinallyInterceptor interceptor) {
        insertInterceptor(finallyInterceptors, interceptor);
    }

    public void registerInterceptor(ExceptionInterceptor interceptor) {
        insertInterceptor(exceptionInterceptors, interceptor);
        Collections.sort(exceptionInterceptors);
    }

    @Override
    protected void releaseResources() {
        beforeInterceptors.clear();
        afterInterceptors.clear();
        exceptionInterceptors.clear();
        finallyInterceptors.clear();
    }
}
