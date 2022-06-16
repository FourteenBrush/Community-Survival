package com.jeff_media.scs.customchat.commands;

import com.jeff_media.scs.customchat.CustomChat;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MeCommand implements CommandExecutor {

    private static final CustomChat main = CustomChat.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String msg = String.join(" ", args);
        broadcast((Player) sender, msg);
        return true;
    }

    private void broadcast(Player player, String message) {
        BaseComponent[] components = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("me.message")).replace("{name}", CustomChat.toLegacyHex(main.getPrefix(player, true))).replace("{message}", message));
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.spigot().sendMessage(components);
        }
    }
}
