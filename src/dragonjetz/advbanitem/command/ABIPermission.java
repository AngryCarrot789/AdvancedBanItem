package dragonjetz.advbanitem.command;

import dragonjetz.api.permission.IPermission;
import dragonjetz.api.permission.PermissionManager;

public class ABIPermission {
    public static final IPermission MAIN_COMMAND = PermissionManager.registerPermission("ABI_MAIN_COMMAND", "advbanitem.maincommand");
    public static final IPermission DISPLAY_LIMITERS = PermissionManager.registerPermission("ABI_DISPLAY_LIMITERS", "advbanitem.display");
    public static final IPermission SHOW_HAND_ITEM = PermissionManager.registerPermission("ABI_SHOW_HAND_ITEM", "advbanitem.hand");
    public static final IPermission SHOW_LOOKING_BLOCK = PermissionManager.registerPermission("ABI_SHOW_LOOKING_BLOCK", "advbanitem.look");
    public static final IPermission HELP = PermissionManager.registerPermission("ABI_HELP", "advbanitem.help");
    public static final IPermission RELOAD_CONFIG = PermissionManager.registerPermission("ABI_RELOAD_CONFIG", "advbanitem.reload");
    public static final IPermission LIST_BANNED_IDS = PermissionManager.registerPermission("ABI_LIST_BANNED_IDS", "advbanitem.listall");
}
