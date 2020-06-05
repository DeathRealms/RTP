package me.deathrealms.rtp.listeners

import me.deathrealms.realmsapi.RealmsAPI
import me.deathrealms.realmsapi.files.CustomSettings
import me.deathrealms.rtp.Config
import me.deathrealms.rtp.commands.RTPCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class DelayListener(private val settings: CustomSettings) : Listener {

    @EventHandler
    fun PlayerMoveEvent.onPlayerMove() {
        if (from.blockX != to?.blockX || from.blockZ != to?.blockZ || from.blockY != to?.blockY) {
            val user = RealmsAPI.getUser(player)
            val task = RTPCommand.delays[user.uuid] ?: return
            task.cancel()
            RTPCommand.delays.remove(user.uuid)
            user.data.removeCooldown("rtp")
            user.sendMessage(settings.getProperty(Config.PREFIX) + settings.getProperty(Config.TELEPORT_CANCELLED))
        }
    }
}