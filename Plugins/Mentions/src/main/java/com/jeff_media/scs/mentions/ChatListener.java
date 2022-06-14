package com.jeff_media.scs.mentions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onMention(AsyncPlayerChatEvent event) {
        String[]split = event.getMessage().split(" ");
        for(String word : split) {
            if(!word.startsWith("@") || word.length() == 1) {
                continue;
            }
            word = word.substring(1);
            Player player = Bukkit.getPlayer(word);
            if(player == null || player.equals(event.getPlayer())) {
                continue;
            }
            Mentions.getInstance().mention(event.getPlayer(), player);
        }
    }
}
