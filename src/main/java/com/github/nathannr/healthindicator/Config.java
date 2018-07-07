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
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	public static void initConfig() {
		File file = new File("plugins/HealthIndicator/config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.options().header("Config file of HealthIndicator Spigot plugin by Nathan_N");
		cfg.addDefault("HealthIndicator.HealthDescription", "&4\u2764");
		cfg.addDefault("HealthIndicator.UseHealthPointsInsteadOfHearts", false);
		List<String> worlds = cfg.getStringList("HealthIndicator.Worlds");
		if (cfg.getList("HealthIndicator.Worlds") == null) {
			for (World w : Bukkit.getWorlds()) {
				worlds.add(w.getName());
			}
			cfg.set("HealthIndicator.Worlds", worlds);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload"); //To fix a bug
		}

		cfg.options().copyDefaults(true);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initLang() {
		File file = new File("plugins/HealthIndicator/language.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.options().header("Language file of HealthIndicator Spigot plugin by Nathan_N");
		cfg.addDefault("HealthIndicator.Language.NoPermission",
				"&cSorry, but you don't have permission to execute this command!&r");

		cfg.options().copyDefaults(true);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
