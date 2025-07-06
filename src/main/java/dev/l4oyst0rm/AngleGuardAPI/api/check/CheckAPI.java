package dev.l4oyst0rm.AngleGuardAPI.api.check;

import org.bukkit.entity.Player;

public interface CheckAPI {
    int getViolationLevel(Player player);
    String getName();
}
