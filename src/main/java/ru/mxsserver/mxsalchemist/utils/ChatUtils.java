package ru.mxsserver.mxsalchemist.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
    public ChatUtils() {
    }

    public static String color(String from) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        for(Matcher matcher = pattern.matcher(from); matcher.find(); matcher = pattern.matcher(from)) {
            String hexCode = from.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();

            for(char c : ch) {
                builder.append("&").append(c);
            }

            from = from.replace(hexCode, builder.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', from);
    }

    public static void sendMsg(Player p, String msg) {
        if (msg != null) {
            p.sendMessage(color(msg));
        }
    }
}
