
package com.github.nathannr.healthindicator;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Hologram {
	private static final String NMS = "net.minecraft.server.";
	private static final String OBC = "org.bukkit.craftbukkit.";

	private static Field playerConnection;
	private static Constructor<?> packetPlayOutEntityDestroy, packetPlayOutEntityTeleport,
			packetPlayOutSpawnEntityLiving, armorStand;
	private static Method connSendPacket, playerGetHandle, worldGetHandle, assetLocation, assetCustomName,
			assetCustomNameVisible, assetInvisible, assetMarker, asgetId;

	private static boolean init;
	private static String version;

	private static void init() throws ReflectiveOperationException {
		Thread f = new Thread("");
		Runtime.getRuntime().addShutdownHook(f);
		if(init) return;
		final String name = Bukkit.getServer().getClass().getPackage().getName();
		version = name.substring(name.lastIndexOf('.') + 1);

		Class<?> packet = Class.forName(NMS + version + ".Packet");
		Class<?> packetPlayOutSpawnEntityLivingC = Class.forName(NMS + version + ".PacketPlayOutSpawnEntityLiving");
		Class<?> packetPlayOutEntityTeleportC = Class.forName(NMS + version + ".PacketPlayOutEntityTeleport");
		Class<?> packetPlayOutEntityDestroyC = Class.forName(NMS + version + ".PacketPlayOutEntityDestroy");
		Class<?> craftPlayerC = Class.forName(OBC + version + ".entity.CraftPlayer");
		Class<?> craftWorldC = Class.forName(OBC + version + ".CraftWorld");
		Class<?> entityC = Class.forName(NMS + version + ".Entity");
		Class<?> entityLivingC = Class.forName(NMS + version + ".EntityLiving");
		Class<?> entityPlayerC = Class.forName(NMS + version + ".EntityPlayer");
		Class<?> entityArmorStandC = Class.forName(NMS + version + ".EntityArmorStand");
		Class<?> worldC = Class.forName(NMS + version + ".World");

		packetPlayOutSpawnEntityLiving = packetPlayOutSpawnEntityLivingC.getConstructor(entityLivingC);
		packetPlayOutEntityTeleport = packetPlayOutEntityTeleportC.getConstructor(entityC);
		packetPlayOutEntityDestroy = packetPlayOutEntityDestroyC.getConstructor(int.class);
		armorStand = entityArmorStandC.getConstructor(worldC);

		asgetId = entityArmorStandC.getMethod("getId");
		assetCustomName = entityArmorStandC.getMethod("setCustomName", String.class);
		assetLocation = entityArmorStandC.getMethod("setLocation", double.class, double.class, double.class,
				float.class, float.class);
		assetCustomNameVisible = entityArmorStandC.getMethod("setCustomNameVisible", boolean.class);
		assetMarker = entityArmorStandC.getMethod("setMarker", boolean.class);
		assetInvisible = entityArmorStandC.getMethod("setInvisible", boolean.class);

		playerGetHandle = craftPlayerC.getMethod("getHandle");
		worldGetHandle = craftWorldC.getMethod("getHandle");

		playerConnection = entityPlayerC.getField("playerConnection");
		connSendPacket = Class.forName(NMS + version + ".PlayerConnection").getMethod("sendPacket", packet);
		init = true;
	}


}

