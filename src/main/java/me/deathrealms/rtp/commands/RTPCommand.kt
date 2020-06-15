package me.deathrealms.rtp.commands

import me.deathrealms.realmsapi.RealmsAPI
import me.deathrealms.realmsapi.files.CustomSettings
import me.deathrealms.realmsapi.items.ItemTag
import me.deathrealms.realmsapi.user.User
import me.deathrealms.realmsapi.utils.Utils
import me.deathrealms.rtp.Config
import me.mattstudios.mf.annotations.*
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
    @Description("Teleports you to a random location.")
    fun rtpCommand(user: User) {
        var location = user.location.getRandomLocation()
        val timeout = System.currentTimeMillis() + Utils.toMillis(settings[Config.TIMEOUT])

        while (!location.isSafeBlock()) {
            if (System.currentTimeMillis() > timeout) {
                return user.sendMessage(settings[Config.PREFIX] + settings[Config.TELEPORT_FAILED])
            }
            if (user.data.hasCooldown("rtp")) break
            if (user.uuid in delays) return
            location = user.location.getRandomLocation()
        }

        if (settings[Config.COOLDOWN_ENABLED]) {
            if (!user.data.giveCooldown("rtp", settings[Config.COOLDOWN_TIME], "rtp.bypass.cooldown")) {
                return user.sendMessage(
                    settings[Config.PREFIX] + settings[Config.TELEPORT_COOLDOWN]
                        .replace("%time%", user.data.getCooldown("rtp"))
                )
            }
        }

        if (settings[Config.DELAY_ENABLED]) {
            if (!user.isAuthorized("rtp.bypass.delay")) {
                user.sendMessage(
                    settings[Config.PREFIX] + settings[Config.TELEPORT_IN_PROGRESS]
                        .replace("%delay%", settings[Config.DELAY_TIME].toString())
                )
                delays[user.uuid] = RealmsAPI.runTask(false, {
                    user.teleportTo(location)
                    delays.remove(user.uuid)
                }, (settings[Config.DELAY_TIME] * 20).toLong())
                return
            }
        }

        user.teleportTo(location)
    }

    private fun User.teleportTo(location: Location) {
        teleport(location.toCenterLocation().add(0.0, 1.0, 0.0))
        sendMessage(
            settings[Config.PREFIX] + settings[Config.TELEPORT_SUCCESSFUL]
                .replace("%location%", "X: ${location.blockX} Y: ${location.blockY} Z: ${location.blockZ}")
                .replace("%x%", "${location.blockX}")
                .replace("%y%", "${location.blockY}")
                .replace("%z%", "${location.blockZ}")
        )
    }

    private fun Location.getRandomLocation(): Location {
        val x = Utils.randomNumber(settings[Config.minX], settings[Config.maxX])
        val z = Utils.randomNumber(settings[Config.minZ], settings[Config.maxZ])
        val y = world.getHighestBlockYAt(x.toInt(), z.toInt()).toDouble()
        return Location(world, x, y, z, yaw, pitch)
    }

    private fun Location.isSafeBlock(): Boolean {
        return ItemTag.VALID_SPAWN.isTagged(block.type)
    }
}