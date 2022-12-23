package com.github.bun133.ats.light

import com.github.bun133.ats.command.LightPlaceCommand
import com.github.bun133.ats.util.isRail
import com.github.bun133.ats.util.nearByBlockLocation
import com.github.bun133.ats.util.rails
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.JavaPlugin

class LightPlacer(plugin: JavaPlugin) : Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlaceBlock(e: BlockPlaceEvent) {
        if (LightPlaceCommand.enabled.contains(e.player)) {
            if (e.blockPlaced.type == Material.BEACON || e.blockPlaced.type == Material.REDSTONE_BLOCK) {
                val rails = getNearRail(e.block.location)
                when (rails.size) {
                    0 -> {
                        // レールが見つからなかった
                        e.player.sendMessage(Component.text("レールが見つかりませんでした").color(NamedTextColor.RED))
                    }

                    1 -> {
                        // レールが見つかった
                        val light = Light(
                            LightLocation(
                                e.block.location,
                                rails[0],
                                e.player.facing
                            )
                        )

                        LightManager.instance.addLight(light)
                        e.player.sendMessage(Component.text("信号を設置しました").color(NamedTextColor.GREEN))
                    }

                    else -> {
                        // なんかいっぱい見つかった
                        e.player.sendMessage(Component.text("複数の信号が見つかりました").color(NamedTextColor.RED))
                    }
                }
            }
        }
    }

    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        when (e.block.type) {
            in rails -> {
                val light = LightManager.instance.getConnectedLight(e.block.location)
                if (light != null) {
                    // 壊したレールに信号が紐づいている場合
                    if (LightPlaceCommand.enabled.contains(e.player)) {
                        e.player.sendMessage(Component.text("紐づいていた信号を削除しました").color(NamedTextColor.GREEN))
                        deleteLight(light)
                    } else {
                        e.player.sendMessage(Component.text("信号が紐づいているので破壊できません").color(NamedTextColor.RED))
                        e.isCancelled = true
                    }
                } else {
                    // 信号が紐づいていないレールなのでヨシ!
                }
            }

            Material.BEACON, Material.REDSTONE_BLOCK -> {
                val light = LightManager.instance.getLight(e.block.location)

                if (light != null) {
                    // 壊したビーコンに信号インスタンスが紐づいていた場合
                    if (LightPlaceCommand.enabled.contains(e.player)) {
                        e.player.sendMessage(Component.text("信号を削除しました").color(NamedTextColor.GREEN))
                        deleteLight(light)
                    } else {
                        e.player.sendMessage(Component.text("信号なので破壊できません").color(NamedTextColor.RED))
                        e.isCancelled = true
                    }
                } else {
                    // 壊したビーコンに信号インスタンスが紐づいていないのでヨシ!
                }
            }

            else -> {
                // 興味なし
            }
        }
    }

    private fun deleteLight(light: Light) {
        LightManager.instance.removeLight(light)
    }

    private fun getNearRail(location: Location) = location.block.nearByBlockLocation().filter { it.block.type.isRail() }
}