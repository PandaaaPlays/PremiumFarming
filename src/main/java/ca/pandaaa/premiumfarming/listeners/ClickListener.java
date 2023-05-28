package ca.pandaaa.premiumfarming.listeners;

import ca.pandaaa.premiumfarming.PremiumFarming;
import ca.pandaaa.premiumfarming.ToolsManager;
import ca.pandaaa.premiumfarming.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ClickListener implements Listener {
    // Class instances //
    PremiumFarming plugin = PremiumFarming.getPlugin();
    ToolsManager toolsManager = plugin.getToolsManager();

    // What should happen when a player right clicks (air or block!) //
    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        Action action = event.getAction();
        if(!action.equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(!event.getHand().equals(EquipmentSlot.HAND))
            return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().equals(Material.AIR))
            return;

        // If the player is holding a tool //
        playerItemInHand(player, item, event.getClickedBlock().getLocation());
    }

    // Uses the tool (if it is a PremiumFarming tool) //
    private void playerItemInHand(Player player, ItemStack item, Location location) {
        ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        for(NamespacedKey key : container.getKeys()) {

            if(toolsManager.getToolsKeyList().contains(key))
                toolsManager.getTool(key).useTool(player, item, location, key);

        }
    }
}
