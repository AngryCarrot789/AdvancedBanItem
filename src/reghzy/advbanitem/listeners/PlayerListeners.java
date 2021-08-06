package reghzy.advbanitem.listeners;

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
import reghzy.mfunclagfind.utils.BaseListener;
import reghzy.mfunclagfind.utils.types.BoolRef;

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteraction(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
            if (CountAirAsInteraction) {
                ItemStack item = event.getItem();
                if (item == null)
                    return;

                if (limitManager.shouldCancelInteract(event.getPlayer(), item, this.destroyOnCancel)) {
                    event.setCancelled(true);
                    if (getAndClear(destroyOnCancel)) {
                        event.getPlayer().setItemInHand(null);
                    }
                }
            }
        }
        else if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null) {
                if (limitManager.shouldCancelInteract(event.getPlayer(), item, this.destroyOnCancel)) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (ProcessPlacedBlockInteraction) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    if (limitManager.shouldCancelInteract(event.getPlayer(), block, this.destroyOnCancel)) {
                        event.setCancelled(true);

                        if (getAndClear(destroyOnCancel)) {
                            event.getPlayer().setItemInHand(null);
                        }
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

        if (limitManager.shouldCancelInventoryClick((Player) event.getWhoClicked(), stack, this.destroyOnCancel)) {
            event.setCancelled(true);

            if (getAndClear(destroyOnCancel)) {
                event.getWhoClicked().setItemInHand(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (event.getItem() == null)
            return;

        if (limitManager.shouldCancelPickup(event.getPlayer(), event.getItem(), this.destroyOnCancel)) {
            event.setCancelled(true);

            if (getAndClear(destroyOnCancel)) {
                event.getPlayer().setItemInHand(null);
            }
        }
    }

    public static boolean getAndClear(BoolRef ref) {
        boolean value = ref.getValue();
        ref.setValue(false);
        return value;
    }
}