package ru.mxsserver.mxsalchemist.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.mxsserver.mxsalchemist.configurations.Menu;

public class Utils {
    public Utils() {
    }

    public static void fillInventory(Player p, Inventory inv) {
        if (!Storage.chance.containsKey(p.getUniqueId())) {
            Storage.chance.put(p.getUniqueId(), Menu.getStandardChance());
        }

        if (!Storage.price.containsKey(p.getUniqueId())) {
            Storage.price.put(p.getUniqueId(), Menu.getChanceGiverPrice());
        }

        if (Menu.isChanceGiverEnable()) {
            ItemStack item = Menu.getChanceGiverMaterial();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList();

            for(String s : Menu.getChanceGiverLore()) {
                lore.add(ChatUtils.color(s).replace("%chance%", String.valueOf(Storage.chance.get(p.getUniqueId()))).replace("%price%", String.valueOf(Storage.price.get(p.getUniqueId()))));
            }

            meta.setLore(lore);
            meta.setDisplayName(ChatUtils.color(Menu.getChanceGiverName()).replace("%percent%", String.valueOf(Menu.getChanceGiverChance())));
            item.setItemMeta(meta);

            for(Integer i : Menu.getChanceGiverSlots()) {
                inv.setItem(i, item);
            }
        }

        ItemStack item = Menu.getMixedPotionMaterial();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList();

        for(String s : Menu.getMixedPotionLore()) {
            lore.add(ChatUtils.color(s).replace("%chance%", String.valueOf(Storage.chance.get(p.getUniqueId()))).replace("%price%", String.valueOf(Menu.getMixPrice())));
        }

        meta.setLore(lore);
        meta.setDisplayName(ChatUtils.color(Menu.getMixedPotionName()));
        item.setItemMeta(meta);
        inv.setItem(Menu.getMixedPotionSlot(), item);

        for(String path : Menu.getDividerItems()) {
            String s = "divider-items." + path + ".";
            String material = Menu.getConfig().getString(s + "material");
            if (material == null) {
                Bukkit.getConsoleSender().sendMessage("Укажите материал для " + path);
            } else {
                ItemStack divider;
                if (material.startsWith("head-")) {
                    String[] split = material.split("-");
                    divider = getSkull(split[1], path);
                } else {
                    try {
                        divider = new ItemStack(Material.valueOf(material));
                    } catch (IllegalArgumentException var14) {
                        divider = new ItemStack(Material.BARRIER);
                        Bukkit.getConsoleSender().sendMessage("Укажите верный материал для " + path);
                    }
                }

                List<String> lore1 = new ArrayList();
                if (Menu.getConfig().get(s + "lore") != null) {
                    for(String s1 : Menu.getConfig().getStringList(s + "lore")) {
                        lore1.add(ChatUtils.color(s1));
                    }
                }

                ItemMeta dividerMeta = divider.getItemMeta();
                if (Menu.getConfig().getString(s + "name") != null) {
                    dividerMeta.setDisplayName(ChatUtils.color(Menu.getConfig().getString(s + "name")));
                }

                divider.setItemMeta(dividerMeta);

                for(Integer i : Menu.getConfig().getIntegerList(s + "slots")) {
                    inv.setItem(i, divider);
                }
            }
        }

        ItemStack exit = Menu.getExitMaterial();
        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.setDisplayName(ChatUtils.color(Menu.getExitName()));
        exit.setItemMeta(exitMeta);

        for(Integer i : Menu.getExitSlots()) {
            inv.setItem(i, exit);
        }
    }

    public static ItemStack getSkull(String url, String path) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        if (Bukkit.getBukkitVersion().contains("1.12")) {
            item = new ItemStack(Material.valueOf("HEAD"));
        }

        ItemMeta meta = item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException var61) {
            Bukkit.getLogger().severe("Такой головы не существует: " + path);
        }

        item.setItemMeta(meta);
        return item;
    }
}
