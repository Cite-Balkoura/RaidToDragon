package fr.grimtown.RaidToDragon.listeners.others;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

public class PiglinLogic {

    /**
     * Make piglins invulnerable.
     * @param event the damage event.
     */
    public static void run(final EntityDamageEvent event) {
        System.out.println(event.getCause());
        if (    event.getCause() != EntityDamageEvent.DamageCause.VOID && (
                event.getEntityType() == EntityType.PIGLIN          ||
                event.getEntityType() == EntityType.PIGLIN_BRUTE    ||
                event.getEntityType() == EntityType.ZOMBIFIED_PIGLIN
        ))
            event.setDamage(0);
    }
}
