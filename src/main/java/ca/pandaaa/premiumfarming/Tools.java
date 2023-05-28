package ca.pandaaa.premiumfarming;

import ca.pandaaa.premiumfarming.tools.ToolType;
import ca.pandaaa.premiumfarming.utils.ConfigManager;
import ca.pandaaa.premiumfarming.utils.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Tools {
    // Class instances //
    PremiumFarming plugin = PremiumFarming.getPlugin();
    private final ConfigManager config;

    // ItemStack attributes //
    private ItemStack tool;
    private ItemMeta toolMeta;
    private Material material;
    private String itemStackName;
    private ArrayList<String> lore;
    private boolean isEnchanted;
    private NamespacedKey toolKey;

    // Tool attributes //
    private final String toolName;
    private Sound sound;
    private ToolType type;
    private int uses;
    private List<String> description;

    // Tool instance creator //
    public Tools(String toolIdentificator, ConfigManager configManager) {
        // Stores basic informations //
        toolName = toolIdentificator;
        config = configManager;
        toolKey = new NamespacedKey(plugin, "PremiumFarming." + toolName + ".toolUses");

        // Creates & store the tool ItemStack //
        createItemStackAttributes();
        createItemStack();

        // Stores configuration settings & messages //
        createConfigAttributes();
    }

    // Creates the attributes that are needed to create the tool ItemStack //
    private void createItemStackAttributes() {
        uses = config.getToolUses(toolName);
        type = config.getToolType(toolName);
        material = config.getToolMaterial(toolName);
        itemStackName = Utils.applyFormat(config.getToolName(toolName));
        lore = getToolLore(uses);
        isEnchanted = config.getToolEnchanted(toolName);
    }

    // Creates and returns the tool lore as a List //
    private ArrayList<String> getToolLore(int uses) {
        ArrayList<String> loreList = new ArrayList<>();
        for(String loreMessage : config.getToolLore(toolName))
            loreList.add(Utils.applyFormat(loreMessage.replace("%amount%", String.valueOf(uses))));
        return loreList;
    }

    public NamespacedKey getToolPersistentKey() {
        return toolKey;
    }

    // Creates and returns the ItemStack //
    private void createItemStack() {
        tool = new ItemStack(material);

        toolMeta = tool.getItemMeta();

        if (toolMeta == null)
            return;
        toolMeta.setDisplayName(itemStackName);
        toolMeta.setLore(lore);
        toolMeta.getPersistentDataContainer().set(toolKey, PersistentDataType.INTEGER, uses);
        if (isEnchanted) {
            toolMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            toolMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(material.equals(Material.PLAYER_HEAD)) {
            SkullMeta skullMeta = (SkullMeta) toolMeta;
            // TODO Deprecated
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(config.getToolSkullOwner(toolName)));
            toolMeta = skullMeta;
        }
        tool.setItemMeta(toolMeta);
    }

    // Creates the tool settings and messages attributes //
    private void createConfigAttributes() {
        description = config.getToolDescription(toolName);
        uses = config.getToolUses(toolName);
        type = config.getToolType(toolName);
        sound = config.getToolSound(toolName);
    }

    // Returns the name of the tool //
    public String getName() {
        return toolName;
    }

    // Returns the name of the itemStack //
    public String getItemStackName() { return itemStackName; }

    // Returns the description (list) of the tool //
    public List<String> getDescription() {
        if(description.isEmpty())
            return null;
        return description;
    }

    // Gives the tool to a player (with a specified amount) //
    public void giveTool(Player player) {
        player.getInventory().addItem(tool);
    }

    // Uses the tool (can delete the tool if it has one use left) //
    public void useTool(Player player, ItemStack item, Location clickLocation, NamespacedKey key) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if(container.has(key, PersistentDataType.INTEGER)) {
            int usesValue = container.get(key, PersistentDataType.INTEGER);
            if(usesValue > 1) {
                itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, usesValue - 1);
                itemMeta.setLore(getToolLore(usesValue - 1));
                item.setItemMeta(itemMeta);
            }
            else if(usesValue == 1)
                player.getInventory().removeItem(item);
        }

        // Sends the sound to the player if it is not null //
        if (sound != null)
            player.playSound(player.getLocation(), sound, 1, 1);
    }
}
