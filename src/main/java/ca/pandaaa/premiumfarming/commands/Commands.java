package ca.pandaaa.premiumfarming.commands;

import ca.pandaaa.premiumfarming.PremiumFarming;
import ca.pandaaa.premiumfarming.Tools;
import ca.pandaaa.premiumfarming.ToolsManager;
import ca.pandaaa.premiumfarming.utils.ConfigManager;
import ca.pandaaa.premiumfarming.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    // Class instances //
    PremiumFarming plugin = PremiumFarming.getPlugin();
    ConfigManager configManager = plugin.getConfigManager();
    ToolsManager toolsManager = plugin.getToolsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
        // If the sender is neither a player nor the console, cancel! //
        if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) return false;

        if (command.getName().equalsIgnoreCase("premiumfarming")) {
            // If there are no arguments, sends the error and return false //
            if (args.length == 0) {
                sendUnknownCommandMessage(sender);
                return false;
            }

            // Checks the first argument //
            switch (args[0].toLowerCase()) {
                // "reload" will reload the configurations //
                case "reload":
                    reloadPlugin(sender);
                    break;
                // "list" will print all the tools in the sender's chat //
                case "list":
                    toolList(sender);
                    break;
                // "give" will give the tool to the specified player //
                case "give":
                    giveTool(sender, args);
                    break;
                // Anything else will send the error //
                default:
                    sendUnknownCommandMessage(sender);
                    break;
            }
        }
        return false;
    }

    // Reloads the plugin //
    public void reloadPlugin(CommandSender sender) {
        if (!sender.hasPermission("premiumfarming.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        // Reloads the configurations and sends the confirmation message //
        plugin.reloadConfig(sender);
    }

    // Tools listing : Sends a list of all the available tools to the sender //
    private void toolList(CommandSender sender) {
        if (!sender.hasPermission("premiumfarming.config")) {
            sendNoPermissionMessage(sender);
            return;
        }
        // If no tool is specified in the configuration //
        if (toolsManager.isEmpty())
            return;

        // Sends the listing messages to the sender //
        toolsManager.sendListMessages(sender);
    }

    // Give tool : Gives the specified player the tool //
    private void giveTool(CommandSender sender, String[] args) {
        if (!sender.hasPermission("premiumfarming.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        // Command : /premiumfarming give //
        if (args.length != 3) {
            sendIncorrectGiveArgumentsMessage(sender);
            return;
        }

        String playerString = args[1];
        String toolID = args[2];

        // Command : /premiumfarming give %incorrect_player% //
        if (Bukkit.getPlayer(playerString) == null) {
            sendIncorrectGivePlayerMessage(sender, playerString);
            return;
        }
        Player player = Bukkit.getPlayer(playerString);

        // Command: /premiumfarming give %player% %incorrect_tool% //
        if (!toolsManager.contains(toolID)) {
            sendIncorrectGiveToolMessage(sender, toolID);
            return;
        }

        // Gets the Tools instance for the tool name (ID) specified //
        Tools tool = toolsManager.getTool(toolID);

        // Gives the tools to the player //
        tool.giveTool(player);

        // Sends the message to the sender & the receiver! //
        sender.sendMessage(configManager.getToolSentMessage(player.getName(), toolID, tool.getName()));
        String senderName = "Console";
        if (sender instanceof Player)
            senderName = sender.getName();
        player.sendMessage(configManager.getToolReceivedMessage(senderName, toolID, tool.getName()));
    }

    // Sends the unknownCommand error message //
    private void sendUnknownCommandMessage(CommandSender sender) {
        sender.sendMessage(configManager.getUnknownCommandMessage());
    }

    // Sends the noPermission error message //
    private void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(configManager.getNoPermissionMessage());
    }

    // Sends the incorrect give arguments message //
    private void sendIncorrectGiveArgumentsMessage(CommandSender sender) {
        sender.sendMessage(configManager.getIncorrectGiveArgumentsMessage());
    }

    // Sends the incorrect give player message //
    private void sendIncorrectGivePlayerMessage(CommandSender sender, String player) {
        sender.sendMessage(configManager.getIncorrectGivePlayerMessage(player));
    }

    // Sends the incorrect give tool message //
    private void sendIncorrectGiveToolMessage(CommandSender sender, String tool) {
        sender.sendMessage(configManager.getIncorrectGiveToolMessage(tool));
    }
}
