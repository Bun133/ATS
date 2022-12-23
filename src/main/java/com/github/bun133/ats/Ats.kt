package com.github.bun133.ats

import com.github.bun133.ats.command.LightPlaceCommand
import com.github.bun133.ats.light.LightManager
import com.github.bun133.ats.light.LightPlacer
import org.bukkit.plugin.java.JavaPlugin

class Ats : JavaPlugin() {
    override fun onEnable() {
        LightManager.instance.registerTimer(this)
        LightPlacer(this)
        LightPlaceCommand(this)
    }

    override fun onDisable() {
    }
}