package dev.l4oyst0rm.AngleGuardAPI.api.command;

import org.bukkit.command.CommandExecutor;

import java.util.Collection;

public interface CommandAPI {
    boolean registerCommand(String commandName, CommandExecutor executor);
    boolean unregisterCommand(String commandName);

    /**
     * @return Collection of all registered command names
     */
    Collection<String> getRegisteredCommands();
}