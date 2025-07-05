package dev.l4oyst0rm.angleGuard;

import com.tcoded.folialib.FoliaLib;
import dev.l4oyst0rm.angleGuard.addons.AddonLoader;
import dev.l4oyst0rm.angleGuard.api.AngleGuardAPI;
import dev.l4oyst0rm.angleGuard.api.manager.PunishmentHandler;
import dev.l4oyst0rm.angleGuard.check.Check;
import dev.l4oyst0rm.angleGuard.check.CheckManager;
import dev.l4oyst0rm.angleGuard.commands.MainCommand;
import dev.l4oyst0rm.angleGuard.commands.TabCompleter;
import dev.l4oyst0rm.angleGuard.listeners.MenuListener;
import dev.l4oyst0rm.angleGuard.manager.AlertsManager;
import dev.l4oyst0rm.angleGuard.manager.PunishmentManager;
import dev.l4oyst0rm.angleGuard.manager.WebhookManager;
import dev.l4oyst0rm.angleGuard.utils.LicenseUtil;
import dev.l4oyst0rm.angleGuard.utils.TpsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AngleGuard extends JavaPlugin implements AngleGuardAPI {
    private static AngleGuard instance;
    private PunishmentManager punishmentManager;
    private AlertsManager alertsManager;
    private WebhookManager webhookManager;
    private AddonLoader addonLoader;
    private FoliaLib foliaLib;

    private final Map<String, Check> checks = new HashMap<>();

    @Override
    public void onEnable() {
        VersionChecker.getInstance(this);
        instance = this;
        saveDefaultConfig();
        alertsManager = new AlertsManager(this);
        punishmentManager = new PunishmentManager(this, alertsManager);
        webhookManager = new WebhookManager(this);
        addonLoader = new AddonLoader(new File(getDataFolder(), "addons"), punishmentManager);
        foliaLib = new FoliaLib(this);

        try {
            if (!LicenseUtil.checkLicense(getConfig().getString("license-key"))) {
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } catch (IllegalStateException e) {
            getLogger().severe(e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        addonLoader.loadAddons();

        if (foliaLib.isFolia()) {
            foliaLib.getScheduler().runTimer(() -> {
                TpsUtil.tick();
            }, 0L, 1L);
        } else {
            Bukkit.getScheduler().runTaskTimer(this, () -> {
                TpsUtil.tick();
            }, 0L, 1L);
        }

        CheckManager.registerChecks(this, alertsManager, punishmentManager);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getCommand("angleguard").setExecutor(new MainCommand(this));
        getCommand("angleguard").setTabCompleter(new TabCompleter(this));
        getLogger().info("AngleGuard v1.4.0-alpha enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AngleGuard v1.4.0-alpha disabled!");
    }

    public static AngleGuard getInstance() {
        return instance;
    }

    public FoliaLib getFoliaLib() {
        return foliaLib;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    @Override
    public void addPunishmentType(String punishmentId, PunishmentHandler handler) {
        punishmentManager.addPunishmentType(punishmentId, handler);
    }

    @Override
    public void executePunishment(Player player, String punishmentId) {
        punishmentManager.executePunishment(player, punishmentId);
    }

    @Override
    public Check createCheck(String name, String configPath, AlertsManager alertsManager, PunishmentManager punishmentManager) {
        return new Check(name, configPath, alertsManager, punishmentManager);
    }

    @Override
    public void registerCheck(Check check) {
        checks.put(check.getName().toLowerCase(), check);
        getServer().getPluginManager().registerEvents(check, this);
    }

    @Override
    public int getViolation(Player player, String checkId) {
        return 0;
    }

    public Check getCheck(String checkName) {
        return checks.get(checkName.toLowerCase());
    }

    public WebhookManager getWebhookManager() {
        return webhookManager;
    }

    public void reloadAddons() {
        addonLoader.unloadAddons();
        addonLoader.loadAddons();
    }
}