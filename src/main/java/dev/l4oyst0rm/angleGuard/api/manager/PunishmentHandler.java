package dev.l4oyst0rm.angleGuard.api.manager;

import org.bukkit.entity.Player;

public interface PunishmentHandler {
    void execute(Player player, String punishmentId);
}
