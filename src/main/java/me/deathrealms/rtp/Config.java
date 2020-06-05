package me.deathrealms.rtp;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {
    public static final Property<String> PREFIX = newProperty("messages.prefix", "&8[&dRTP&8] ");
    @Comment("Placeholders: %location% %x% %y% %z%")
    public static final Property<String> TELEPORT_SUCCESSFUL = newProperty("messages.teleport-successful", "&aTeleported to &f%location%");
    public static final Property<String> TELEPORT_COOLDOWN = newProperty("messages.teleport-cooldown", "&eYou are currently on cooldown for &f%time%.");
    public static final Property<String> TELEPORT_CANCELLED = newProperty("messages.teleport-cancelled", "&cTeleportation cancelled.");
    public static final Property<String> TELEPORT_IN_PROGRESS = newProperty("messages.teleport-in-progress", "&fYou will be teleported in &c%delay% &fseconds.");

    public static final Property<Integer> minX = newProperty("settings.minX", 0);
    public static final Property<Integer> minZ = newProperty("settings.minZ", 0);
    public static final Property<Integer> maxX = newProperty("settings.maxX", 500);
    public static final Property<Integer> maxZ = newProperty("settings.maxZ", 500);

    @Comment("Can be bypassed with \"rtp.bypass.cooldown\"")
    public static final Property<Boolean> COOLDOWN_ENABLED = newProperty("settings.cooldown.enabled", true);
    @Comment("Time in seconds.")
    public static final Property<Integer> COOLDOWN_TIME = newProperty("settings.cooldown.time", 300);

    @Comment("Can be bypassed with \"rtp.bypass.delay\"")
    public static final Property<Boolean> DELAY_ENABLED = newProperty("settings.delay.enabled", true);
    @Comment("Time in seconds.")
    public static final Property<Integer> DELAY_TIME = newProperty("settings.delay.time", 5);
}
