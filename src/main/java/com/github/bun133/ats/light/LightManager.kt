package com.github.bun133.ats.light

import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

/**
 * 信号の更新処理などのマネージャー
 */
class LightManager private constructor() {
    companion object {
        val instance = LightManager()
    }

    private val lights = mutableListOf<Light>()

    /**
     * レールにくっついている信号インスタンスを取得
     */
    fun getConnectedLight(railLocation: Location): Light? {
        return lights.find { it.location.railLocation.toBlockLocation() == railLocation.toBlockLocation() }
    }

    /**
     * [lightLocation]にある信号インスタンスを取得
     */
    fun getLight(lightLocation: Location): Light? {
        return lights.find { it.location.location.toBlockLocation() == lightLocation.toBlockLocation() }
    }

    /**
     * 全部の信号を一気にupdateするので実際は正常に信号が現示するまで数回updateが必要
     * TODO 列車の通過を認識してupdateを走らせる
     */
    fun update() {
        lights.forEach {
            it.update()
            it.updateWorld()
        }
    }

    fun registerTimer(plugin: JavaPlugin, interval: Long = 10L) {
        plugin.server.scheduler.runTaskTimer(plugin, Runnable { update() }, 0L, interval)
    }

    fun addLight(light: Light) {
        lights.add(light)
    }

    fun removeLight(light:Light){
        lights.remove(light)
    }
}