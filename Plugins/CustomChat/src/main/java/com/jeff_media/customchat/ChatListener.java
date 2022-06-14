package com.jeff_media.customchat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private static final CustomChat main = CustomChat.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("format","<%s> %s").replace("{color}", main.getColor(event.getPlayer()).toString())));
    }
}
