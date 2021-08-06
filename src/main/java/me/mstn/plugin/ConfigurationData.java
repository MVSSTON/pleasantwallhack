package me.mstn.plugin;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;

public class ConfigurationData {

    /*
    * Settings section
    * */
    @Getter private final int radius;
    @Getter private final int time;
    @Getter private final boolean checkWorlds;
    @Getter private final int cooldownTime;
    @Getter private final boolean cooldownEnabled;

    /*
    * Messages section
    * */
    @Getter private final String permissionRequiredMessage;
    @Getter private final String worldDisabledMessage;
    @Getter private final String glowEnabledMessage;
    @Getter private final String nearbyPlayersEmptyMessage;
    @Getter private final String cooldownMessage;

    /*
    * Disabled worlds list
    * */
    @Getter private final Collection<String> disabledWorlds;

    public ConfigurationData(FileConfiguration configuration) {
        ConfigurationSection settingsSection = configuration.getConfigurationSection("settings");
        ConfigurationSection messagesSections = configuration.getConfigurationSection("messages");

        disabledWorlds = configuration.getStringList("disabled-worlds");

        radius = settingsSection.getInt("radius");
        time = settingsSection.getInt("time");
        checkWorlds = settingsSection.getBoolean("check-disabled-worlds");
        cooldownTime = settingsSection.getInt("cooldown-time");
        cooldownEnabled = settingsSection.getBoolean("cooldown-enabled");

        permissionRequiredMessage = messagesSections.getString("permission-required");
        worldDisabledMessage = messagesSections.getString("world-disabled");
        glowEnabledMessage = messagesSections.getString("glow-enabled");
        nearbyPlayersEmptyMessage = messagesSections.getString("nearby-players-empty");
        cooldownMessage = messagesSections.getString("cooldown");
    }

}
