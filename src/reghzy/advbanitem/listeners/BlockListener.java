package reghzy.advbanitem.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.REghZyBasePlugin;

public class BlockListener extends BaseListener implements Listener {
    private final LimitManager limitManager;

    public static boolean CountAirAsInteraction = true;

    public BlockListener(LimitManager limitManager, REghZyBasePlugin plugin) {
        super(plugin);
        this.limitManager = limitManager;
        registerEvent(this);
    }

    public static void reloadInfoFromConfig(Config config) {
        CountAirAsInteraction = config.getBoolean("CountAirAsInteraction");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getBlock() == null)
            return;

        if (limitManager.shouldCancelBlockBreak(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getBlock() == null)
            return;

        if (limitManager.shouldCancelBlockPlace(player, event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            if (limitManager.shouldCancelBlockInteract(player, event.getClickedBlock())) {
                event.setCancelled(true);
            }
        }
        else if (CountAirAsInteraction && (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR)) {
            if (limitManager.shouldCancelBlockInteract(player, event.getItem().getTypeId(), event.getItem().getData().getData())) {
                event.setCancelled(true);
            }
        }
    }
}