package ru.mxsserver.mxsalchemist.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.mxsserver.mxsalchemist.configurations.Menu;
import ru.mxsserver.mxsalchemist.utils.Utils;

public class Alchemist {
    private static Inventory inv;

    public Alchemist() {
    }

    public static void open(Player p) {
        inv = Bukkit.createInventory(p, Menu.getSize(), "§8" + Menu.getTitle());
        Utils.fillInventory(p, inv);
        p.openInventory(inv);
    }

    public static void update(Player p, Inventory inv) {
        Utils.fillInventory(p, inv);
        p.updateInventory();
    }
}
