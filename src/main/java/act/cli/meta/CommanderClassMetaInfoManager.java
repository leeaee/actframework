package act.cli.meta;

import act.asm.Type;
import act.util.DestroyableBase;
import org.osgl.logging.LogManager;
import org.osgl.logging.Logger;
import org.osgl.util.C;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

import static act.Destroyable.Util.destroyAll;

@ApplicationScoped
public class CommanderClassMetaInfoManager extends DestroyableBase {

    private static final Logger logger = LogManager.get(CommanderClassMetaInfoManager.class);

    private Map<String, CommanderClassMetaInfo> commands = C.newMap();
    private Map<Type, List<CommanderClassMetaInfo>> subTypeInfo = C.newMap();

    public CommanderClassMetaInfoManager() {
    }

    @Override
    protected void releaseResources() {
        destroyAll(commands.values(), ApplicationScoped.class);
        commands.clear();
        for (List<CommanderClassMetaInfo> l : subTypeInfo.values()) {
            destroyAll(l, ApplicationScoped.class);
        }
        subTypeInfo.clear();
        super.releaseResources();
    }

    public void registerCommanderMetaInfo(CommanderClassMetaInfo metaInfo) {
        String className = Type.getObjectType(metaInfo.className()).getClassName();
        commands.put(className, metaInfo);
    }

    public CommanderClassMetaInfo commanderMetaInfo(String className) {
        return commands.get(className);
    }

}
