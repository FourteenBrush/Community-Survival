package com.jeff_media.scs.customchat;

import com.jeff_media.scs.customchat.commands.*;
import de.jeff_media.jefflib.TextUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class CustomChat extends JavaPlugin {

    private static final Pattern NEW_HEX_PATTERN = Pattern.compile("#[0-9A-Za-z]{6}");
    private static CustomChat instance;
    private final NamespacedKey chatColor1Key = new NamespacedKey(this, "hex1");
    private final NamespacedKey chatColor2Key = new NamespacedKey(this, "hex2");
    private final Map<UUID, String> prefixes = new HashMap<>();
    private final NamespacedKey disabledKey = new NamespacedKey(this, "mentions-disabled");
    private BukkitAudiences adventure;

    {
        instance = this;
    }

    public static CustomChat getInstance() {
        return instance;
    }

    public static String parseMiniMessage(String input) {
        return BukkitComponentSerializer.legacy().serialize(MiniMessage.miniMessage().deserialize(input));
    }

    public static String getRgbHex(Color color) {
        String r = String.format("%02X", color.getRed());
        String g = String.format("%02X", color.getGreen());
        String b = String.format("%02X", color.getBlue());
        return "#" + r + g + b;
    }

    public static String toLegacyHex(String string) {
        //Matcher matcher = NEW_HEX_PATTERN.matcher(string);
        return TextUtils.color(string.replace('§', '&'));
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public NamespacedKey getDisabledKey() {
        return disabledKey;
    }

    public String getPrefix(Player player, boolean italic) {
        String color1 = player.getPersistentDataContainer().getOrDefault(chatColor1Key, PersistentDataType.STRING, "#ffffff");
        String color2 = player.getPersistentDataContainer().getOrDefault(chatColor2Key, PersistentDataType.STRING, color1);
        Component component = MiniMessage.miniMessage().deserialize("<gradient:" + color1 + ":" + color2 + ">" + (italic ? "<italic>" : "") + player.getName() + "</gradient>");
        return LegacyComponentSerializer.builder().hexColors().build().serialize(component);
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

    public ChatColor getColor(String input) {
        input = input.toLowerCase(Locale.ROOT);
        if (input.startsWith("#")) input = input.substring(1);
        ChatColor parsed;
        if (input.length() == 1) {
            parsed = ChatColor.getByChar(input.toCharArray()[0]);
            if (!isValidColor(parsed)) {
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

    public static boolean isValidColor(ChatColor color) {
        return color != null && color != ChatColor.MAGIC && color != ChatColor.RESET && color != ChatColor.STRIKETHROUGH && color != ChatColor.UNDERLINE && color != ChatColor.BOLD && color != ChatColor.ITALIC;
    }

    @Override
    public void onDisable() {
        adventure.close();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
        getCommand("mentions").setExecutor(new MentionsCommand());
        getCommand("showitem").setExecutor(new ShowItemCommand());
        ShowInvCommand showInvCommand = new ShowInvCommand();
        getCommand("showinv").setExecutor(showInvCommand);
        getCommand("me").setExecutor(new MeCommand());
        getServer().getPluginManager().registerEvents(showInvCommand, this);
        adventure = BukkitAudiences.create(this);
    }

    public Map<UUID, String> getPrefixes() {
        return prefixes;
    }

    public void mention(Player player, Player mentioned) {
        if (getConfig().getBoolean("notify-sender")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(org.bukkit.ChatColor.translateAlternateColorCodes('&', getConfig().getString("message-sender").replace("{name}", mentioned.getName()))));
        }
        if (!mentioned.getPersistentDataContainer().has(disabledKey, PersistentDataType.BYTE)) {
            mentioned.playSound(mentioned.getLocation(), getConfig().getString("sound"), SoundCategory.MASTER, (float) getConfig().getDouble("volume"), (float) getConfig().getDouble("pitch"));
            mentioned.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(org.bukkit.ChatColor.translateAlternateColorCodes('&', getConfig().getString("message").replace("{name}", player.getName()))));
        }
    }
}
