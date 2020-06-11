package me.deathrealms.rtp

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty

class Config : SettingsHolder {
    companion object {
        val PREFIX: Property<String> = newProperty("messages.prefix", "&8[&dRTP&8] ")

        @Comment("Placeholders: %location% %x% %y% %z%")
        val TELEPORT_SUCCESSFUL: Property<String> =
            newProperty("messages.teleport-successful", "&aTeleported to &f%location%")
        val TELEPORT_COOLDOWN: Property<String> =
            newProperty("messages.teleport-cooldown", "&eYou are currently on cooldown for &f%time%.")
        val TELEPORT_CANCELLED: Property<String> =
            newProperty("messages.teleport-cancelled", "&cTeleportation cancelled.")
        val TELEPORT_IN_PROGRESS: Property<String> =
            newProperty("messages.teleport-in-progress", "&fYou will be teleported in &c%delay% &fseconds.")
        val TELEPORT_FAILED: Property<String> =
            newProperty("messages.teleport-failed", "&cCould not find a safe location to teleport to.")
        val NO_COOLDOWN: Property<String> =
            newProperty("messages.no-cooldown", "&f%player% &cis not currently on cooldown.")
        val COOLDOWN_RESET: Property<String> =
            newProperty("messages.cooldown-reset", "&aCooldown for &f%player% &ahas been reset.")

        @Comment("The amount of time in seconds it should wait to cancel the teleport if it can't find a safe location")
        val TIMEOUT: Property<Int> = newProperty("settings.timeout", 5)
        val minX: Property<Int> = newProperty("settings.minX", 0)
        val minZ: Property<Int> = newProperty("settings.minZ", 0)
        val maxX: Property<Int> = newProperty("settings.maxX", 500)
        val maxZ: Property<Int> = newProperty("settings.maxZ", 500)

        @Comment("Can be bypassed with \"rtp.bypass.cooldown\"")
        val COOLDOWN_ENABLED: Property<Boolean> = newProperty("settings.cooldown.enabled", true)

        @Comment("Time in seconds.")
        val COOLDOWN_TIME: Property<Int> = newProperty("settings.cooldown.time", 300)

        @Comment("Can be bypassed with \"rtp.bypass.delay\"")
        val DELAY_ENABLED: Property<Boolean> = newProperty("settings.delay.enabled", true)

        @Comment("Time in seconds.")
        val DELAY_TIME: Property<Int> = newProperty("settings.delay.time", 5)
    }
}