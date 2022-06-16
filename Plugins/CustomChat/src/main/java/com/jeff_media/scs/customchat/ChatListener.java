package com.jeff_media.scs.customchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

public class ChatListener implements Listener {

    private static final CustomChat main = CustomChat.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("format").replace("{name}", main.getPrefix(event.getPlayer(), false)).replace("{message}", event.getMessage().replace("%","%%"))));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(event.getPlayer().getPersistentDataContainer().has(main.getChatColor1Key(), PersistentDataType.INTEGER)) {
            event.getPlayer().getPersistentDataContainer().remove(main.getChatColor1Key());
        }
        if(event.getPlayer().getPersistentDataContainer().has(main.getChatColor2Key(), PersistentDataType.INTEGER)) {
            event.getPlayer().getPersistentDataContainer().remove(main.getChatColor2Key());
        }
        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("join").replace("{name}", main.getPrefix(event.getPlayer(), false))));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("leave").replace("{name}", main.getPrefix(event.getPlayer(), false))));
    }

    @EventHandler
    public void onMention(AsyncPlayerChatEvent event) {
        String[] split = event.getMessage().split(" ");
        //for(String word : split) {
        for(int i = 0; i < split.length; i++) {
            String word = split[i];
            if(!word.startsWith("@") || word.length() == 1) {
                continue;
            }
            word = word.substring(1);
            Player mentioned = Bukkit.getPlayer(word);
            if(mentioned == null || mentioned.equals(event.getPlayer())) {
                continue;
            }
            split[i] = "@" + ChatColor.translateAlternateColorCodes('&',main.getPrefix(mentioned, false)) + ChatColor.RESET;
            CustomChat.getInstance().mention(event.getPlayer(), mentioned);
        }
        event.setMessage(String.join(" ", split));
    }
}
