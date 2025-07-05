package dev.l4oyst0rm.angleGuard.addons;

import dev.l4oyst0rm.angleGuard.api.AngleGuardAPI;

public interface Addon {
    void onEnable(AngleGuardAPI api);

    void onDisable();
}
