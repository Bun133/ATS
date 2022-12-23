package com.github.bun133.ats.util

import org.bukkit.Material

val rails = listOf(
    Material.RAIL, Material.ACTIVATOR_RAIL, Material.DETECTOR_RAIL, Material.POWERED_RAIL
)

fun Material.isRail(): Boolean {
    return rails.contains(this)
}