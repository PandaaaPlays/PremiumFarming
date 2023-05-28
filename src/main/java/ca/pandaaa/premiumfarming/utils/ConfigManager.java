package ca.pandaaa.premiumfarming.utils;

import ca.pandaaa.premiumfarming.tools.ToolType;
import ca.pandaaa.premiumfarming.tools.Tractor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Set;

public class ConfigManager {
    // Configuration files //
    private final FileConfiguration configuration;
    private final FileConfiguration tools;

    // Constructor //
    public ConfigManager(FileConfiguration configuration, FileConfiguration tools) {
        this.configuration = configuration;
        this.tools = tools;
    }

    // Returns the unknown command message //
    public String getUnknownCommandMessage() {
        return Utils.applyFormat(configuration.getString("unknown-command"));
    }

    // Returns the no permission message //
    public String getNoPermissionMessage() {
        return Utils.applyFormat(configuration.getString("no-permission"));
    }

    // Returns the incorrect give argument(s) message //
    public String getIncorrectGiveArgumentsMessage() {
        return Utils.applyFormat(configuration.getString("incorrect-give-arguments"));
    }

    // Returns an array with the tools names (identificators) //
    public String[] getToolsNames() {
        Set<String> toolNamesSet = tools.getConfigurationSection("tools").getKeys(false);
        String[] toolsNamesList = new String[toolNamesSet.size()];
        return toolNamesSet.toArray(toolsNamesList);
    }

    // Returns the tool name (ItemStack name) //
    public String getToolName(String toolName) {
        return tools.getString("tools." + toolName + ".name");
    }

    // Returns the description of the tool //
    public List<String> getToolDescription(String toolName) {
        return tools.getStringList("tools." + toolName + ".description");
    }

    // Returns the list of lores applied on the ToolStack //
    public List<String> getToolLore(String toolName) {
        return tools.getStringList("tools." + toolName + ".lore");
    }

    // Returns the material of the tool (if valid!) //
    public Material getToolMaterial(String toolName) {
        try {
            return Material.valueOf(tools.getString("tools." + toolName + ".item"));
        } catch(Exception exception) {
            return null;
        }
    }

    // Returns the owner of the skull (only applicable if material = PLAYER_HEAD) //
    public String getToolSkullOwner(String toolName) {
        try {
            return tools.getString("tools." + toolName + ".owner");
        } catch(Exception exception) {
            return null;
        }
    }

    // Returns true if the tool should be enchanted //
    public boolean getToolEnchanted(String toolName) {
        return tools.getBoolean("tools." + toolName + ".enchanted");
    }

    // Returns the type of the tool (tractor, etc.) //
    public ToolType getToolType(String toolName) {
        String type = tools.getString("tools." + toolName + ".type");
        if(type.equalsIgnoreCase("Tractor"))
            return new Tractor();
        return null;
    }

    // Returns the amount of uses of the tool (-1 is infinite) //
    public int getToolUses(String toolName) {
        return tools.getInt("tools." + toolName + ".uses");
    }

    // Returns the tool sound (if valid!) //
    public Sound getToolSound(String toolName) {
        try {
            return Sound.valueOf(tools.getString("tools." + toolName + ".sound"));
        } catch(Exception exception) {
            return null;
        }
    }

    // Returns the incorrect give player message //
    public String getIncorrectGivePlayerMessage(String player) {
        return Utils.applyFormat(configuration.getString("incorrect-give-player").replaceAll("%player%", player));
    }

    // Returns the incorrect give tool message //
    public String getIncorrectGiveToolMessage(String tool) {
        return Utils.applyFormat(configuration.getString("incorrect-give-tool").replaceAll("%tool%", tool));
    }

    // Returns the plugin reload command message //
    public String getPluginReloadMessage() {
        return Utils.applyFormat(configuration.getString("plugin-reload"));
    }

    // Returns the format for the tool listing (tool "title") //
    public String getListNameMessage(String tool, String toolName) {
        return Utils.applyFormat(configuration.getString("list-name").replaceAll("%tool%", toolName).replaceAll("%tool_id%", tool));
    }

    // Returns the format for the tool listing (tool description) //
    public String getListDescriptionMessage(String description) {
        return Utils.applyFormat(configuration.getString("list-description").replaceAll("%description%", description));
    }

    // Returns the message sent when receiving a tool //
    public String getToolReceivedMessage(String sender, String tool, String toolName) {
        return Utils.applyFormat(configuration.getString("tool-received-message").replaceAll("%sender%", sender).replaceAll("%tool%", toolName).replaceAll("%tool_id%", tool));
    }

    // Returns the message sent when giving a tool //
    public String getToolSentMessage(String receiver, String tool, String toolName) {
        return Utils.applyFormat(configuration.getString("tool-sent-message").replaceAll("%receiver%", receiver).replaceAll("%tool%", toolName).replaceAll("%tool_id%", tool));
    }
}