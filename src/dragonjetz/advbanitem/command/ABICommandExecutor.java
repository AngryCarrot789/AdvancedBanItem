package dragonjetz.advbanitem.command;

import dragonjetz.advbanitem.AdvancedBanItem;
import dragonjetz.advbanitem.command.commands.single.DisplayLimiterCommand;
import dragonjetz.advbanitem.command.commands.single.HandItemInfoCommand;
import dragonjetz.advbanitem.command.commands.single.ListAllLimitsCommand;
import dragonjetz.advbanitem.command.commands.single.LookBlockInfoCommand;
import dragonjetz.advbanitem.command.commands.single.ReloadCommand;
import dragonjetz.api.commands.MainCommandExecutor;
import dragonjetz.api.commands.predefined.HelpCommand;
import dragonjetz.api.commands.utils.DJLogger;
import dragonjetz.api.permission.IPermission;

public class ABICommandExecutor extends MainCommandExecutor {
    public static final DJLogger ABILogger = AdvancedBanItem.LOGGER;

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
