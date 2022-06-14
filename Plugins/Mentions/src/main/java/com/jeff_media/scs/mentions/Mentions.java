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
    private final NamespacedKey disabledKey = new NamespacedKey(this,"disabled");

    {
        instance = this;
    }

    public static Mentions getInstance() {
        return instance;
    }

    public NamespacedKey getDisabledKey() {
        return disabledKey;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatListener(),this);
        getCommand("mentions").setExecutor(new MentionsCommand());
    }



    public void mention(Player player, Player mentioned) {
        if(getConfig().getBoolean("notify-sender")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', getConfig().getString("message-sender").replace("{name}", mentioned.getName()))));
        }
        if(!mentioned.getPersistentDataContainer().has(disabledKey, PersistentDataType.BYTE)) {
            //mentioned.playNote(mentioned.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A));
            mentioned.playSound(mentioned.getLocation(),getConfig().getString("sound"),SoundCategory.MASTER,(float) getConfig().getDouble("volume"),(float) getConfig().getDouble("pitch"));
            mentioned.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', getConfig().getString("message").replace("{name}", player.getName()))));
        }
    }
}
