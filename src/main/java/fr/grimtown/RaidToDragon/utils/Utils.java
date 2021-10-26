package fr.grimtown.RaidToDragon.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.StringTokenizer;

public class Utils {

    public static void sendActionBar(final Player player, final String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public static String truncate(final double x, final int pow) {
        int n2;
        final String token = String.valueOf(Math.round(x * Math.pow(10, pow)) / Math.pow(10, pow));
        StringTokenizer t = new StringTokenizer(token, ".");
        String s1 = t.nextToken();
        String s2 = t.nextToken();
        n2 = s2.length();
        if (n2 != 1) {
            if (s2.charAt(s2.length() - 1) == '0') n2 = s2.length() - 1;
        }
        if (n2 == 0 || (n2 == 1 && s2.charAt(0) == '0')) {
            return s1;
        }
        return token;
    }

    public static String fromDuration(final long timeStamp) {
        final int millis = (int) Math.ceil(timeStamp / 1000D);
        final int seconds = (int) Math.ceil(millis % 60D);
        final int minutes = (int) Math.floor((millis / 60D) % 60D);
        final int hours = (int) Math.floor((millis / 3600D) % 24D);
        final int days = (int) Math.floor((millis / 3600D / 24D) % 30D);
        final int months = (int) Math.floor((millis / 3600D / 24D / 30D) % 12D);
        final int years = (int) Math.floor(millis / 3600D / 24D / 30D / 12D);
        final StringBuilder sb = new StringBuilder();
        if (years > 0)
            sb.append(years).append(" an").append(years > 1 ? "s" : "").append(isNotNull(1, months, days, hours, minutes, seconds) ? " et " : (!isNotNull(0, months, days, hours, minutes, seconds) ? ", " : ""));
        if (months > 0)
            sb.append(months).append(" mois").append(isNotNull(1, days, hours, minutes, seconds) ? " et " : (!isNotNull(0, days, hours, minutes, seconds) ? ", " : ""));
        if (days > 0)
            sb.append(days).append(" jour").append(days > 1 ? "s" : "").append(isNotNull(1, hours, minutes, seconds) ? " et " : (!isNotNull(0, hours, minutes, seconds) ? ", " : ""));
        if (hours > 0)
            sb.append(hours).append(" heure").append(hours > 1 ? "s" : "").append(isNotNull(1, minutes, seconds) ? " et " : (!isNotNull(0, minutes, seconds) ? ", " : ""));
        if (minutes > 0)
            sb.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(isNotNull(1, seconds) ? " et " : "");
        if (seconds > 0)
            sb.append(seconds).append(" seconde").append(seconds > 1 ? "s" : "");
        if (sb.length() == 0)
            sb.append("maintenant");
        return sb.toString();
    }

    private static boolean isNotNull(final int numbers, final int... integers) {
        return Arrays.stream(integers).asDoubleStream().filter(number -> number != 0).count() == numbers;
    }

    public static String[] getSkin(final Player player) {
        final GameProfile gameProfile = (GameProfile) ReflectionUtils.executeMethod(ReflectionUtils.getHandle(player),"getProfile");
        if (gameProfile == null)
            return new String[] { "", "" };
        final Property property = gameProfile.getProperties().get("textures").iterator().next();
        final String value = property.getValue();
        final String signature = property.getSignature();
        return new String[] { value, signature };
    }

}
