package com.jeff_media.scs.customchat;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ChatColorDataType implements PersistentDataType<Integer, ChatColor> {

    public static final ChatColorDataType INSTANCE = new ChatColorDataType();

    @NotNull
    @Override
    public Class<Integer> getPrimitiveType() {
        return Integer.class;
    }

    @NotNull
    @Override
    public Class<ChatColor> getComplexType() {
        return ChatColor.class;
    }

    @NotNull
    @Override
    public Integer toPrimitive(@NotNull ChatColor color, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return color.getColor().getRGB();
    }

    @NotNull
    @Override
    public ChatColor fromPrimitive(@NotNull Integer rgb, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return ChatColor.of(new Color(rgb));
    }
}
