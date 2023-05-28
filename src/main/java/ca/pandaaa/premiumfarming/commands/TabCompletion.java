package ca.pandaaa.premiumfarming.commands;

import ca.pandaaa.premiumfarming.PremiumFarming;
import ca.pandaaa.premiumfarming.ToolsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {
    // Class instances //
    ToolsManager toolsManager = PremiumFarming.getPlugin().getToolsManager();

    // Proposes completion for the PremiumTools command //
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) {
        List<String> completionList = new ArrayList<>();
        if (sender.hasPermission("premiumfarming.config")) {
            // Completion : /premiumfarming < >
            if (args.length == 1) {
                completionList.add("give");
                completionList.add("list");
                completionList.add("reload");
            } else if (args.length == 2) {
                // Completion : /premiumfarming give < >
                if (args[0].equalsIgnoreCase("give")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        completionList.add(player.getName());
                    }
                    return completionList;
                }
            } else if (args.length == 3) {
                // Completion : /premiumfarming give ... < >
                if (args[0].equalsIgnoreCase("give")) {
                    completionList.addAll(toolsManager.getToolsNamesList());
                    return completionList;
                }
            }
        }
        return completionList;
    }
}