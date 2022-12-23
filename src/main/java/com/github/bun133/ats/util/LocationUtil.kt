package com.github.bun133.ats.util

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockFace.*
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

/**
 * [blockFace]の方角に1足します。
 */
fun Location.add(blockFace: BlockFace, amount: Int = 1): Location {
    val vec = when (blockFace) {
        NORTH -> Vector(.0, .0, -1.0)
        EAST -> Vector(1.0, .0, .0)
        SOUTH -> Vector(.0, .0, 1.0)
        WEST -> Vector(-1.0, .0, .0)
        UP -> Vector(.0, 1.0, .0)
        DOWN -> Vector(.0, -1.0, .0)
        NORTH_EAST -> Vector(1.0, .0, -1.0)
        NORTH_WEST -> Vector(-1.0, .0, -1.0)
        SOUTH_EAST -> Vector(1.0, .0, 1.0)
        SOUTH_WEST -> Vector(-1.0, .0, 1.0)
        WEST_NORTH_WEST -> Vector(-2.0, .0, -1.0)
        NORTH_NORTH_WEST -> Vector(-1.0, .0, -2.0)
        NORTH_NORTH_EAST -> Vector(1.0, .0, -2.0)
        EAST_NORTH_EAST -> Vector(2.0, .0, -1.0)
        EAST_SOUTH_EAST -> Vector(2.0, .0, 1.0)
        SOUTH_SOUTH_EAST -> Vector(1.0, .0, 2.0)
        SOUTH_SOUTH_WEST -> Vector(-1.0, .0, 2.0)
        WEST_SOUTH_WEST -> Vector(-2.0, .0, 1.0)
        SELF -> Vector(.0, .0, .0)
    }

    return this.add(vec)
}

fun Location.nearByBlockLocation(): List<Location> {
    return listOf(
        this.clone().add(.0, .0, 1.0),
        this.clone().add(.0, .0, -1.0),
        this.clone().add(.0, 1.0, .0),
        this.clone().add(.0, -1.0, .0),
        this.clone().add(1.0, .0, .0),
        this.clone().add(-1.0, .0, .0),
    )
}

fun Block.nearByBlockLocation() = this.location.nearByBlockLocation()