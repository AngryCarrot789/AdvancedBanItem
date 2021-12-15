package reghzy.advbanitem.command;

import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.commands.single.DisplayLimiterCommand;
import reghzy.advbanitem.command.commands.single.HandItemInfoCommand;
import reghzy.advbanitem.command.commands.single.ListAllLimitsCommand;
import reghzy.advbanitem.command.commands.single.LookBlockInfoCommand;
import reghzy.advbanitem.command.commands.single.ReloadCommand;
import reghzy.api.commands.MainCommandExecutor;
import reghzy.api.commands.predefined.HelpCommand;
import reghzy.api.commands.utils.RZLogger;
import reghzy.api.permission.IPermission;

public class ABICommandExecutor extends MainCommandExecutor {
    public static final RZLogger ABILogger = AdvancedBanItem.LOGGER;

    public ABICommandExecutor() {
        super("abi", ABILogger, "Main command for advanced ban item!");
    }

    @Override
    public void registerCommands() {
        registerCommand(new HelpCommand("abi", this.getCommands(), ABIPermission.HELP));
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
