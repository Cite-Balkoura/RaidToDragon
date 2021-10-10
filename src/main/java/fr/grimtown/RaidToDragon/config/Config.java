package fr.grimtown.RaidToDragon.config;

public class Config {

    private static Config instance;

    public static Config get() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

    private boolean debug = true;

    private Config() {
    }

    public boolean hasDebugMode() {
        return debug;
    }
}
