package fr.grimtown.RaidToDragon.entities.adapters;

import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.plugin.RaidPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GameAdapter {

    public static Optional<GamePlayer> adapt(final Player player) {
        return RaidPlugin.get().getGameManager().getPlayers()
                .stream().filter(gamePlayer -> gamePlayer.getUniqueId().equals(player.getUniqueId())).findFirst();
    }

    public static Optional<Player> adapt(final GamePlayer gamePlayer) {
        return Optional.ofNullable(Bukkit.getPlayer(gamePlayer.getUniqueId()));
    }
}
