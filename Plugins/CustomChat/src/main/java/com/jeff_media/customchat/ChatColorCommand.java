package com.jeff_media.customchat;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class ChatColorCommand implements CommandExecutor {

    private static final CustomChat main = CustomChat.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            main.reloadConfig();
            return true;
        }
        Player player = (Player) commandSender;
        if(args.length == 0) {
            return false;
        }
        ChatColor color = main.getColor(args[0]);
        if(color == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThat's not a valid color. Valid colors are the default color codes (0-9 and a-z) or a hex color."));
            return true;
        }
        player.getPersistentDataContainer().set(main.getChatColorKey(), PersistentDataType.STRING, args[0]);
        player.sendMessage(color + "This is your new chat color :3");
        return true;
    }
}
