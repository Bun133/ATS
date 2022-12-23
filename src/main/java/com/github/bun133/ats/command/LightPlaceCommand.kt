package com.github.bun133.ats.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class LightPlaceCommand(val plugin: JavaPlugin) : CommandExecutor, TabCompleter {
    companion object {
        val enabled = mutableListOf<Player>()
    }

    init {
        plugin.getCommand("light")!!.setExecutor(this)
        plugin.getCommand("light")!!.tabCompleter = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            if (args != null) {
                when (args.size) {
                    1 -> {
                        when (args[0]) {
                            "enable" -> {
                                enabled.add(sender)
                                sender.sendMessage(Component.text("有効化しました").color(NamedTextColor.GREEN))
                                return true
                            }

                            "disable" -> {
                                enabled.remove(sender)
                                sender.sendMessage(Component.text("無効化しました").color(NamedTextColor.GRAY))
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    private val suggestion = listOf("enable", "disable")

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String>? {
        if (sender is Player) {
            if (args == null) return suggestion.toMutableList()
        }

        return emptyList<String>().toMutableList()
    }
}