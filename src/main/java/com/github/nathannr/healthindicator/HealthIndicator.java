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
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class HealthIndicator extends JavaPlugin {

	public String cprefix = "[HealthIndicator] ";
	public String prefix = "§8[§e§lHealthIndicator§8] §r";

	static int resource = 30196;

	Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	Objective obj = board.registerNewObjective("HealthIndicator", "Nathan_N");

	Scoreboard boardn = Bukkit.getScoreboardManager().getNewScoreboard();
	Objective objn = boardn.registerNewObjective("HealthIndicatorN", "Nathan_NNNN");

	File file = new File("plugins/HealthIndicator/config.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

	@Override
	public void onEnable() {
		Config.initConfig();
		Config.initLang();
		initEvents();
		initCmds();
		createIndicator();
		runIndicator();
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		System.out.println(
				cprefix + "HealthIndicator by Nathan_N version " + this.getDescription().getVersion() + " enabled.");
	}

	@Override
	public void onDisable() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setScoreboard(boardn);
		}
		System.out.println(
				cprefix + "HealthIndicator by Nathan_N version " + this.getDescription().getVersion() + " disabled.");
	}

	public void initEvents() {
		// PluginManager pm = Bukkit.getPluginManager();
	}

	public void initCmds() {
		getCommand("healthindicator").setExecutor(new HealthIndicatorCommand(this));
	}

	public void createIndicator() {

		board.getObjectivesByCriteria(Criterias.HEALTH);
		try {
			obj.setDisplayName(
					ChatColor.translateAlternateColorCodes('&', cfg.getString("HealthIndicator.HealthDescription")));
		} catch (Exception e) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload"); // Bug?
		}
		obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}

	public void runIndicator() {
		this.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (cfg.getList("HealthIndicator.Worlds").contains(all.getWorld().getName())) {

						@SuppressWarnings("deprecation")
						Score score = obj.getScore(all);
						int scoreint = (int) all.getHealth();
						if (!cfg.getBoolean("HealthIndicator.UseHealthPointsInsteadOfHearts")) {
							scoreint = scoreint / 2;
						}
						score.setScore(scoreint);
						all.setScoreboard(board);
					} else {
						all.setScoreboard(boardn);
					}
				}

			}
		}, 1L, 10L);
	}

	public void sendInfo(CommandSender sender) {
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "HealthIndicator plugin version " + this.getDescription().getVersion()
				+ " by Nathan_N" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "More information about the plugin: https://www.spigotmc.org/resources/"
				+ resource + "/" + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.BLUE + "Source code: https://github.com/NathanNr/HealthIndicator/" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/healthindicator reload' to reload the plugin." + ChatColor.RESET);
		// playerCheckUpdate(sender);
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
	}

}
