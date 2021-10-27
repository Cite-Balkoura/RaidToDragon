package fr.grimtown.RaidToDragon.entities;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import fr.grimtown.RaidToDragon.updaters.OtherUpdater;
import fr.grimtown.RaidToDragon.updaters.RegenUpdater;
import fr.grimtown.RaidToDragon.updaters.TotemUpdater;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private final Map<UUID, GamePlayer> players;
    private boolean started;

    private final RegenUpdater regen;
    private final OtherUpdater other;
    private final TotemUpdater totem;

    public GameManager() {
        this.players = new HashMap<>();
        this.started = false;
        this.regen = new RegenUpdater();
        this.other = new OtherUpdater(this);
        this.totem = new TotemUpdater();
    }

    public void start() {
        if (this.started)
            return;
        this.players.values().forEach(player ->
            player.setStartTime(System.currentTimeMillis())
        );
        this.regen.runTaskTimer(RaidPlugin.get(), 0L, Config.get().getRegenUpdate());
        this.other.runTaskTimer(RaidPlugin.get(), 0L, Config.get().getOtherUpdate());
        this.totem.runTaskTimer(RaidPlugin.get(), 0L, Config.get().getTotemUpdate());
    }

    public void stop() {
        if (!this.regen.isCancelled())
            this.regen.cancel();
        if (!this.other.isCancelled())
            this.other.cancel();
        if (!this.totem.isCancelled())
            this.totem.cancel();
    }

    public Map<UUID, GamePlayer> getPlayers() {
        return this.players;
    }

    public boolean isStarted() {
        return this.started;
    }
}
