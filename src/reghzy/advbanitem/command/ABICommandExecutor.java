package reghzy.advbanitem.command;

import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.commands.single.DisplayLimiterCommand;
import reghzy.advbanitem.command.commands.single.HandItemInfoCommand;
import reghzy.advbanitem.command.commands.single.ListAllLimitsCommand;
import reghzy.advbanitem.command.commands.single.LookBlockInfoCommand;
import reghzy.advbanitem.command.commands.single.ReloadCommand;
import reghzy.mfunclagfind.command.MainCommandExecutor;
import reghzy.mfunclagfind.command.commands.single.HelpCommand;
import reghzy.mfunclagfind.command.utils.REghZyLogger;
import reghzy.mfunclagfind.permissions.IPermission;

public class ABICommandExecutor extends MainCommandExecutor {
    public static final REghZyLogger ABILogger = AdvancedBanItem.LOGGER;

    public ABICommandExecutor() {
        super("abi", ABILogger, "Main command for advanced ban item!");
    }

    @Override
    public void registerCommands() {
        registerCommand(new HelpCommand("abi", this.getCommands()));
        registerClass(DisplayLimiterCommand.class);
        registerClass(ListAllLimitsCommand.class);
        registerClass(ReloadCommand.class);
        registerClass(LookBlockInfoCommand.class);
        registerClass(HandItemInfoCommand.class);
    }

    @Override
    public IPermission getPermission() {
        return ABIPermission.MAIN_COMMAND;
    }
}
