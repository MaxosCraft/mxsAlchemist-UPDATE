package ru.mxsserver.mxsalchemist.utils;

import org.bukkit.entity.Player;
import ru.mxsserver.mxsalchemist.api.events.AddChanceEvent;
import ru.mxsserver.mxsalchemist.configurations.Menu;

public class StorageUtils {
    public StorageUtils() {
    }

    public static void addChance(Player p) {
        int percent = Menu.getChanceGiverChance();
        int chance = (Integer)Storage.chance.get(p.getUniqueId());
        int sum = percent + chance;
        if (sum > Menu.getMaxChance()) {
            if (Menu.getMaxChance() != chance) {
                Storage.chance.put(p.getUniqueId(), Menu.getMaxChance());
                chance = (Integer)Storage.chance.get(p.getUniqueId());
                new AddChanceEvent(p, chance);
            }
        } else {
            Storage.chance.put(p.getUniqueId(), sum);
            new AddChanceEvent(p, chance);
        }
    }

    public static void addPrice(Player p) {
        int giverPrice = (Integer)Storage.price.get(p.getUniqueId());
        int multiplier = Menu.getChanceGiverMultiplier();
        int sum = giverPrice * multiplier;
        if (sum > Menu.getMaxPrice()) {
            if (giverPrice != Menu.getMaxPrice()) {
                Storage.price.put(p.getUniqueId(), Menu.getMaxPrice());
            }
        } else {
            Storage.price.put(p.getUniqueId(), sum);
        }
    }
}
