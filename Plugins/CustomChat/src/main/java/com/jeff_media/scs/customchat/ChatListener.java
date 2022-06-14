package com.jeff_media.scs.customchat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class ChatListener implements Listener {

    private static final CustomChat main = CustomChat.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("format").replace("{name}", main.getPrefix(event.getPlayer())).replace("{message}", event.getMessage().replace("%","%%"))));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(event.getPlayer().getPersistentDataContainer().has(main.getChatColor1Key(), PersistentDataType.INTEGER)) {
            event.getPlayer().getPersistentDataContainer().remove(main.getChatColor1Key());
        }
        if(event.getPlayer().getPersistentDataContainer().has(main.getChatColor2Key(), PersistentDataType.INTEGER)) {
            event.getPlayer().getPersistentDataContainer().remove(main.getChatColor2Key());
        }
    }
}
