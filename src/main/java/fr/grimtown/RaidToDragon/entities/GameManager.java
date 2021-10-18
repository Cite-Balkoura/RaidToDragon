package fr.grimtown.RaidToDragon.entities;

import fr.grimtown.RaidToDragon.config.Config;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import fr.grimtown.RaidToDragon.updaters.OtherUpdater;
import fr.grimtown.RaidToDragon.updaters.RegenUpdater;

import java.util.ArrayList;

public class GameManager {

    private final ArrayList<GamePlayer> players;
    private boolean started;

    private final RegenUpdater regen;
    private final OtherUpdater other;

    public GameManager() {
        this.players = new ArrayList<>();
        this.started = false;
        this.regen = new RegenUpdater();
        this.other = new OtherUpdater(this);

    }

    public void start() {
        if (this.started)
            return;
        this.players.forEach(player -> {
            player.setStartTime(System.currentTimeMillis());
        });
        this.regen.runTaskTimer(RaidPlugin.get(), 0L, Config.get().getRegenUpdate());
        this.other.runTaskTimer(RaidPlugin.get(), 0L, Config.get().getOtherUpdate());
    }

    public void stop() {
        if (!this.regen.isCancelled())
            this.regen.cancel();
        if (!this.other.isCancelled())
            this.other.cancel();
    }

    public ArrayList<GamePlayer> getPlayers() {
        return this.players;
    }

    public boolean isStarted() {
        return this.started;
    }
}
