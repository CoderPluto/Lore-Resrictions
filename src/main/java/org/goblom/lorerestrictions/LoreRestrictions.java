
package org.goblom.lorerestrictions;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Goblom
 */
public class LoreRestrictions extends JavaPlugin {
    
    private LoreRestrictions instance;
    private JavaPlugin plugin;
    
    public boolean isList = false;
    public String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Lore Restrictions" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;
    public LoreRestrictions getInstance() { return instance; }
    public JavaPlugin getPlugin() { return plugin; }
    
    public void onEnable() {
        saveDefaultConfig();
        checkRestricted();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), plugin);
    }
    
    public void onDisable() { HandlerList.unregisterAll(this); }
    
    public void checkRestricted() { 
        if (getConfig().isList("Restricted-Words")) isList = true;
        if (isList) {
            for (String word : getConfig().getStringList("Restricted-Words")) {
                if (getConfig().contains("Messages." + word)) return;
                if (!getConfig().contains("Messages." + word)) {
                    getConfig().set("Messages." + word, "You do not have permission to use this Item.");
                    reloadConfig();
                    
                    /* Use addDefault */ //{
                    /* Use addDefault */ //    Configuration defaults = getConfig().getDefaults();
                    /* Use addDefault */ //    defaults.addDefault("Messages." + word, "You do not have permission to use this Item.");
                    /* Use addDefault */ //    getConfig().setDefaults(defaults);
                    /* Use addDefault */ //    reloadConfig();
                    /* Use addDefault */ //}
                }
            }
        } else {
            getServer().getPluginManager().disablePlugin(this);
            getLogger().severe("Lore Restrictions Disabled!");
            getLogger().severe("Restricted-Words MUST BE A STRING LIST!");
        }
    }
    public List<String> getRestrictedList() { return getConfig().getStringList("Restricted-Words"); }
    public String getRestrictedWord() { return getConfig().getString("Restricted-Words"); }
    public void sendMessage(Player player, String message) { player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message)); }
    
    public void moveItemFirstEmpty(Player player, ItemStack itemStack) {
        int firstEmpty = player.getInventory().firstEmpty();
        player.getInventory().remove(itemStack);
        player.getInventory().setItem(firstEmpty, itemStack);
        player.updateInventory();
    }
}
