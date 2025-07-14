package dev.l4oyst0rm.AngleGuardAPI.api;

import dev.l4oyst0rm.AngleGuardAPI.api.check.CheckAPI;
import dev.l4oyst0rm.AngleGuardAPI.api.command.CommandAPI;
import dev.l4oyst0rm.AngleGuardAPI.api.handlers.PunishmentHandler;
import org.bukkit.entity.Player;

public interface AngleGuardAPI {
    void registerCheck(CheckAPI check);
    CheckAPI createCheck(String name, String configPath);
    void addPunishmentType(String typeId, PunishmentHandler handler);
    void executePunishment(Player player, String punishmentId);
    int getViolation(Player player, String checkName);
    void logger(String string);
    // Command API
    CommandAPI getCommandAPI();
}
