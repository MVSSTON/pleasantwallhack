package me.mstn.plugin;

import me.mstn.plugin.command.WallHackCommand;
import me.mstn.plugin.utilities.BukkitVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PleasantWallHack extends JavaPlugin {

    private static PleasantWallHack instance;

    private boolean placeholderApiSupported;

    public void onEnable() {
        instance = this;

        int currentVersion = BukkitVersion.getCurrent().getId();
        int minimalVersion = BukkitVersion.v1_9_R1.getId();

        if (currentVersion < minimalVersion) {
            getLogger().severe("!!! Plugin does not work on versions below 1.9 !!!");
            Bukkit.getPluginManager().disablePlugin(this);

            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderApiSupported = true;
        }

        saveDefaultConfig();

        getCommand("pleasantwallhack").setExecutor(new WallHackCommand(this));
    }

    public void onDisable() {

    }

    public boolean isPlaceholderApiSupported() {
        return placeholderApiSupported;
    }

    public static PleasantWallHack getInstance() {
        return instance;
    }

}
