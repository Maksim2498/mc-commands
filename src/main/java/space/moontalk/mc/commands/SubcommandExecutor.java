package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

public interface SubcommandExecutor {
    void onSubcommand(@NotNull SubcommandCall call) throws Exception; 
}
