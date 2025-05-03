package ru.mxsserver.mxsalchemist.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AddChanceEvent extends Event {
    private final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int chance;

    public AddChanceEvent(Player p, int q) {
        this.player = p;
        this.chance = q;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getChance() {
        return this.chance;
    }

    public HandlerList getHandlers() {
        return this.handlers;
    }
}