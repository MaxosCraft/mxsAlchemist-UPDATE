package ru.mxsserver.mxsalchemist.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.mxsserver.mxsalchemist.menus.Animation;

public class AnimationClickListener implements Listener {
    public AnimationClickListener() {
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("§8Идёт создание зелья...")) {
            if (Animation.itemStackMap.containsKey(e.getWhoClicked().getUniqueId())) {
                e.setCancelled(true);
                return;
            }

            if (e.getClickedInventory() != null && e.getClickedInventory() == e.getWhoClicked().getInventory()) {
                return;
            }

            if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getType() != Material.BARRIER) {
                if (e.getSlot() != 13) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }
}
