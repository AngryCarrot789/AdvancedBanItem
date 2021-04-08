package reghzy.advbanitem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import reghzy.advbanitem.REghZyBasePlugin;
import reghzy.advbanitem.limit.LimitManager;

public class BlockListeners extends BaseListener implements Listener {
    private final LimitManager limitManager;

    public BlockListeners(LimitManager limitManager, REghZyBasePlugin plugin) {
        super(plugin);
        this.limitManager = limitManager;
        registerEvent(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (limitManager.shouldCancelBlockBreak(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (limitManager.shouldCancelBlockPlace(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }
}