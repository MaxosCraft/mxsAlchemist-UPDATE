package ru.mxsserver.mxsalchemist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.mxsserver.mxsalchemist.api.events.CommandUseEvent;
import ru.mxsserver.mxsalchemist.menus.Alchemist;

public class OpenCommand implements CommandExecutor {
    public OpenCommand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !(new CommandUseEvent((Player)sender)).isCancelled()) {
            Alchemist.open((Player)sender);
        }

        return true;
    }
}
