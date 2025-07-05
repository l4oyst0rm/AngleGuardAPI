package dev.l4oyst0rm.angleGuard.check;

import dev.l4oyst0rm.angleGuard.AngleGuard;
import dev.l4oyst0rm.angleGuard.check.impl.anchoraura.AnchorAuraA;
import dev.l4oyst0rm.angleGuard.check.impl.anchoraura.AnchorAuraB;
import dev.l4oyst0rm.angleGuard.check.impl.autototem.AutoTotemA;
import dev.l4oyst0rm.angleGuard.check.impl.autototem.AutoTotemB;
import dev.l4oyst0rm.angleGuard.check.impl.crystalaura.CrystalAuraA;
import dev.l4oyst0rm.angleGuard.check.impl.crystalaura.CrystalAuraB;
import dev.l4oyst0rm.angleGuard.check.impl.elytraswap.ElytraSwapA;
import dev.l4oyst0rm.angleGuard.check.impl.packets.MaceDelay;
import dev.l4oyst0rm.angleGuard.check.impl.packets.MaceSwap;
import dev.l4oyst0rm.angleGuard.check.impl.triggerbot.TriggerBotA;
import dev.l4oyst0rm.angleGuard.manager.AlertsManager;
import dev.l4oyst0rm.angleGuard.manager.PunishmentManager;
import org.bukkit.plugin.PluginManager;

public class CheckManager {
    public static void registerChecks(AngleGuard plugin, AlertsManager alertsManager, PunishmentManager punishmentManager) {
        PluginManager pm = plugin.getServer().getPluginManager();
        registerCheck(plugin, new MaceSwap(alertsManager, punishmentManager));
        registerCheck(plugin, new MaceDelay(alertsManager, punishmentManager));
        registerCheck(plugin, new ElytraSwapA(alertsManager, punishmentManager));
        registerCheck(plugin, new AnchorAuraA(alertsManager, punishmentManager));
        registerCheck(plugin, new AnchorAuraB(alertsManager, punishmentManager));
        registerCheck(plugin, new CrystalAuraA(alertsManager, punishmentManager));
        registerCheck(plugin, new CrystalAuraB(alertsManager, punishmentManager));
        registerCheck(plugin, new TriggerBotA(alertsManager, punishmentManager));
        registerCheck(plugin, new AutoTotemA(alertsManager, punishmentManager));
        registerCheck(plugin, new AutoTotemB(alertsManager, punishmentManager));
        plugin.getLogger().info("Registered all built-in check listeners");
    }

    private static void registerCheck(AngleGuard plugin, Check check) {
        plugin.registerCheck(check);
    }
}