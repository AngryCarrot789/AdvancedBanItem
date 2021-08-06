package reghzy.advbanitem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.mfunclagfind.utils.BaseListener;
import reghzy.mfunclagfind.utils.types.BoolRef;

public class BlockListeners extends BaseListener implements Listener {
    private final LimitManager limitManager;
    private final BoolRef destroyOnCancel;

    public BlockListeners(LimitManager limitManager, Plugin plugin) {
        super(plugin);
        this.limitManager = limitManager;
        this.destroyOnCancel = new BoolRef();
        register(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (limitManager.shouldCancelBlockBreak(event.getPlayer(), event.getBlock(), destroyOnCancel)) {
            event.setCancelled(true);

            if (PlayerListeners.getAndClear(destroyOnCancel)) {
                event.getPlayer().setItemInHand(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (limitManager.shouldCancelBlockPlace(event.getPlayer(), event.getBlock(), destroyOnCancel)) {
            event.setCancelled(true);

            if (PlayerListeners.getAndClear(destroyOnCancel)) {
                event.getPlayer().setItemInHand(null);
            }
        }
    }
}