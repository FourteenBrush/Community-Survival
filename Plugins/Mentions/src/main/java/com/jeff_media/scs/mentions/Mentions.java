package com.jeff_media.scs.mentions;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.UUID;

public class Mentions extends JavaPlugin {

    private static Mentions instance;

    {
        instance = this;
    }

    public static Mentions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatListener(),this);
    }



    public void mention(Player player, Player mentioned) {
        mentioned.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A));
        mentioned.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',getConfig().getString("message").replace("{name}",player.getName()))));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',getConfig().getString("message2").replace("{name}",mentioned.getName()))));
    }
}
