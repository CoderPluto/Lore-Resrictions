
package org.goblom.lorerestrictions;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Goblom
 */
public class PlayerListener implements Listener {

    private LoreRestrictions plugin;
    public PlayerListener(LoreRestrictions instance) { plugin = instance; }

    @EventHandler
    private void onPlayerSwing(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType().equals(Material.AIR)) return;
        ItemStack handItem = event.getItem();
        Player player = event.getPlayer();
        if (handItem.hasItemMeta()) {
            ItemMeta handMeta = handItem.getItemMeta();
            if (handMeta.hasLore()) {
                List<String> lore = handMeta.getLore();
                if (plugin.isList == false) {
                    if (lore.contains(plugin.getRestrictedWord())) {
                        if (player.hasPermission("lorerestriction." + plugin.getRestrictedWord())) return;
                        if (!player.hasPermission("lorerestriction." + plugin.getRestrictedWord())) {
                           plugin.moveItemFirstEmpty(player, handItem);
                           event.setCancelled(true);
                           plugin.sendMessage(player, plugin.getConfig().getString("Messages." + plugin.getRestrictedWord()));
                        }
                    }
                } else if (plugin.isList == false) {
                    for (String string : plugin.getRestrictedList()) {
                        if (lore.contains(string)) {
                            if (player.hasPermission("lorerestriction." + string)) return;
                            if (!player.hasPermission("lorerestriction." + string)) {
                                plugin.moveItemFirstEmpty(player, handItem);
                                event.setCancelled(true);
                                plugin.sendMessage(player, plugin.getConfig().getString("Messages." + string));
                            }
                        }
                    }
                }
            }
        }
    }
}
