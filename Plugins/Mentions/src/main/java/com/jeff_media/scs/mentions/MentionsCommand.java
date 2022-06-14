package com.jeff_media.scs.mentions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class MentionsCommand implements CommandExecutor {

    private static final Mentions main = Mentions.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            main.reloadConfig();
            return true;
        }
        Player player = (Player) commandSender;
        boolean enabledNow = player.getPersistentDataContainer().has(main.getDisabledKey(), PersistentDataType.BYTE);

        if(enabledNow) {
            player.getPersistentDataContainer().remove(main.getDisabledKey());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("enabled")));
        } else {
            player.getPersistentDataContainer().set(main.getDisabledKey(), PersistentDataType.BYTE, (byte) 1);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("disabled")));
        }
        return true;
    }
}
