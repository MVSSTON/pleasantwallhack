package me.mstn.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand implements CommandExecutor {

    private final String name;
    private final String[] usages;

    public AbstractCommand(String name, String... aliases) {
        this.name = name;
        this.usages = aliases;
    }

    public String[] getAliases() {
        return this.usages;
    }

    public String getName() {
        return this.name;
    }

    public CommandExecutor getCommandExecutor() {
        return this;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            this.execute(sender, args);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return false;
    }

}
