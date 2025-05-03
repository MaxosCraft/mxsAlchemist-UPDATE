package ru.mxsserver.mxsalchemist.api.events;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SuccessPotionMixEvent extends Event {
    private final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack potion;
    private final List<PotionEffect> effects;

    public SuccessPotionMixEvent(Player p, ItemStack s, List<PotionEffect> e) {
        this.player = p;
        this.potion = s;
        this.effects = e;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getPotion() {
        return this.potion;
    }

    public List<PotionEffect> getEffects() {
        return this.effects;
    }

    public HandlerList getHandlers() {
        return this.handlers;
    }
}
