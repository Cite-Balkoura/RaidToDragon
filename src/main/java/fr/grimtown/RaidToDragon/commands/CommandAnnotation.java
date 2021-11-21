package fr.grimtown.RaidToDragon.commands;

import fr.grimtown.RaidToDragon.config.Permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {

    String command();
    int arguments() default 0;

    Permissions permission() default Permissions.NONE;
    ExecutorType executor() default ExecutorType.ALL;

    public static enum ExecutorType {
        PLAYER,
        CONSOLE,
        ALL
    }

}
