package com.github.nathannr.healthindicator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.yaml.snakeyaml.Yaml;

public class Config {

	public static void initConfig() {
		File file = new File("plugins/HealthIndicator/config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		
		
		cfg.options().header("Config file of HealthIndicator Spigot plugin by Nathan_N");
		cfg.addDefault("HealthIndicator.HealthDescription", "&4\u2764");
		cfg.addDefault("HealthIndicator.UseHealthPointsInsteadOfHearts", false);
		List<String> worlds = cfg.getStringList("HealthIndicator.Worlds");
		if(cfg.getList("HealthIndicator.Worlds") == null) {
			for(World w : Bukkit.getWorlds()) {
				worlds.add(w.getName());
			}
			cfg.set("HealthIndicator.Worlds", worlds);
		} else {
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
		cfg.addDefault("HealthIndicator.Language.NoPermission", "&cSorry, but you don't have permission to execute this command!&r");
		
		cfg.options().copyDefaults(true);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
