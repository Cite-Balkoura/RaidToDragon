package fr.grimtown.RaidToDragon.logs;

import fr.grimtown.RaidToDragon.utils.ItemSerializer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class DeathLog extends LogEvent {

    private final EntityType killerType;
    private UUID killerId;

    private final String[] victimInventory;
    private String[] killerInventory;

    public DeathLog(final Player player, final EntityType killerType, final Optional<Player> killer) {
        super(player.getUniqueId());
        this.victimInventory = ItemSerializer.playerInventoryToBase64(player.getInventory());
        this.killerType = killerType;
        killer.ifPresent(pKiller -> {
            this.killerId = pKiller.getUniqueId();
            this.killerInventory = ItemSerializer.playerInventoryToBase64(pKiller.getInventory());
        });
    }

    public EntityType getKillerType() {
        return this.killerType;
    }

    public UUID getKillerId() {
        return this.killerId;
    }

    public String[] getVictimInventory() {
        return this.victimInventory;
    }

    public String[] getKillerInventory() {
        return this.killerInventory;
    }
}
