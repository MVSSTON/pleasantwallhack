package me.mstn.plugin.command;

import me.mstn.api.command.AbstractCommand;
import me.mstn.api.common.Cooldown;
import me.mstn.plugin.ConfigurationData;
import me.mstn.plugin.PleasantWallHack;
import me.mstn.plugin.protocol.GlowPacketManipulator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WallHackCommand extends AbstractCommand {

    public WallHackCommand() {
        super("pleasantwallhack", "pwh", "wh", "wallhack", "вх");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ConfigurationData configurationData = PleasantWallHack.getConfigurationData();

            if (player.hasPermission("pleasantwallhack.use") || player.hasPermission("*")) {
                if (configurationData.isCheckWorlds() && configurationData.getDisabledWorlds().contains(player.getWorld().getName())) {
                    player.sendMessage(
                            ChatColor.translateAlternateColorCodes('&', configurationData.getWorldDisabledMessage())
                    );

                    return;
                }

                if (!Cooldown.hasCooldown(player.getName(), "wallhack-use")) {
                    int radius = configurationData.getRadius();
                    int time = configurationData.getTime();

                    GlowPacketManipulator.glowPlayersAroundPlayer(player, radius, time, isPlayersNearby -> {
                        if (isPlayersNearby) {
                            player.sendMessage(
                                    ChatColor.translateAlternateColorCodes('&',
                                            configurationData.getGlowEnabledMessage()
                                                    .replace("{radius}", String.valueOf(radius))
                                                    .replace("{time}", String.valueOf(time))
                                    )
                            );

                            return;
                        }

                        player.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', configurationData.getNearbyPlayersEmptyMessage())
                        );
                    });

                    if (configurationData.isCooldownEnabled() && !player.hasPermission("pleasantwallhack.bypass")) Cooldown.addCooldown(player.getName(), "wallhack-use", 20L * configurationData.getCooldownTime());

                    return;
                }

                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                configurationData.getCooldownMessage()
                                        .replace("{time}", String.valueOf(Cooldown.getSecondCooldown(player.getName(), "wallhack-use")))
                        )
                );

                return;
            }

            player.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', configurationData.getPermissionRequiredMessage())
            );

            return;
        }

        sender.sendMessage(ChatColor.RED + "Command only for players!");
    }

}
