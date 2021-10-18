package fr.grimtown.RaidToDragon.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.*;
import java.util.Arrays;

public class ReflectionUtils {

    private static final String NET_MINECRAFT_SERVER_PACKAGE_PATH;
    private static final String CRAFT_BUKKIT_PACKAGE_PATH;
    public static final int MINECRAFT_VERSION;

    static {
        final String serverPath = Bukkit.getServer().getClass().getPackage().getName();
        final String version = serverPath.substring(serverPath.lastIndexOf(".") + 1);
        final String packageVersion = serverPath.substring(serverPath.lastIndexOf(".") + 2);
        MINECRAFT_VERSION = Integer.parseInt(packageVersion.substring(0, packageVersion.lastIndexOf("_")).replace("_", ".").substring(2));
        NET_MINECRAFT_SERVER_PACKAGE_PATH = "net.minecraft" + (MINECRAFT_VERSION < 17 ? ".server." + version : "");
        CRAFT_BUKKIT_PACKAGE_PATH = "org.bukkit.craftbukkit." + version;
    }


    public static void sendPacket(final Player player, final Object packet) {
        final Object entityPlayer = getHandle(player);
        Object playerConnection = null;
        if (MINECRAFT_VERSION < 17)
            playerConnection = readField(entityPlayer, "playerConnection");
        else
            playerConnection = readField(entityPlayer, "b");
        executeMethod(playerConnection, getMethod(playerConnection.getClass(), "sendPacket", getNmsClass("network.protocol", "Packet")), packet);
    }

    public static Object getHandle(final Object object) {
        return executeMethod(object, "getHandle");
    }

    public static Class<?> getCraftClass(final String name) {
        try {
            return Class.forName(CRAFT_BUKKIT_PACKAGE_PATH + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getNmsClass(final String name) {
        return getNmsClass("", name);
    }

    public static Class<?> getNmsClass(String packageName, final String name) {
        try {
            if (MINECRAFT_VERSION < 17)
                packageName = "";
            return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_PATH + "." + packageName + (packageName.equals("") ? "" : ".") + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getPacketClass(final String name) {
        try {
            if (MINECRAFT_VERSION < 17)
                return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_PATH + "." + name);
            else
                return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_PATH + ".network.protocol.game." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getWorldClass() {
        try {
            if (MINECRAFT_VERSION < 17)
                return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_PATH + ".World");
            else
                return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_PATH + ".world.level.World");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getArmorStandClass() {
        try {
            if (MINECRAFT_VERSION < 17)
                return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_PATH + ".EntityArmorStand");
            else
                return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_PATH + ".world.entity.decoration.EntityArmorStand");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(final Class<?> clazz, final String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            try {
                return clazz.getDeclaredField(name);
            }
            catch (NoSuchFieldException ignored) {
            }
            e.printStackTrace();
        }
        return null;
    }

    public static Object readField(final Object object, final String name) {
        try {
            final Field field = getField(object.getClass(), name);
            field.setAccessible(true);
            return field.get(Modifier.isStatic(field.getModifiers()) ? null : object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(final Class<?> targetClass, final String name, final Class<?>... parameters) {
        try {
            return targetClass.getMethod(name, parameters);
        } catch (NoSuchMethodException e) {
            try {
                return targetClass.getDeclaredMethod(name, parameters);
            } catch (NoSuchMethodException ignored) {
            }
            e.printStackTrace();
        }
        return null;
    }

    public static Object executeMethod(final Object object, final Method method, final Object... parameters) {
        try {
            method.setAccessible(true);
            return method.invoke(object, parameters);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object executeMethod(final Object object, final String name, final Object... parameters) {
        try {
            final Method method = getMethod(object.getClass(), name, Arrays.stream(parameters).map(Object::getClass).toList().toArray(new Class<?>[0]));
            method.setAccessible(true);
            return method.invoke(object, parameters);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Constructor<?> getConstructor(final Class<?> targetClass, final Class<?>... parameterTypes) {
        try {
            return targetClass.getConstructor(parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object executeConstructor(final Class<?> targetClass, final Object... parameters) {
        try {
            final Constructor<?> constructor = getConstructor(targetClass, Arrays.stream(parameters).map(Object::getClass).toList().toArray(new Class<?>[0]));
            constructor.setAccessible(true);
            return constructor.newInstance(parameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object executeConstructor(final Constructor<?> constructor, final Object... parameters) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(parameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setCustomName(final Object entity, final String customName){
        try {
            Class<?> nmsChatBaseComponentClazz = null;
            Object componentText = null;
            if (MINECRAFT_VERSION < 17) {
                nmsChatBaseComponentClazz = ReflectionUtils.getNmsClass("IChatBaseComponent");
                componentText = ReflectionUtils.getNmsClass("ChatComponentText").getConstructor(String.class).newInstance(ChatColor.translateAlternateColorCodes('&', customName));
            } else {
                nmsChatBaseComponentClazz = ReflectionUtils.getNmsClass("network.chat","IChatBaseComponent");
                componentText = ReflectionUtils.getNmsClass("network.chat", "ChatComponentText").getConstructor(String.class).newInstance(ChatColor.translateAlternateColorCodes('&', customName));
            }
            entity.getClass().getMethod("setCustomName", nmsChatBaseComponentClazz).invoke(entity, componentText);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object toNMS(final ItemStack item) {
        return executeMethod(null, getMethod(getCraftClass("inventory.CraftItemStack"), "asNMSCopy", item.getClass()), item);
    }

    public static Object toNMS(final EquipmentSlot slot) {
        return ((Object[]) executeMethod(null, getMethod(getNmsClass("world.entity", "EnumItemSlot"), "values")))[slot.ordinal()];
    }

    public static Object toPair(final Object first, final Object second) {
        try {
            final Class<?> clazz = Class.forName("com.mojang.datafixers.util.Pair");
            final Constructor<?> constructor = clazz.getConstructor(Object.class, Object.class);
            return constructor.newInstance(first, second);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
