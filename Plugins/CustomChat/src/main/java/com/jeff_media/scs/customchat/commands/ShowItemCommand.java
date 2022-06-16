package com.jeff_media.scs.customchat.commands;

import com.jeff_media.scs.customchat.CustomChat;
import de.jeff_media.jefflib.MaterialUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ShowItemCommand implements CommandExecutor {

    private static final CustomChat main = CustomChat.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player)  sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(itemStack == null || itemStack.getAmount() == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("showitem.no-item")));
            return true;
        }

        String itemName = MaterialUtils.getNiceMaterialName(itemStack.getType());
        if(itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            if(meta.hasDisplayName()) {
                itemName = meta.getDisplayName();
            }
        }
        /*BaseComponent[] components = TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&',
                        main.getConfig().getString("showitem.message")).replace("{name}", ChatColor.stripColor(main.getPrefix(player))).replace("{item}", itemName));
        */
        BaseComponent[] components = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("showitem.message")).replace("{name}",CustomChat.toLegacyHex(main.getPrefix(player, true))).replace("{item}",itemName));
        BaseComponent[] hover = new BaseComponent[] {
                new TextComponent(itemStackToJson(itemStack))
        };
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hover);
        for(int i = 0; i < components.length; i++) {
            components[i].setHoverEvent(event);
        }
        //BaseComponent[] components = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"&x&f&f&0&0&0&0This is red"));
        for(Player online : Bukkit.getOnlinePlayers()) {
            online.spigot().sendMessage(components);
        }
        return true;
    }

    public static String itemStackToJson(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag compoundTag = new CompoundTag();
        nmsItemStack.save(compoundTag);
        return compoundTag.getAsString();
    }
}
