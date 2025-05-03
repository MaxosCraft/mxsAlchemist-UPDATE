package ru.mxsserver.mxsalchemist.configurations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.mxsserver.mxsalchemist.utils.Utils;

public class Menu {
    private static FileConfiguration config;
    private static File file;

    public Menu() {
    }

    public static void loadYaml(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "menu.yml");
        if (!file.exists()) {
            plugin.saveResource("menu.yml", true);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static int getFirstPotionSlot() {
        return config.getInt("items.first-potion.slot");
    }

    public static int getSecondPotionSlot() {
        return config.getInt("items.second-potion.slot");
    }

    public static int getMixedPotionSlot() {
        return config.getInt("items.mixed-potion.slot");
    }

    public static List<Integer> getChanceGiverSlots() {
        return new ArrayList(config.getIntegerList("items.chance-giver.slots"));
    }

    public static List<Integer> getExitSlots() {
        return new ArrayList(config.getIntegerList("items.exit.slots"));
    }

    public static List<String> getMixedPotionLore() {
        return new ArrayList(config.getStringList("items.mixed-potion.lore"));
    }

    public static List<String> getChanceGiverLore() {
        return new ArrayList(config.getStringList("items.chance-giver.lore"));
    }

    public static String getChanceGiverName() {
        return config.getString("items.chance-giver.name");
    }

    public static String getExitName() {
        return config.getString("items.exit.name");
    }

    public static String getMixedPotionName() {
        return config.getString("items.mixed-potion.name");
    }

    public static boolean isChanceGiverEnable() {
        return config.getBoolean("items.chance-giver.enable");
    }

    public static int getChanceGiverPrice() {
        return config.getInt("items.chance-giver.price");
    }

    public static int getChanceGiverChance() {
        return config.getInt("items.chance-giver.give-chance");
    }

    public static int getChanceGiverMultiplier() {
        return config.getInt("items.chance-giver.multiplier");
    }

    public static int getMaxChance() {
        return config.getInt("max-chance");
    }

    public static int getMaxPrice() {
        return config.getInt("max-price");
    }

    public static ItemStack getChanceGiverMaterial() {
        String material = config.getString("items.chance-giver.material");
        if (material == null) {
            Bukkit.getLogger().severe("Неизвестный материал в chance-giver");
            return new ItemStack(Material.BARRIER);
        } else if (material.startsWith("head-")) {
            String[] split = material.split("-");
            return Utils.getSkull(split[1], "chance-giver");
        } else {
            return new ItemStack(Material.valueOf(material));
        }
    }

    public static ItemStack getMixedPotionMaterial() {
        String material = config.getString("items.mixed-potion.material");
        if (material == null) {
            Bukkit.getLogger().severe("Неизвестный материал в mixed-potion");
            return new ItemStack(Material.BARRIER);
        } else if (material.startsWith("head-")) {
            String[] split = material.split("-");
            return Utils.getSkull(split[1], "mixed-potion");
        } else {
            return new ItemStack(Material.valueOf(material));
        }
    }

    public static Set<String> getDividerItems() {
        return config.getConfigurationSection("divider-items").getKeys(false);
    }

    public static ItemStack getExitMaterial() {
        String material = config.getString("items.exit.material");
        if (material == null) {
            Bukkit.getLogger().severe("Неизвестный материал в exit");
            return new ItemStack(Material.BARRIER);
        } else if (material.startsWith("head-")) {
            String[] split = material.split("-");
            return Utils.getSkull(split[1], "exit");
        } else {
            return new ItemStack(Material.valueOf(material));
        }
    }

    public static String getTitle() {
        return config.getString("title");
    }

    public static int getSize() {
        return config.getInt("size") * 9;
    }

    public static int getStandardChance() {
        return config.getInt("standard-chance");
    }

    public static int getMixPrice() {
        return config.getInt("price-for-mix");
    }
}