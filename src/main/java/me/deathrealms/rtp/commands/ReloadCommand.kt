package me.deathrealms.rtp.commands

import me.deathrealms.realmsapi.files.CustomSettings
import me.deathrealms.realmsapi.source.CommandSource
import me.deathrealms.realmsapi.xseries.XSound
import me.deathrealms.rtp.Config
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.PlayerAndConsole
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase

@Command("rtp")
class ReloadCommand(private val settings: CustomSettings) : CommandBase() {

    @SubCommand("reload")
    @Permission("rtp.reload")
    @PlayerAndConsole
    fun reloadCommand(source: CommandSource) {
        settings.reload()
        source.sendMessage(settings[Config.PREFIX] + "&7You have reloaded all configuration files.")
        if (source.isPlayer) {
            source.user.playSound(XSound.BLOCK_NOTE_BLOCK_PLING)
        }
    }
}