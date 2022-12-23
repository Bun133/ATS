package com.github.bun133.ats.light.signal

import org.bukkit.Material

/**
 * ビーコンの色で現示するタイプの信号現示
 */
abstract class BeaconSignal : Signal() {
    abstract fun beaconColor(): Material
    abstract override fun higher():BeaconSignal
    abstract override fun lower():BeaconSignal
}

class BlueSignal private constructor() : BeaconSignal() {
    companion object {
        val instance = BlueSignal()
    }

    override fun beaconColor(): Material = Material.BLUE_STAINED_GLASS

    override fun limitedSpeed(): Double = Double.MAX_VALUE

    override fun higher(): BeaconSignal = instance

    override fun lower(): BeaconSignal = YellowSignal.instance
}

class YellowSignal private constructor() : BeaconSignal() {
    companion object {
        val instance = YellowSignal()
    }

    override fun beaconColor(): Material = Material.YELLOW_STAINED_GLASS

    override fun limitedSpeed(): Double = 2.0

    override fun higher(): BeaconSignal = BlueSignal.instance

    override fun lower(): BeaconSignal = RedSignal.instance
}

class RedSignal private constructor() : BeaconSignal() {
    companion object {
        val instance = RedSignal()
    }

    override fun beaconColor(): Material = Material.RED_STAINED_GLASS

    override fun limitedSpeed(): Double = 0.0

    override fun higher(): BeaconSignal = YellowSignal.instance

    override fun lower(): BeaconSignal = instance
}

class ErrorSignal private constructor() : BeaconSignal() {
    companion object {
        val instance = ErrorSignal()
    }

    override fun beaconColor(): Material = Material.BLACK_STAINED_GLASS

    override fun limitedSpeed(): Double = 0.0

    override fun higher(): BeaconSignal = YellowSignal.instance

    override fun lower(): BeaconSignal = instance
}
