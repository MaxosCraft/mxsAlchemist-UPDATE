package ru.mxsserver.mxsalchemist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.mxsserver.mxsalchemist.commands.OpenCommand;
import ru.mxsserver.mxsalchemist.configurations.Config;
import ru.mxsserver.mxsalchemist.configurations.Menu;
import ru.mxsserver.mxsalchemist.listeners.AlchemistClickListener;
import ru.mxsserver.mxsalchemist.listeners.AnimationClickListener;
import ru.mxsserver.mxsalchemist.listeners.CloseInventoryListener;
import ru.mxsserver.mxsalchemist.utils.ChatUtils;
import ru.mxsserver.mxsalchemist.utils.Eco;
import ru.mxsserver.mxsalchemist.utils.Metrics;

public final class mxsAlchemist extends JavaPlugin {
    private void msg(String msg) {
        String prefix = ChatUtils.color("&amxsAlchemist &7| &f");
        Bukkit.getConsoleSender().sendMessage(ChatUtils.color(prefix + msg));
    }

    @Override
    public void onEnable() {
        Eco.init();
        Bukkit.getPluginManager().registerEvents(new AlchemistClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new CloseInventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new AnimationClickListener(), this);
        getCommand("mxsalchemist").setExecutor(new OpenCommand());
        Config.loadYaml(this);
        Menu.loadYaml(this);
        if (Config.getConfig().getBoolean("enable-metrics")) {
            new Metrics(this, 17498);
        }

        Bukkit.getConsoleSender().sendMessage("");
        msg("&aПлагин запущен. &fВерсия: &dv" + getDescription().getVersion());
        msg("Разработчик: &bhttps://mxsserver.ru");
        Bukkit.getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("");
        msg("&cПлагин выключен. &fВерсия: &dv" + getDescription().getVersion());
        msg("Разработчик: &bhttps://mxsserver.ru");
        Bukkit.getConsoleSender().sendMessage("");
    }
}
