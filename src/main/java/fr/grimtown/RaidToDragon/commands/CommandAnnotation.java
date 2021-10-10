package fr.grimtown.RaidToDragon.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {

    String command();
    int arguments() default 0;

    String permission() default "";
    ExecutorType executor() default ExecutorType.ALL;

    public static enum ExecutorType {
        PLAYER,
        CONSOLE,
        ALL
    }

}
