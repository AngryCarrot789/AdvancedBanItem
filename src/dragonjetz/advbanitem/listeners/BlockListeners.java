package dragonjetz.advbanitem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import dragonjetz.advbanitem.limit.LimitManager;
import dragonjetz.api.utils.BaseListener;
import dragonjetz.api.utils.types.BoolRef;

public class BlockListeners extends BaseListener implements Listener {
    private final LimitManager limitManager;
    private final BoolRef destroyOnCancel;

    public BlockListeners(LimitManager limitManager, Plugin plugin) {
        super(plugin);
        this.limitManager = limitManager;
        this.destroyOnCancel = new BoolRef();
        register(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (limitManager.shouldCancelBlockBreak(event.getPlayer(), event.getBlock(), destroyOnCancel)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (limitManager.shouldCancelBlockPlace(event.getPlayer(), event.getBlock(), destroyOnCancel)) {
            event.setCancelled(true);
            if (destroyOnCancel.getAndSet(false) && event.getPlayer().getItemInHand().equals(event.getItemInHand())) {
                event.getPlayer().setItemInHand(null);
                event.getPlayer().closeInventory();
            }
        }
    }
}