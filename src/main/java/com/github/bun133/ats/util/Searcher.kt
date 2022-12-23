package com.github.bun133.ats.util

import org.bukkit.Location

/**
 * 探索用メソッド
 */
fun search(
    from: Location,
    validator: (Location) -> Boolean,
    locationSuggester: LocationSuggester = allDirectionSuggester,
    max: Long? = null,
): List<Location> {
    val stack = mutableListOf<Location>()
    val confirmed = mutableListOf<Location>()

    stack.add(from)  // 最初の一つを積む

    fun processStack() {
        val exStack = mutableListOf<Location>()

        stack.forEach {
            if (validator(it)) {
                // 適切なLocationなので、resultに加えて、周囲のブロックをスタックに加える
                confirmed.add(it)
                exStack.addAll(locationSuggester(it).filter { l -> l !in stack && l !in confirmed })
            } else {
                // 適切じゃなかったのでお前は船降りろ。
            }
        }

        // stack内のが処理し終わったので、処理中に出てきた新たなる世界をstackに乗っける
        stack.clear()
        stack.addAll(exStack)
    }

    /**
     * 取得最大個数を越えたかどうか
     */
    fun isReachedMax(): Boolean {
        return if (max == null) false
        else confirmed.size >= max
    }

    while (stack.isNotEmpty() && !isReachedMax()) {
        processStack()
    }

    return confirmed
}

typealias LocationSuggester = (Location) -> List<Location>

val allDirectionSuggester: LocationSuggester = {
    listOf(
        it.clone().add(1.0, 0.0, 0.0),
        it.clone().add(-1.0, 0.0, 0.0),
        it.clone().add(0.0, 1.0, 0.0),
        it.clone().add(0.0, -1.0, 0.0),
        it.clone().add(0.0, 0.0, -1.0),
        it.clone().add(0.0, 0.0, 1.0)
    )
}

val railConnectionSuggester:LocationSuggester = {
    listOf(
        // 前後左右上下の6方向
        it.clone().add(1.0, 0.0, 0.0),
        it.clone().add(-1.0, 0.0, 0.0),
        it.clone().add(0.0, 1.0, 0.0),
        it.clone().add(0.0, -1.0, 0.0),
        it.clone().add(0.0, 0.0, -1.0),
        it.clone().add(0.0, 0.0, 1.0),

        // 高低差がある前後左右
        it.clone().add(1.0, 1.0, 0.0),
        it.clone().add(1.0, -1.0, 0.0),
        it.clone().add(-1.0, 1.0, 0.0),
        it.clone().add(-1.0, -1.0, 0.0),
        it.clone().add(0.0, 1.0, -1.0),
        it.clone().add(0.0, -1.0, -1.0),
        it.clone().add(0.0, 1.0, 1.0),
        it.clone().add(0.0, -1.0, 1.0),
    )
}