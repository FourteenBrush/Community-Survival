package com.jeff_media.customchat;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CustomChat extends JavaPlugin {

    private static CustomChat instance;
    private final NamespacedKey chatColor1Key = new NamespacedKey(this,"hex1");
    private final NamespacedKey chatColor2Key = new NamespacedKey(this,"hex2");
    private BukkitAudiences adventure;
    private final Map<UUID,String> prefixes = new HashMap<>();

    {
        instance = this;
    }

    public static CustomChat getInstance() {
        return instance;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public static String parseMiniMessage(String input) {
        return BukkitComponentSerializer.legacy().serialize(MiniMessage.miniMessage().deserialize(input));
    }

    public static String getRgbHex(Color color) {
        String r = String.format("%02X",color.getRed());
        String g = String.format("%02X",color.getGreen());
        String b = String.format("%02X",color.getBlue());
        return "#" + r + g + b;
    }

    public String getPrefix(Player player) {
        return prefixes.computeIfAbsent(player.getUniqueId(), __ -> {
            String color1 = player.getPersistentDataContainer().getOrDefault(chatColor1Key, PersistentDataType.STRING,"#ffffff");
            String color2 = player.getPersistentDataContainer().getOrDefault(chatColor2Key, PersistentDataType.STRING,color1);
            Component component = MiniMessage.miniMessage().deserialize("<gradient:" + color1 + ":" + color2 + ">" + player.getName() + "</gradient>");
            return LegacyComponentSerializer.builder().hexColors().build().serialize(component);
            //return LegacyComponentSerializer.legacySection().serialize(component);
        });
    }

    public NamespacedKey getChatColor1Key() {
        return chatColor1Key;
    }

    public NamespacedKey getChatColor2Key() {
        return chatColor2Key;
    }

    public ChatColor getColor(Player player) {
        return getColor(player.getPersistentDataContainer().getOrDefault(chatColor1Key, PersistentDataType.STRING, "f"));
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
        adventure = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {
        adventure.close();
    }

    public Map<UUID, String> getPrefixes() {
        return prefixes;
    }
}
