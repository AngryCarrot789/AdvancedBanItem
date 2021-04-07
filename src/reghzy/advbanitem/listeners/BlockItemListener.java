package reghzy.advbanitem.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.REghZyBasePlugin;

public class BlockItemListener extends BaseListener implements Listener {
    private final LimitManager limitManager;

    public static boolean CountAirAsInteraction = true;
    public static boolean ProcessPlacedBlockInteraction = true;

    public BlockItemListener(LimitManager limitManager, REghZyBasePlugin plugin) {
        super(plugin);
        this.limitManager = limitManager;
        registerEvent(this);
    }

    public static void reloadInfoFromConfig(Config config) {
        CountAirAsInteraction = config.getBoolean("CountAirAsInteraction");
        ProcessPlacedBlockInteraction = config.getBoolean("ProcessPlacedBlockInteraction");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (limitManager.shouldCancelBlockBreak(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (limitManager.shouldCancelBlockPlace(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
            if (CountAirAsInteraction) {
                ItemStack item = event.getItem();
                if (item == null)
                    return;

                if (limitManager.shouldCancelInteract(player, item.getType().getId(), item.getData().getData())) {
                    event.setCancelled(true);
                }
            }
        }
        else if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null) {
                if (limitManager.shouldCancelInteract(player, item.getType().getId(), item.getData().getData())) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (ProcessPlacedBlockInteraction) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    if (limitManager.shouldCancelInteract(player, block)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}