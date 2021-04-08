package reghzy.advbanitem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.event.CraftEventFactory;
import org.bukkit.event.Listener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import reghzy.advbanitem.REghZyBasePlugin;

/**
 * A base class for every listener class
 */
public abstract class BaseListener {
    public final REghZyBasePlugin plugin;

    public BaseListener(REghZyBasePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a listener to the bukkit plugin manager using the plugin this instance contains
     */
    public void registerEvent(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, (JavaPlugin) this.plugin);
    }
}
