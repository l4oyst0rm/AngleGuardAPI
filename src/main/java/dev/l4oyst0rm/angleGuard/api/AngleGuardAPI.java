package dev.l4oyst0rm.angleGuard.api;

import dev.l4oyst0rm.angleGuard.api.manager.PunishmentHandler;
import dev.l4oyst0rm.angleGuard.check.Check;
import dev.l4oyst0rm.angleGuard.manager.AlertsManager;
import dev.l4oyst0rm.angleGuard.manager.PunishmentManager;
import org.bukkit.entity.Player;

public interface AngleGuardAPI {
    void addPunishmentType(String punishmentId, PunishmentHandler handler);
    void executePunishment(Player player, String punishmentId);
    Check createCheck(String name, String configPath, AlertsManager alertsManager, PunishmentManager punishmentManager);
    void registerCheck(Check check);
    int getViolation(Player player, String checkId);
}