package ru.mxsserver.mxsalchemist.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import ru.mxsserver.mxsalchemist.configurations.Menu;
import ru.mxsserver.mxsalchemist.menus.Animation;

public class CloseInventoryListener implements Listener {
    public CloseInventoryListener() {
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("§8" + Menu.getTitle())) {
            if (e.getInventory().getItem(Menu.getFirstPotionSlot()) != null) {
                e.getPlayer().getInventory().addItem(new ItemStack[]{e.getInventory().getItem(Menu.getFirstPotionSlot())});
            }

            if (e.getInventory().getItem(Menu.getSecondPotionSlot()) != null) {
                e.getPlayer().getInventory().addItem(new ItemStack[]{e.getInventory().getItem(Menu.getSecondPotionSlot())});
            }
        } else if (e.getView().getTitle().equals("§8Идёт создание зелья...")) {
            Player player = (Player)e.getPlayer();
            if (e.getInventory().getItem(13) != null) {
                if (e.getInventory().getItem(13).getType() != Material.BARRIER) {
                    player.getInventory().addItem(new ItemStack[]{e.getInventory().getItem(13)});
                }
            } else if (Animation.itemStackMap.containsKey(player.getUniqueId())) {
                if (((ItemStack)Animation.itemStackMap.get(player.getUniqueId())).getType() != Material.BARRIER) {
                    player.getInventory().addItem(new ItemStack[]{(ItemStack)Animation.itemStackMap.get(player.getUniqueId())});
                }

                Animation.itemStackMap.remove(player.getUniqueId());
            }
        }
    }
}
