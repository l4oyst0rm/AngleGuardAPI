package dev.l4oyst0rm.angleGuard.api.manager;

public class Punishment {
    private final String playerName;
    private final String punishmentId;
    private final long timestamp;

    public Punishment(String playerName, String punishmentId, long timestamp) {
        this.playerName = playerName;
        this.punishmentId = punishmentId;
        this.timestamp = timestamp;
    }

    public String getPlayerName() { return playerName; }
    public String getPunishmentId() { return punishmentId; }
    public long getTimestamp() { return timestamp; }
}
