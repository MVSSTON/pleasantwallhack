package me.mstn.api.command;

import java.util.Collection;

public interface ICommandManager {

    void registerCommand(AbstractCommand... command);

    Collection<AbstractCommand> getRegisteredCommands();

}
