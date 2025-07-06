package dev.l4oyst0rm.AngleGuardAPI.api;

public interface Addon {
    void onEnable(AngleGuardAPI api); // get API injected
    void onDisable();
}
