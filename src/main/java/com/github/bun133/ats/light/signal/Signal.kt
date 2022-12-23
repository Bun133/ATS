package com.github.bun133.ats.light.signal

/**
 * 信号[Light]の現示種類のこと
 */
abstract class Signal {
    /**
     * この現示での信号制限速度(単位 ブロック/s)
     */
    abstract fun limitedSpeed(): Double

    /**
     * この現示より一段階高い現示
     */
    abstract fun higher(): Signal

    /**
     * この現示より一段階低い現示
     */
    abstract fun lower(): Signal
}