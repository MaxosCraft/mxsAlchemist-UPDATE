package ru.mxsserver.mxsalchemist.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UnsuccessfulPotionMixEvent extends Event {
    private final HandlerList handlers = new HandlerList();
    private final Player player;

    public UnsuccessfulPotionMixEvent(Player p) {
        this.player = p;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return this.handlers;
    }
}
