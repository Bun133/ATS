package com.github.bun133.ats.light

import com.github.bun133.ats.light.signal.BeaconSignal
import com.github.bun133.ats.light.signal.ErrorSignal
import com.github.bun133.ats.light.signal.RedSignal
import com.github.bun133.ats.light.signal.YellowSignal
import com.github.bun133.ats.util.add
import com.github.bun133.ats.util.isRail
import com.github.bun133.ats.util.railConnectionSuggester
import com.github.bun133.ats.util.search
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Minecart

/**
 * 信号のこと
 */
class Light(
    val location: LightLocation
) {
    /**
     * 信号の表示色を更新(ロジック)
     */
    fun update() {
        val (rails, light) = section()
        sig = if (rails.isEmpty()) {
            // レールに信号がくっついていない
            ErrorSignal.instance
        } else if (hasTrainIn(rails)) {
            // 閉塞内に列車がいるので現示を赤に。
            RedSignal.instance
        } else if (light != null) {
            // 閉塞内に列車がいないので、現示を前方の信号の一段階上へ
            light.signal().higher()
        } else {
            // 閉塞内に列車がいないが、前方に信号がないのでとりあえず注意現示
            YellowSignal.instance
        }
    }

    /**
     * 信号の表示色を更新(ワールド内でのブロック変更可)
     */
    fun updateWorld() {
        val toSetRedStone = signal().limitedSpeed() > 0.0
        val color = signal().beaconColor()
        // 信号の下の部分
        val base = location.location.block
        val baseType = if (toSetRedStone) {
            Material.REDSTONE_BLOCK
        } else {
            Material.BEACON
        }
        if (base.type != baseType) base.type = baseType

        val glass = location.location.clone().add(.0, 1.0, .0).block
        if (glass.type != color) glass.type = color
    }

    private var sig: BeaconSignal = RedSignal.instance

    /**
     * この信号の現示
     */
    fun signal(): BeaconSignal = sig

    /**
     * @return 信号の横のレールから次の信号までのレールブロック,次の信号(あれば)
     */
    private fun section(): Pair<List<Location>, Light?> {
        var light: Light? = null
        fun checkLight(location: Location): Boolean {
            val l = LightManager.instance.getConnectedLight(location)
            if (l != null) light = l    // TODO 反対方向の信号も検知してしまう
            return l == null
        }

        val rail = search(
            location.railLocation.clone().add(location.direction),
            { it.block.type.isRail() && checkLight(it) },
            { railConnectionSuggester(it).filter { l -> l != location.railLocation } }
        )

        return rail to light
    }

    /**
     * 指定された[rails]の区間内に列車が存在するかどうか
     */
    private fun hasTrainIn(rails: List<Location>): Boolean {
        return rails.any { it.hasTrain() }
    }

    /**
     * このLocationに列車が存在するかどうか
     */
    private fun Location.hasTrain(): Boolean {
        return this.getNearbyEntitiesByType(Minecart::class.java, 0.05, 0.75).isNotEmpty()
    }
}

data class LightLocation(
    val location: Location,
    val railLocation: Location,
    val direction: BlockFace
)
