package ca.pandaaa.premiumfarming;

import ca.pandaaa.premiumfarming.commands.Commands;
import ca.pandaaa.premiumfarming.commands.TabCompletion;
import ca.pandaaa.premiumfarming.listeners.ClickListener;
import ca.pandaaa.premiumfarming.utils.ConfigManager;
import ca.pandaaa.premiumfarming.utils.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class PremiumFarming extends JavaPlugin {

    private File toolsFile;
    private FileConfiguration toolsConfig;

    private static PremiumFarming plugin;
    private ConfigManager configManager;
    private ToolsManager toolsManager;

    // What happens when the plugin starts //
    @Override
    public void onEnable() {
        // Plugin singleton //
        plugin = this;

        boolean test = true;

        // Initializes the BStats metrics //
        int pluginID = 18531;
        Metrics metrics = new Metrics(this, pluginID);

        saveDefaultConfigurations();
        loadToolsConfig();
        configManager = new ConfigManager(getConfig(), toolsConfig);
        toolsManager = new ToolsManager(configManager);

        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getCommands();

        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "   &2_____  &a_____ "));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|  _  |&a|   __|    &2Prem&aium&8Farm&7ing"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|   __|&a|   __|    &7Version " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|__|   &a|__|         &7by &8Pa&7nd&5aaa"));
        getServer().getConsoleSender().sendMessage("");

        if(test) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + getDescription().getName() + " : This version of the plugin is meant for testing / may have bugs."));
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease try to update to the next fully working version as soon as possible."));
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease contact Pandaaa on discord if you believe this is an error."));
        }
    }

    // Saves the default configuration files (creates them in the case that they don't already exist) //
    private void saveDefaultConfigurations() {
        this.saveDefaultConfig();
        toolsFile = new File(getDataFolder(), "tools.yml");
        if (!toolsFile.exists())
            saveResource("tools.yml", false);
        toolsConfig = new YamlConfiguration();
    }

    // Tries to load the tools.yml file //
    private void loadToolsConfig() {
        try {
            toolsConfig.load(toolsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    // Creates or changes the command //
    private void getCommands() {
        getCommand("PremiumFarming").setExecutor(new Commands());
        getCommand("PremiumFarming").setTabCompleter(new TabCompletion());
    }

    // Returns the plugin //
    public static PremiumFarming getPlugin() {
        return plugin;
    }

    // Returns the configuration manager (the same one is use troughout the entire plugin) //
    public ConfigManager getConfigManager() {
        return configManager;
    }

    // Reloads the configurations //
    public void reloadConfig(CommandSender sender) {
        // Deletes the config data (not in the file but in the program) //
        plugin.reloadConfig();
        // Replaces the messages data by the ones from the file //
        toolsConfig = YamlConfiguration.loadConfiguration(toolsFile);

        // Replaces the commands and attributes //
        configManager = new ConfigManager(getConfig(), toolsConfig);
        toolsManager = new ToolsManager(configManager);
        HandlerList.unregisterAll();
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getCommands();

        // Sends the confirmation message to the command executor //
        sender.sendMessage(configManager.getPluginReloadMessage());
    }

    // Returns the tools manager (the same one is use troughout the entire plugin) //
    public ToolsManager getToolsManager() {
        return toolsManager;
    }
}
