package reghzy.advbanitem.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.entity.Player;

public class LuckPermsManager implements IPermsManager {
    private LuckPerms perms;
    private UserManager userManager;

    public void init() throws NoPermissionManagerClassException {
        try {
            perms = LuckPermsProvider.get();
            userManager = perms.getUserManager();
        }
        catch (NoClassDefFoundError e) {
            throw new NoPermissionManagerClassException();
        }
    }

    @Override
    public boolean has(Player player, String permission) {
        User user = userManager.getUser(player.getUniqueId());
        if (user == null)
            return false;

        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
