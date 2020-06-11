package me.deathrealms.rtp.commands

import me.deathrealms.realmsapi.RealmsAPI
import me.deathrealms.realmsapi.files.CustomSettings
import me.deathrealms.realmsapi.items.ItemTag
import me.deathrealms.realmsapi.user.User
import me.deathrealms.realmsapi.utils.Utils
import me.deathrealms.rtp.Config
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Location
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask
import java.util.*

@Command("rtp")
@Alias("wild")
class RTPCommand(private val settings: CustomSettings) : CommandBase(), Listener {
    companion object {
        val delays = mutableMapOf<UUID, BukkitTask>()
    }

    @Default
    @Permission("rtp.use")
    fun rtpCommand(user: User) {
        var location = user.location.getRandomLocation()
        val timeout = System.currentTimeMillis() + Utils.toMillis(settings.getProperty(Config.TIMEOUT))

        while (!location.isSafeBlock()) {
            if (System.currentTimeMillis() > timeout) {
                return user.sendMessage(settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_FAILED))
            }
            if (user.data.hasCooldown("rtp")) break
            if (user.uuid in delays) return
            location = user.location.getRandomLocation()
        }

        if (settings.getProperty(Config.COOLDOWN_ENABLED)) {
            if (!user.data.giveCooldown("rtp", settings.getProperty(Config.COOLDOWN_TIME), "rtp.bypass.cooldown")) {
                return user.sendMessage(
                    settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_COOLDOWN)
                        .replace("%time%", user.data.getCooldown("rtp"))
                )
            }
        }

        if (settings.getProperty(Config.DELAY_ENABLED)) {
            if (!user.isAuthorized("rtp.bypass.delay")) {
                user.sendMessage(
                    settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_IN_PROGRESS)
                        .replace("%delay%", settings.getProperty(Config.DELAY_TIME).toString())
                )
                delays[user.uuid] = RealmsAPI.runTask(false, {
                    user.teleportTo(location)
                    delays.remove(user.uuid)
                }, (settings.getProperty(Config.DELAY_TIME) * 20).toLong())
                return
            }
        }

        user.teleportTo(location)
    }

    private fun User.teleportTo(location: Location) {
        user.teleport(location.toCenterLocation().add(0.0, 1.0, 0.0))
        user.sendMessage(
            settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_SUCCESSFUL)
                .replace("%location%", "X: ${location.blockX} Y: ${location.blockY} Z: ${location.blockZ}")
                .replace("%x%", "${location.blockX}")
                .replace("%y%", "${location.blockY}")
                .replace("%z%", "${location.blockZ}")
        )
    }

    private fun Location.getRandomLocation(): Location {
        val x = Utils.randomNumber(settings.getProperty(Config.minX), settings.getProperty(Config.maxX))
        val z = Utils.randomNumber(settings.getProperty(Config.minZ), settings.getProperty(Config.maxZ))
        val y = world.getHighestBlockYAt(x.toInt(), z.toInt()).toDouble()
        return Location(world, x, y, z, yaw, pitch)
    }

    private fun Location.isSafeBlock(): Boolean {
        return ItemTag.VALID_SPAWN.isTagged(block.type)
    }
}