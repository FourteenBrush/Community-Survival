package com.jeff_media.customchat;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.regex.Pattern;

public class CustomChat extends JavaPlugin {

    private static CustomChat instance;
    private final NamespacedKey chatColorKey = new NamespacedKey(this,"color");
//    private static final Pattern HEX_PATTERN = Pattern.compile("^([a-fA-F0-9]{6})$");

    {
        instance = this;
    }

    public static CustomChat getInstance() {
        return instance;
    }

    public NamespacedKey getChatColorKey() {
        return chatColorKey;
    }

    public ChatColor getColor(Player player) {
        return getColor(player.getPersistentDataContainer().getOrDefault(chatColorKey, PersistentDataType.STRING, "f"));
    }

    public static boolean isValidColor(ChatColor color) {
        return color != null && color != ChatColor.MAGIC && color != ChatColor.RESET && color != ChatColor.STRIKETHROUGH && color != ChatColor.UNDERLINE && color != ChatColor.BOLD && color != ChatColor.ITALIC;
    }

    public ChatColor getColor(String input) {
        input = input.toLowerCase(Locale.ROOT);
        if(input.startsWith("#")) input = input.substring(1);
        ChatColor parsed;
        if(input.length() == 1) {
            parsed = ChatColor.getByChar(input.toCharArray()[0]);
            if(!isValidColor(parsed)) {
                return null;
            }
        } else {
            try {
                parsed = ChatColor.of("#" + input);
            } catch (IllegalArgumentException ex) {
                parsed = null;
            }
        }
        return parsed;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatListener(),this);
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
    }
}
