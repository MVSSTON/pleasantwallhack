package me.mstn.plugin.utilities;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationUtility {

    public static final Pattern HEX_PATTERN = Pattern.compile("&#(([A-Fa-f0-9]){6})");

    public static List<String> getParsedLines(Player player, ConfigurationSection section, String key,
                                              boolean replacePlaceholders) {
        if (!section.contains(key)) {
            return Collections.emptyList();
        }

        List<String> temp = section.getStringList(key);
        List<String> lines = new ArrayList<>();

        if (temp.isEmpty()) {
            return Collections.emptyList();
        }

        for (String line : temp) {
            if (replacePlaceholders) {
                line = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, line);
            }

            lines.add(applyColor(line));
        }

        temp.clear();

        return lines;
    }

    public static int getIntOrDefault(ConfigurationSection section, String key, int defaultValue) {
        if (!section.contains(key)) {
            return defaultValue;
        }

        return section.getInt(key);
    }

    public static boolean getBooleanOrDefault(ConfigurationSection section, String key, boolean defaultValue) {
        if (!section.contains(key)) {
            return defaultValue;
        }

        return section.getBoolean(key);
    }

    public static List<String> getStringListOrEmpty(ConfigurationSection section, String key) {
        if (!section.contains(key)) {
            return Collections.emptyList();
        }

        return section.getStringList(key);
    }

    public static String applyColor(String string) {
        if (BukkitVersion.getCurrent().getId() >= BukkitVersion.v1_16_R1.getId()) {
            Matcher matcher = HEX_PATTERN.matcher(string);

            while (matcher.find()) {
                ChatColor hexColor = ChatColor.of("#" + matcher.group(1));
                String before = string.substring(0, matcher.start());
                String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
