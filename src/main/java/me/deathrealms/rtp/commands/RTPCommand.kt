package me.deathrealms.rtp.commands

import me.deathrealms.realmsapi.RealmsAPI
import me.deathrealms.realmsapi.files.CustomSettings
import me.deathrealms.realmsapi.user.User
import me.deathrealms.realmsapi.utils.LocationUtils
import me.deathrealms.realmsapi.utils.Utils
import me.deathrealms.rtp.Config
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Location
import org.bukkit.Tag
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask
import java.util.*

@Command("rtp")
class RTPCommand(private val settings: CustomSettings) : CommandBase(), Listener {
    companion object {
        val delays = mutableMapOf<UUID, BukkitTask>()
    }

    @Default
    @Permission("rtp.use")
    fun rtpCommand(user: User) {
        var isSafe = false
        var location = getRandomLocation(user.location)
        while (!isSafe) {
            location = getRandomLocation(user.location)
            if (isSafeBlock(location)) isSafe = true
        }

        if (settings.getProperty(Config.COOLDOWN_ENABLED)) {
            if (!user.isAuthorized("rtp.bypass.cooldown")) {
                if (!user.data.hasCooldown("rtp")) {
                    user.data.giveCooldown("rtp", settings.getProperty(Config.COOLDOWN_TIME))
                } else {
                    user.sendMessage(
                        settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_COOLDOWN)
                            .replace("%time%", user.data.getCooldown("rtp"))
                    )
                    return
                }
            }
        }

        if (settings.getProperty(Config.DELAY_ENABLED)) {
            if (!user.isAuthorized("rtp.bypass.delay")) {
                delays.remove(user.uuid)
                user.sendMessage(
                    settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_IN_PROGRESS)
                        .replace("%delay%", settings.getProperty(Config.DELAY_TIME).toString())
                )
                delays[user.uuid] = RealmsAPI.runTask(false, {
                    teleportPlayer(user, location)
                    delays.remove(user.uuid)
                }, (settings.getProperty(Config.DELAY_TIME) * 20).toLong())
                return
            }
        }

        teleportPlayer(user, location)
    }

    private fun teleportPlayer(user: User, location: Location) {
        user.teleport(LocationUtils.getCenteredLocation(location.add(0.0, 1.0, 0.0)))
        user.sendMessage(
            settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_SUCCESSFUL)
                .replace("%location%", "X: ${location.blockX} Y: ${location.blockY} Z: ${location.blockZ}")
                .replace("%x%", "${location.blockX}")
                .replace("%y%", "${location.blockY}")
                .replace("%z%", "${location.blockZ}")
        )
    }

    private fun getRandomLocation(location: Location): Location {
        val x = Utils.randomNumber(settings.getProperty(Config.minX), settings.getProperty(Config.maxX))
        val z = Utils.randomNumber(settings.getProperty(Config.minZ), settings.getProperty(Config.maxZ))
        val y = location.world?.getHighestBlockYAt(x.toInt(), z.toInt())!!.toDouble()
        return Location(location.world, x, y, z, location.yaw, location.pitch)
    }

    private fun isSafeBlock(location: Location): Boolean {
        return Tag.VALID_SPAWN.isTagged(location.block.type)
    }
}