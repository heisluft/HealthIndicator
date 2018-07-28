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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HealthIndicator extends JavaPlugin implements Listener {

	static final String cprefix = "[HealthIndicator] ";
	static final String prefix = "§8[§e§lHealthIndicator§8] §r";

	private Map<UUID, EntityArmorStand> hds = new HashMap<>();

	private EntityArmorStand createArmorStand(Player p) {
		int health = (int) p.getHealth();
		Location loc = p.getLocation();

		EntityArmorStand as = new EntityArmorStand(((CraftWorld) p.getWorld()).getHandle());
		as.setLocation(loc.getX(), loc.getY() + 2, loc.getZ(), 0, 0);
		StringBuilder bb = new StringBuilder(ChatColor.translateAlternateColorCodes('&', "&4"));
		for(int i = 0; i < health; i++) bb.append('\u2764');
		as.setCustomName(bb.toString());
		as.setInvisible(true);
		as.setCustomNameVisible(true);
		as.setMarker(true);

		hds.put(p.getUniqueId(), as);

		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(as);
		Bukkit.getOnlinePlayers().forEach((pl) -> ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet));
		return as;
	}

	private void renderArmorStand(Player p) {
		EntityArmorStand as = hds.get(p.getUniqueId());
		if(as == null) as = createArmorStand(p);
		double health = p.getHealth();
		Location loc = p.getLocation();
		as.setLocation(loc.getX(), loc.getY() + 2, loc.getZ(), 0, 0);
		StringBuilder bb = new StringBuilder(ChatColor.translateAlternateColorCodes('&', "&4"));
		for(int i = 0; i < (int) health; i++) bb.append('\u2764');
		if(health - (int) health != 0) bb.append(ChatColor.translateAlternateColorCodes('&', "&c\u2764"));
		as.setCustomName(bb.toString());

		PacketPlayOutEntityTeleport pp = new PacketPlayOutEntityTeleport(as);
		Bukkit.getOnlinePlayers().forEach((pl) -> ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(pp));
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		createArmorStand(e.getPlayer());
	}

	@EventHandler
	public void onChangeDimension(PlayerChangedWorldEvent e) {
		EntityArmorStand as = hds.get(e.getPlayer().getUniqueId());
		hds.put(e.getPlayer().getUniqueId(), createArmorStand(e.getPlayer()));
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(as.getId());
		Bukkit.getOnlinePlayers().forEach((pl) -> ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet));
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		hds.put(e.getPlayer().getUniqueId(), null);
	}

	static int resource = 30196;

	private File file = new File("plugins/HealthIndicator/config.yml");
	private FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

	@Override
	public void onEnable() {
		Config.initConfig();
		Config.initLang();
		Bukkit.getPluginManager().registerEvents(this, this);
		new HealthIndicatorCommand(this);

		getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
			for(Player p : Bukkit.getOnlinePlayers())
				if(cfg.getList("HealthIndicator.Worlds").contains(p.getWorld().getName())) renderArmorStand(p);
		}, 1L, 1L);
	}
}
