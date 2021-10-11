package fr.grimtown.RaidToDragon.entities;

import java.util.ArrayList;

public class GameManager {

    private final ArrayList<GamePlayer> players;
    private boolean started;

    public GameManager() {
        this.players = new ArrayList<>();
        this.started = false;
    }

    public void start() {
        if (this.started)
            return;
        this.players.forEach(player -> {
            player.setStartTime(System.currentTimeMillis());
        });
    }

    public ArrayList<GamePlayer> getPlayers() {
        return this.players;
    }

    public boolean isStarted() {
        return this.started;
    }
}
