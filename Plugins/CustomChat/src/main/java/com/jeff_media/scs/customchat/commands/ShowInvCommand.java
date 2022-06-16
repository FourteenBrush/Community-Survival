package com.jeff_media.scs.customchat.commands;

import com.jeff_media.scs.customchat.CustomChat;
import de.jeff_media.jefflib.TextUtils;
import de.jeff_media.jefflib.Ticks;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShowInvCommand implements CommandExecutor, Listener {

    private static final CustomChat main = CustomChat.getInstance();
    private static final int HOTBAR_START = 54 - 9;
    private static final int STORAGE_START = 18;
    private final Map<UUID, Inventory> inventories = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;
        if (args.length > 0 && args[0].length() == 36) {
            UUID uuid = UUID.fromString(args[0]);
            Inventory inventory = inventories.get(uuid);
            if (inventory == null) return true;
            player.openInventory(inventory);
            return true;
        }
        UUID uuid = createInventory(player);
        broadcast(player, uuid);
        return true;
    }

    public UUID createInventory(Player player) {
        Inventory newInv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("showinv.title").replace("{name}", TextUtils.color(main.getPrefix(player, false)))));
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < 9; i++) {
            newInv.setItem(HOTBAR_START + i, cloneItem(inv.getItem(i)));
        }
        for (int i = 9; i < 9 + (3 * 9); i++) {
            newInv.setItem(STORAGE_START + i - 9, cloneItem(inv.getItem(i)));
        }
        newInv.setItem(1, cloneItem(inv.getHelmet()));
        newInv.setItem(2, cloneItem(inv.getChestplate()));
        newInv.setItem(3, cloneItem(inv.getLeggings()));
        newInv.setItem(4, cloneItem(inv.getBoots()));

        newInv.setItem(7, cloneItem(inv.getItemInOffHand()));
        UUID uuid = UUID.randomUUID();
        inventories.put(uuid, newInv);
        new RemoveTask(uuid).runTaskLater(main, Ticks.fromHours(2));
        return uuid;
    }

    private void broadcast(Player player, UUID uuid) {
        BaseComponent[] components = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("showinv.message")).replace("{name}", CustomChat.toLegacyHex(main.getPrefix(player, true))));
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/showinv " + uuid.toString());
        for (int i = 0; i < components.length; i++) {
            components[i].setClickEvent(clickEvent);
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.spigot().sendMessage(components);
        }
    }

    public static ItemStack cloneItem(ItemStack itemStack) {
        if (itemStack == null) return null;
        return itemStack.clone();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (isPreview(event.getView())) event.setCancelled(true);
    }

    private boolean isPreview(InventoryView view) {
        return inventories.containsValue(view.getTopInventory());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (isPreview(event.getView())) event.setCancelled(true);
    }

    private class RemoveTask extends BukkitRunnable {
        private final UUID uuid;

        private RemoveTask(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTopInventory() == inventories.get(uuid)) {
                    player.closeInventory();
                }
            }
            inventories.remove(uuid);
        }
    }
}
