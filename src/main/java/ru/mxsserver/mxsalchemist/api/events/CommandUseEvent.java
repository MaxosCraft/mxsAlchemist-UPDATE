package ru.mxsserver.mxsalchemist.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandUseEvent extends Event implements Cancellable {
    private final HandlerList handlers = new HandlerList();
    public boolean cancelled;
    private final Player player;

    public CommandUseEvent(Player p) {
        this.player = p;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return this.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
