package com.jeff_media.scs.customchat;

import net.kyori.adventure.text.minimessage.MiniMessage;
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
        ChatColor color1 = main.getColor(args[0]);
        if(color1 == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c\"" + args[0] + "\" not a valid color. Valid colors are the default color codes (0-9 and a-z) or a hex color."));
            return true;
        }
        ChatColor color2 = color1;
        if(args.length > 1) {
            color2 = main.getColor(args[1]);
            if(color2 == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c\"" + args[1] + "\" not a valid color. Valid colors are the default color codes (0-9 and a-z) or a hex color."));
                return true;
            }
        }

        String hex1 = CustomChat.getRgbHex(color1.getColor());
        String hex2 = CustomChat.getRgbHex(color2.getColor());
        player.getPersistentDataContainer().set(main.getChatColor1Key(), PersistentDataType.STRING, hex1);
        player.getPersistentDataContainer().set(main.getChatColor2Key(), PersistentDataType.STRING, hex2);

        main.getPrefixes().remove(player.getUniqueId());

        String msg = "<gradient:" + hex1 + ":" + hex2 + ">This is your new chat color :3";
        main.getAdventure().player(player).sendMessage(MiniMessage.miniMessage().deserialize(msg));
        return true;
    }
}
