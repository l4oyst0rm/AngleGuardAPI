package dev.l4oyst0rm.AngleGuardAPI.api.handlers;

import org.bukkit.entity.Player;

public interface PunishmentHandler {
    void execute(Player player, String punishmentId);
}
