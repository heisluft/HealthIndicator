package com.github.nathannr.healthindicator;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class HICommand implements CommandExecutor {

	File file = new File("plugins/HealthIndicator/language.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	//TODO: Update Checker
	
	private HealthIndicator plugin;
	public HICommand(HealthIndicator plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length != 1) {
			plugin.sendInfo(sender);
		} else {
			if(args[0].equalsIgnoreCase("reload") || args[1].equalsIgnoreCase("rl")) {
				reloadPlugin(sender);
			}
		}
		return true;
	}
	
	String rlwarning = " §c§lThis command works at the moment ONLY for the language file! Please use '/rl' of Bukkit to reload the enabled worlds and the other options of the config file!§r";
	String crlwarning = " This command works at the moment ONLY for the language file!";
	
	public void reloadPlugin(CommandSender sender) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("healthindicator.reload")) {
				p.sendMessage(plugin.prefix + "§aReloading HealthIndicator...§r");
				System.out.println(plugin.cprefix + p.getName() + " (" + p.getUniqueId() + "): Reloading HealthIndicator...");
				plugin.onDisable();
				plugin.onEnable();
				p.sendMessage(plugin.prefix + "§aReload complete!" + rlwarning + "§r");
				System.out.println(plugin.cprefix + p.getName() + " (" + p.getUniqueId() + "): Reload complete!" + crlwarning);
				
			} else {
				System.out.println(plugin.cprefix + p.getName() + " (" + p.getUniqueId() + ") tried to reload HealthIndicator: Permission 'healthindicator.reload' is missing!");
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + cfg.getString("HealthIndicator.Language.NoPermission") + "§r"));
			}
		} else {
			System.out.println(plugin.cprefix + "Reloading HealthIndicator...");
			plugin.onDisable();
			plugin.onEnable();
			System.out.println(plugin.cprefix + "Reload complete!" + crlwarning);
		}
	}
}
