package reghzy.advbanitem.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import reghzy.advbanitem.REghZyBasePlugin;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.limit.LimitManager;

public class PlayerListeners extends BaseListener implements Listener {
    private final LimitManager limitManager;

    public static boolean CountAirAsInteraction = true;
    public static boolean ProcessPlacedBlockInteraction = true;
    public static boolean AllowDropFromInventory = true;

    public PlayerListeners(LimitManager limitManager, REghZyBasePlugin plugin) {
        super(plugin);
        this.limitManager = limitManager;
        registerEvent(this);
    }

    public static void reloadInfoFromConfig(Config config) {
        CountAirAsInteraction = config.getBoolean("CountAirAsInteraction");
        ProcessPlacedBlockInteraction = config.getBoolean("ProcessPlacedBlockInteraction");
        AllowDropFromInventory = config.getBoolean("AllowDropFromInventory");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteraction(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
            if (CountAirAsInteraction) {
                ItemStack item = event.getItem();
                if (item == null)
                    return;

                if (limitManager.shouldCancelInteract(event.getPlayer(), item.getTypeId(), item.getData().getData())) {
                    event.setCancelled(true);
                }
            }
        }
        else if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null) {
                if (limitManager.shouldCancelInteract(event.getPlayer(), item.getTypeId(), item.getData().getData())) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (ProcessPlacedBlockInteraction) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    if (limitManager.shouldCancelInteract(event.getPlayer(), block)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.DROP && AllowDropFromInventory) {
            return;
        }

        ItemStack stack = event.getCurrentItem();
        if (stack == null)
            return;

        if (limitManager.shouldCancelInventoryClick((Player) event.getWhoClicked(), stack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (event.getItem() == null)
            return;

        if (limitManager.shouldCancelPickup(event.getPlayer(), event.getItem())) {
            event.setCancelled(true);
        }
    }
}