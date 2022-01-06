package reghzy.advbanitem.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
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
import org.bukkit.plugin.Plugin;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.api.utils.BaseListener;
import reghzy.api.utils.types.BoolRef;

public class PlayerListeners extends BaseListener implements Listener {
    private final LimitManager limitManager;

    public static boolean CountAirAsInteraction = true;
    public static boolean ProcessPlacedBlockInteraction = true;
    public static boolean AllowDropFromInventory = true;

    private final BoolRef destroyOnCancel;

    public PlayerListeners(LimitManager limitManager, Plugin plugin) {
        super(plugin);
        this.limitManager = limitManager;
        this.destroyOnCancel = new BoolRef();
        register(this);
    }

    public static void reloadInfoFromConfig(ConfigurationSection section) {
        CountAirAsInteraction = section.getBoolean("CountAirAsInteraction");
        ProcessPlacedBlockInteraction = section.getBoolean("ProcessPlacedBlockInteraction");
        AllowDropFromInventory = section.getBoolean("AllowDropFromInventory");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteraction(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
            if (CountAirAsInteraction) {
                ItemStack item = event.getItem();
                if (item == null || item.getType() == Material.AIR)
                    return;

                if (limitManager.shouldCancelInteract(event.getPlayer(), item, this.destroyOnCancel)) {
                    event.setCancelled(true);
                    event.getPlayer().closeInventory(); // an item with an inventory; backpack
                    if (destroyOnCancel.getAndSet(false) && event.getPlayer().getItemInHand().equals(item)) {
                        event.getPlayer().setItemInHand(null);
                    }
                }
            }
        }
        else if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() != Material.AIR) {
                if (limitManager.shouldCancelInteract(event.getPlayer(), item, this.destroyOnCancel)) {
                    event.setCancelled(true);
                    event.getPlayer().closeInventory(); // chest maybe
                    if (destroyOnCancel.getAndSet(false) && event.getPlayer().getItemInHand().equals(item)) {
                        event.getPlayer().setItemInHand(null);
                    }

                    return;
                }
            }

            if (ProcessPlacedBlockInteraction) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    if (limitManager.shouldCancelInteract(event.getPlayer(), block, this.destroyOnCancel)) {
                        event.setCancelled(true);
                        event.getPlayer().closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.DROP && AllowDropFromInventory) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR)
            return;

        if (limitManager.shouldCancelInventoryClick((Player) event.getWhoClicked(), item, this.destroyOnCancel)) {
            event.setCancelled(true);
            if (destroyOnCancel.getAndSet(false)) {
                event.setCurrentItem(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (event.getItem() == null)
            return;

        if (limitManager.shouldCancelPickup(event.getPlayer(), event.getItem(), this.destroyOnCancel)) {
            event.setCancelled(true);
        }
    }
}