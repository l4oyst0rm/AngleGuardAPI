package dev.l4oyst0rm.AngleGuardAPI.api;

import dev.l4oyst0rm.AngleGuardAPI.api.check.CheckAPI;
import dev.l4oyst0rm.AngleGuardAPI.api.command.CommandAPI;
import dev.l4oyst0rm.AngleGuardAPI.api.handlers.PunishmentHandler;
import dev.l4oyst0rm.angleGuard.check.Check;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * The main public API interface for AngleGuard AntiCheat.
 *
 * <p>This API allows other plugins to interact with AngleGuard’s detection system,
 * punishment management, and command handling. It provides easy access to
 * player violations, registered checks, and total detection flags — making it
 * ideal for integration with custom punishment systems, logging tools,
 * and developer dashboards.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * AngleGuardAPI api = getPlugin.getAPI();
 *
 * int totalFlags = api.getTotalFlags(player);
 * List<String> flags = api.getFlags(player);
 *
 * for (String flag : flags) {
 *     Bukkit.getLogger().info(flag);
 * }
 * }</pre>
 *
 * @author L4oySt0rm
 * @version 1.0
 */
public interface AngleGuardAPI {

    /**
     * Registers a new check into the AngleGuard system.
     * Typically used for dynamically loading or extending detection checks.
     * Available: [ONLY FOR ADDON]
     *
     * @param check the check instance to register
     */
    void registerCheck(Check check);

    /**
     * Creates a new check by name and configuration path.
     * This is mainly for internal or dynamic check creation.
     * Available: [ONLY FOR ADDON]
     *
     * @param name        the name of the check (e.g., "ReachA")
     * @param configPath  the configuration path for settings (e.g., "checks.reach.a.")
     * @return a new {@link Check} instance
     */
    Check createCheck(String name, String configPath);

    /**
     * Registers a new punishment type handler.
     * Addons can define their own punishment logic such as custom punishment systems (Animation Punishment).
     * Available: [ONLY FOR ADDON]
     *
     * @param typeId  unique ID for the punishment type (e.g., "firekick")
     * @param handler handler implementation that executes the punishment
     */
    void addPunishmentType(String typeId, PunishmentHandler handler);

    /**
     * Executes a punishment on a player using the given punishment ID.
     * The ID must match one previously registered using {@link #addPunishmentType}.
     * Available: [ONLY FOR ADDON]
     *
     * @param player        the player to punish
     * @param punishmentId  the punishment ID (e.g., "firekick")
     */
    void executePunishment(Player player, String punishmentId);

    /**
     * Gets the current violation level (VL) for a specific check on a player.
     *
     * @param player    the target player
     * @param checkName the name of the check (e.g., "AnchorAuraA")
     * @return the current violation level for that check
     */
    int getViolation(Player player, String checkName);

    /**
     * Logs a message to the console through AngleGuard’s logger.
     *
     * @param string the message to log
     */
    void logger(String string);

    /**
     * Gets the CommandAPI instance, used for registering and managing commands within AngleGuard.
     * Available: [ONLY FOR ADDON]
     *
     * @return the {@link CommandAPI} instance
     */
    CommandAPI getCommandAPI();

    /**
     * Gets the main plugin instance of AngleGuard Pro.
     *
     * @return the {@link Plugin} instance
     */
    Plugin getPlugin();

    /**
     * Gets the total number of detection flags for a player across all checks.
     *
     * @param player the target player
     * @return total number of flags recorded for the player
     */
    int getTotalFlags(Player player);

    /**
     * Gets a list of all detection flags for a player.
     * Each entry typically includes check name, type, and details — for example:
     * <pre>{@code
     * "anchoraura [A] distance=3.85"
     * "autototem [B] delta=20ms"
     * }</pre>
     *
     * @param player the target player
     * @return list of formatted flag strings
     */
    List<String> getFlags(Player player);

    /**
     * Gets a list of detection details for a specific check and player.
     * This provides detailed insight into why a player flagged that check.
     *
     * @param player the target player
     * @param check  the check to retrieve details from
     * @return list of detailed detection strings (e.g., distances, deltas, pitch/yaw differences)
     */
    List<String> getDetails(Player player, Check check);
}
