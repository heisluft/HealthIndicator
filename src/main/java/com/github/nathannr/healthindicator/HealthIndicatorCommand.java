/*
 * This file is part of HealthIndicator <https://github.com/NathanNr/HealthIndicator>.
 * Copyright (C) 2017 NathanNr
 *
 * HealthIndicator is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of  MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.nathannr.healthindicator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class HealthIndicatorCommand implements CommandExecutor, TabCompleter {

	File file = new File("plugins/HealthIndicator/language.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

	// TODO: Update Checker

	private HealthIndicator plugin;

	public HealthIndicatorCommand(HealthIndicator plugin) {
		this.plugin = plugin;
		plugin.getCommand("healthindicator").setExecutor(this);
		plugin.getCommand("healthindicator").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 1) {
			sendInfo(sender);
		} else {
			if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
				reloadPlugin(sender);
			}
		}
		return true;
	}

	String rlwarning = " §c§lThis command works at the moment ONLY for the language file! Please use '/rl' of Bukkit to reload the enabled worlds and the other options of the config file!§r";
	String crlwarning = " This command works at the moment ONLY for the language file!";

	public void reloadPlugin(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("healthindicator.reload")) {
				p.sendMessage(plugin.prefix + "§aReloading HealthIndicator...§r");
				System.out.println(
						plugin.cprefix + p.getName() + " (" + p.getUniqueId() + "): Reloading HealthIndicator...");
				plugin.onDisable();
				plugin.onEnable();
				p.sendMessage(plugin.prefix + "§aReload complete!" + rlwarning + "§r");
				System.out.println(
						plugin.cprefix + p.getName() + " (" + p.getUniqueId() + "): Reload complete!" + crlwarning);

			} else {
				System.out.println(plugin.cprefix + p.getName() + " (" + p.getUniqueId()
						+ ") tried to reload HealthIndicator: Permission 'healthindicator.reload' is missing!");
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						plugin.prefix + cfg.getString("HealthIndicator.Language.NoPermission") + "§r"));
			}
		} else {
			System.out.println(plugin.cprefix + "Reloading HealthIndicator...");
			plugin.onDisable();
			plugin.onEnable();
			System.out.println(plugin.cprefix + "Reload complete!" + crlwarning);
		}
	}

	public void sendInfo(CommandSender sender) {
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "HealthIndicator plugin version " + plugin.getDescription().getVersion()
				+ " by Nathan_N" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "More information about the plugin: https://www.spigotmc.org/resources/"
				+ HealthIndicator.resource + "/" + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.BLUE + "Source code: https://github.com/NathanNr/HealthIndicator/" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/healthindicator reload' to reload the plugin." + ChatColor.RESET);
		// playerCheckUpdate(sender);
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
		return new ArrayList<>(0);
	}
}
