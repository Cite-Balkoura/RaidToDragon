package fr.grimtown.RaidToDragon.entities.adapters;

import fr.grimtown.RaidToDragon.entities.GamePlayer;
import fr.grimtown.RaidToDragon.entities.GameTeam;
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

    public static Optional<GameTeam> getTeam(final Player player) {
        final Optional<GamePlayer> gamePlayer = GameAdapter.adapt(player);
        if (gamePlayer.isEmpty())
            return Optional.empty();
        return getTeam(gamePlayer.get());
    }

    public static Optional<GameTeam> getTeam(final GamePlayer gamePlayer) {
        return Optional.ofNullable(gamePlayer.getTeam());
    }

    public static boolean inSameTeam(final Player player, final Player opponent) {
        final Optional<GamePlayer> gamePlayer = GameAdapter.adapt(player);
        final Optional<GamePlayer> gameOpponent = GameAdapter.adapt(opponent);
        if (gamePlayer.isPresent() && gameOpponent.isPresent())
            return gameOpponent.get().getTeam() == gamePlayer.get().getTeam();
        return false;
    }

}
