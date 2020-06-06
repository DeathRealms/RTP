package me.deathrealms.rtp

import me.deathrealms.realmsapi.RealmsAPI
import me.deathrealms.realmsapi.files.CustomSettings
import me.deathrealms.rtp.commands.RTPCommand
import me.deathrealms.rtp.commands.ReloadCommand
import me.deathrealms.rtp.commands.ResetCommand
import me.deathrealms.rtp.listeners.DelayListener
import me.mattstudios.mf.base.CommandManager
import org.bukkit.Bukkit

class RTP : RealmsAPI() {
    override fun onEnable() {
        super.onEnable()
        createUserData()
        val settings = CustomSettings("config.yml", Config::class.java)
        settings.create()

        val manager = CommandManager(this)
        manager.register(RTPCommand(settings), ReloadCommand(settings), ResetCommand(settings))

        Bukkit.getPluginManager().registerEvents(DelayListener(settings), this)
    }
}