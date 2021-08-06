package me.mstn.api.command;

import me.mstn.api.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.List;

public final class CommandRegister extends Command implements PluginIdentifiableCommand {

    private final Plugin plugin;
    private final CommandExecutor executor;
    private static CommandMap commandMap;

    public CommandRegister(List<String> aliases, String desc, String usage, CommandExecutor owner, Plugin plugin) {
        super(aliases.get(0), desc, usage, aliases);
        this.executor = owner;
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        return executor.onCommand(sender, this, label, args);
    }

    public static void registerCommandExecutor(Plugin plugin, CommandExecutor executor, List<String> aliases, String desc, String usage) {
        try {
            CommandRegister reg = new CommandRegister(aliases, desc, usage, executor, plugin);

            if (commandMap == null) {
                Field commandMapField = ReflectionUtil.getField(
                        Bukkit.getServer().getClass(), true, "commandMap"
                );

                commandMapField.setAccessible(true);
                commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            }

            commandMap.register(plugin.getDescription().getName(), reg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}