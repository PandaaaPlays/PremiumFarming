package ca.pandaaa.premiumfarming;

import ca.pandaaa.utils.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class PremiumFarming extends JavaPlugin {

    private static PremiumFarming plugin;

    @Override
    public void onEnable() {
        plugin = this;
        int pluginID = 18531;
        Metrics metrics = new Metrics(this, pluginID);

        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "   &2_____  &a_____ "));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|  _  |&a|   __|    &2Prem&aium&8Farm&7ing"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|   __|&a|   __|    &7Version " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|__|   &a|__|         &7by &8Pa&7nd&5aaa"));
        getServer().getConsoleSender().sendMessage("");
    }
}
