package me.mstn.plugin;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import lombok.Setter;
import me.mstn.api.MasstonAPI;
import me.mstn.api.command.ICommandManager;
import me.mstn.api.command.imp.CommandManager;
import me.mstn.api.nms.Version;
import me.mstn.plugin.command.WallHackCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PleasantWallHack extends JavaPlugin {

    @Getter @Setter private static MasstonAPI masstonAPI;
    @Getter @Setter private static PleasantWallHack instance;

    @Getter @Setter private static ConfigurationData configurationData;

    public void onLoad() {
        setMasstonAPI(new MasstonAPI());

        ICommandManager commandManager = new CommandManager(this);
        masstonAPI.setCommandManager(commandManager);
        masstonAPI.getServiceManager()
                  .add(ICommandManager.class, commandManager);
    }

    public void onEnable() {
        int currentVersion = Version.getCurrent().getId();
        int minimalVersion = Version.v1_9_R1.getId();

        if (currentVersion < minimalVersion) {
            getLogger().severe("!!! Plugin does not work on versions below 1.9 !!!");
            Bukkit.getPluginManager().disablePlugin(this);

            return;
        }

        saveDefaultConfig();

        setConfigurationData(new ConfigurationData(getConfig()));
        setInstance(this);

        masstonAPI.setProtocolManager(ProtocolLibrary.getProtocolManager());
        masstonAPI.getServiceManager()
                  .getService(ICommandManager.class)
                  .registerCommand(new WallHackCommand());
    }

    public void onDisable() {

    }

}
