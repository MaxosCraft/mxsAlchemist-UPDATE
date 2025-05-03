package ru.mxsserver.mxsalchemist.menus;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Animation {
    private final Player player;
    private final ItemStack itemStack;
    public static HashMap<UUID, ItemStack> itemStackMap = new HashMap();

    public Animation(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public void run() {
        Inventory inv = Bukkit.createInventory(this.player, 27, "§8Идёт создание зелья...");
        this.player.openInventory(inv);
        (new Thread(() -> {
            try {
                itemStackMap.put(this.player.getUniqueId(), this.itemStack);
                int a = 0;
                int b = 26;
                ItemStack divider = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
                ItemMeta meta = divider.getItemMeta();
                meta.setDisplayName("§7");
                divider.setItemMeta(meta);

                for(int i = 0; i < 13 && this.player.getOpenInventory().getTitle().equals("§8Идёт создание зелья..."); ++i) {
                    inv.setItem(a, divider);
                    inv.setItem(b, divider);
                    ++a;
                    --b;
                    this.player.playSound(this.player.getLocation(), Sound.BLOCK_BONE_BLOCK_PLACE, 1.0F, 1.0F);
                    Thread.sleep(350L);
                }

                itemStackMap.remove(this.player.getUniqueId());
                if (this.player.getOpenInventory().getTitle().equals("§8Идёт создание зелья...")) {
                    inv.setItem(13, this.itemStack);
                    this.player.playSound(this.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                }
            } catch (Exception var7) {
            }
        })).start();
    }
}