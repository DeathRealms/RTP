package me.deathrealms.rtp.commands

import me.deathrealms.realmsapi.RealmsAPI
import me.deathrealms.realmsapi.files.CustomSettings
import me.deathrealms.realmsapi.source.CommandSource
import me.deathrealms.rtp.Config
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit

@Command("rtp")
class ResetCommand(private val settings: CustomSettings) : CommandBase() {

    @SubCommand("reset")
    @Permission("rtp.reset")
    @Completion("#allplayers")
    @PlayerAndConsole
    fun resetCommand(source: CommandSource, player: String) {
        val offlinePlayer = Bukkit.getOfflinePlayer(player)
        if (!offlinePlayer.hasPlayedBefore()) {
            source.sendMessage(settings[Config.PREFIX] + "&cCould not find player")
            return
        }
        val target = RealmsAPI.getUser(offlinePlayer)

        if (!target.data.hasCooldown("rtp")) {
            source.sendMessage(
                settings[Config.PREFIX] + settings[Config.NO_COOLDOWN]
                    .replace("%player%", target.name)
            )
            return
        }

        target.data.removeCooldown("rtp")
        source.sendMessage(
            settings[Config.PREFIX] + settings[Config.COOLDOWN_RESET]
                .replace("%player%", target.name)
        )
    }
}