package ca.pandaaa.premiumfarming;

import ca.pandaaa.premiumfarming.utils.ConfigManager;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Set;

public class ToolsManager {
    // Class instances //
    private ConfigManager config;

    // Tool lists (same lists for all tools) //
    private HashMap<String, Tools> toolNames = new HashMap<>();
    private HashMap<NamespacedKey, Tools> toolKeys = new HashMap<>();

    public ToolsManager(ConfigManager configManager) {
        // Stores basic informations //
        config = configManager;

        for (String toolName : configManager.getToolsNames()) {
            // Stores the tool to be used later //
            Tools tool = new Tools(toolName, configManager);
            toolNames.put(toolName, tool);
            toolKeys.put(tool.getToolPersistentKey(), tool);
        }
    }

    // Returns true if the tool exists //
    public boolean contains(String name) {
        return toolNames.containsKey(name);
    }
    private boolean contains(NamespacedKey key) { return toolKeys.containsKey(key); }

    // Is the tool list empty (configuration contains no tools) //
    public boolean isEmpty() {
        return toolNames.isEmpty();
    }

    // Returns the tool associated with the name //
    public Tools getTool(String name) {
        if(!contains(name))
            return null;
        return toolNames.get(name);
    }

    // Returns the tool associated with the key //
    public Tools getTool(NamespacedKey key) {
        if(!contains(key))
            return null;
        return toolKeys.get(key);
    }

    // Returns a Set of all the tool names //
    public Set<String> getToolsNamesList() {
        if(isEmpty())
            return null;
        return toolNames.keySet();
    }

    // Returns a Set of all the tool names //
    public Set<NamespacedKey> getToolsKeyList() {
        if(isEmpty())
            return null;
        return toolKeys.keySet();
    }

    // Sends the tool listing messages to the sender //
    public void sendListMessages(CommandSender sender) {
        for(Tools tool : toolNames.values()) {
            sender.sendMessage(config.getListNameMessage(tool.getName(), tool.getItemStackName()));
            for(String descritpionLine : tool.getDescription())
                sender.sendMessage(config.getListDescriptionMessage(descritpionLine));
        }
    }
}
