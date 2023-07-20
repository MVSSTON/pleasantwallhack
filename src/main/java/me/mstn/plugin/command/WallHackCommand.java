package me.mstn.plugin.command;

import me.mstn.plugin.PleasantWallHack;
import me.mstn.plugin.protocol.GlowHandler;
import me.mstn.plugin.utilities.ConfigurationUtility;
import me.mstn.plugin.utilities.CoolDown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WallHackCommand implements CommandExecutor {

    private final ConfigurationSection messagesSection;
    private final ConfigurationSection settingsSection;

    private final List<String> blockedWorlds;

    public WallHackCommand(Plugin plugin) {
        Configuration configuration = plugin.getConfig();

        messagesSection = configuration.getConfigurationSection("Messages");
        settingsSection = configuration.getConfigurationSection("Settings");

        blockedWorlds = ConfigurationUtility.getStringListOrEmpty(configuration, "Blocked Worlds");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("pleasantwallhack.use")) {
            sendConfigurationMessage(player, "Permission Required");

            return false;
        }

        if (ConfigurationUtility.getBooleanOrDefault(settingsSection, "Check Blocked Worlds", false)) {
            if (blockedWorlds.contains(player.getWorld().getName())) {
                sendConfigurationMessage(player, "World Blocked");

                return false;
            }
        }

        if (CoolDown.hasCoolDown(player.getName())) {
            sendConfigurationMessage(player, "Cooldown");

            return false;
        }

        int radius = ConfigurationUtility.getIntOrDefault(settingsSection, "Radius", 10);
        int time = ConfigurationUtility.getIntOrDefault(settingsSection, "Time", 10);

        boolean isGlowed = GlowHandler.glowPlayers(player, radius, time);

        if (!isGlowed) {
            sendConfigurationMessage(player, "Empty");

            return false;
        }

        for (String line : ConfigurationUtility.getParsedLines(player, messagesSection, "Glowed",
                PleasantWallHack.getInstance().isPlaceholderApiSupported())) {
            player.sendMessage(
                    line.replace("%radius", String.valueOf(radius))
                            .replace("%time", String.valueOf(time))
            );
        }

        if (ConfigurationUtility.getBooleanOrDefault(settingsSection, "CoolDown.Enabled", true)) {
            if (!player.hasPermission("pleasantwallhack.bypass")) {
                CoolDown.addCoolDown(player.getName(), 20L * ConfigurationUtility.getIntOrDefault(settingsSection,
                        "CoolDown.Time", 30));
            }
        }

        return false;
    }

    private void sendConfigurationMessage(Player player, String key) {
        for (String line : ConfigurationUtility.getParsedLines(player, messagesSection, key,
                PleasantWallHack.getInstance().isPlaceholderApiSupported())) {
            player.sendMessage(line);
        }
    }

}
