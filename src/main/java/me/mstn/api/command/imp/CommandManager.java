package me.mstn.api.command.imp;

import me.mstn.api.command.AbstractCommand;
import me.mstn.api.command.CommandRegister;
import me.mstn.api.command.ICommandManager;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CommandManager implements ICommandManager {

    private final Plugin plugin;
    private final Collection<AbstractCommand> commands = new ArrayList<>();

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private void registerCommand(AbstractCommand command) {
        commands.add(command);

        ArrayList<String> commandAliases = new ArrayList<>();
        Collections.addAll(commandAliases, command.getAliases());
        commandAliases.add(command.getName());

        CommandRegister.registerCommandExecutor(plugin, command.getCommandExecutor(), commandAliases, "Plugin command", command.getName());
    }

    @Override
    public void registerCommand(AbstractCommand... command) {
        for (AbstractCommand abstractCommand : command) {
            registerCommand(abstractCommand);
        }
    }

    @Override
    public Collection<AbstractCommand> getRegisteredCommands() {
        return commands;
    }

}