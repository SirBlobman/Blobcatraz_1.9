package com.SirBlobman.blobcatraz.config;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.SirBlobman.blobcatraz.Blobcatraz;
import com.SirBlobman.blobcatraz.Util;

public class ConfigDatabase 
{
	static HashMap<UUID, String> names = new HashMap<UUID, String>();
	static HashMap<UUID, Boolean> banned = new HashMap<UUID, Boolean>();
	static HashMap<UUID, String> bannedReason = new HashMap<UUID, String>();
	static HashMap<UUID, Long> bannedLength = new HashMap<UUID, Long>();
	static HashMap<UUID, Double> balance = new HashMap<UUID, Double>();
	static HashMap<UUID, Boolean> afkstatus = new HashMap<UUID, Boolean>();

	private static final File databaseFile = new File(Blobcatraz.instance.getDataFolder(), "database.yml");
	public static FileConfiguration databaseConfig = YamlConfiguration.loadConfiguration(databaseFile);

	public static void saveDatabase()
	{
		if(!databaseFile.exists())
		{
			try
			{
				databaseFile.createNewFile();
			}
			catch (Exception ex)
			{
				Util.print("Could not create database.yml");
				ex.printStackTrace();
				return;
			}
		}

		for(Entry<UUID, String> e : names.entrySet())
		{
			databaseConfig.set("players." + e.getKey() + ".lastName", e.getValue());
		}
		for(Entry<UUID, Long> e : bannedLength.entrySet())
		{
			databaseConfig.set("players." + e.getKey() + ".bannedLength", e.getValue());
		}
		for(Entry<UUID, String> e : bannedReason.entrySet())
		{
			databaseConfig.set("players." + e.getKey() + ".bannedReason", e.getValue());
		}
		for(Entry<UUID, Boolean> e : banned.entrySet())
		{
			databaseConfig.set("players." + e.getKey() + ".banned", e.getValue());
		}
		for(Entry<UUID, Double> e : balance.entrySet())
		{
			databaseConfig.set("players." + e.getKey() + ".balance", e.getValue());
		}
		for(Entry<UUID, Boolean> e : afkstatus.entrySet())
		{
			databaseConfig.set("players." + e.getKey() + ".afk", e.getValue());
		}

		try 
		{
			databaseConfig.save(databaseFile);
		} 
		catch (Exception ex) 
		{
			Util.print("Failed to save database.yml");
			ex.printStackTrace();
			return;
		}
	}

	public static void loadDatabase()
	{
		try
		{
			databaseConfig.load(databaseFile);
		}
		catch (Exception ex)
		{
			Util.print("Failed to load database.yml, attempting to create");
			saveDatabase();
		}

		try
		{
			for(String key : databaseConfig.getConfigurationSection("players").getKeys(false))
			{
				UUID uuid = UUID.fromString(key);

				names.put(uuid, databaseConfig.getString("players." + key + ".lastName"));
				banned.put(uuid, databaseConfig.getBoolean("players." + key + ".banned"));
				bannedReason.put(uuid, databaseConfig.getString("players." + key + ".bannedReason"));
				bannedLength.put(uuid, databaseConfig.getLong("players." + key  + ".bannedLength"));
				balance.put(uuid, databaseConfig.getDouble("players." + key + ".balance"));
				afkstatus.put(uuid, databaseConfig.getBoolean("players." + key + ".afk"));
			}
		}
		catch (Exception ex)
		{
			writeDefault();
			Util.print("database.yml is empty or null");
			ex.printStackTrace();
		}
	}

	public static void writeDefault()
	{
		saveDatabase();
		UUID sirblobman = UUID.fromString("5ba03c6c-ad4c-4475-8ec9-8bc8a15a9ebe");
		names.put(sirblobman, "SirBlobman");
		banned.put(sirblobman, false);
		bannedReason.put(sirblobman, null);
		bannedLength.put(sirblobman, null);
		balance.put(sirblobman, 0.0);
		afkstatus.put(sirblobman, false);
		saveDatabase();
	}

	public static void writeDefaults(UUID uuid)
	{
		saveDatabase();
		if(uuid == null) return;
		Player p = Bukkit.getPlayer(uuid);
		if(p == null) return;
		String name = p.getName();

		names.put(uuid, name);
		banned.put(uuid, false);
		bannedReason.put(uuid, null);
		bannedLength.put(uuid, null);
		balance.put(uuid, 0.0);
		afkstatus.put(uuid, false);
		saveDatabase();
	}

	public static void clearBalances()
	{
		for(String key : databaseConfig.getConfigurationSection("players").getKeys(false))
		{
			databaseConfig.set("players." + key + ".balance", null);
		}
		balance.clear();
		saveDatabase();
		loadDatabase();
	}

	public static double getPlayerBalance(UUID uuid)
	{
		loadDatabase();
		if(uuid == null || !balance.containsKey(uuid))
		{
			return 0;
		}

		double bal = balance.get(uuid);

		return bal;
	}


	public static void setPlayerBalance(UUID uuid, double amount)
	{
		loadDatabase();
		if(uuid == null)
		{
			return;
		}

		balance.put(uuid, amount);
		saveDatabase();
	}

	public static void addToPlayerBalance(UUID uuid, double amount)
	{
		loadDatabase();
		if(uuid == null) return;
		double newbalance = balance.get(uuid) + amount;
		setPlayerBalance(uuid, newbalance);
	}

	public static void subtractFromPlayerBalance(UUID uuid, double amount)
	{
		loadDatabase();
		if(uuid == null) return;
		double newbalance = balance.get(uuid) - amount;
		if(newbalance < 0) newbalance = 0;
		setPlayerBalance(uuid, newbalance);
	}

	public static void resetPlayerBalance(UUID uuid)
	{
		setPlayerBalance(uuid, 0);
	}

	public static boolean isPlayerBanned(UUID uuid)
	{
		if(uuid == null) return false;

		loadDatabase();
		Boolean ban = banned.get(uuid);
		if(ban == null) return false;

		return ban;
	}

	public static String getBanReason(UUID uuid)
	{
		if(uuid == null) return null;
		loadDatabase();
		String r = bannedReason.get(uuid);
		if(r != null) return r;

		return null;
	}

	public static Long getEndOfBan(UUID uuid)
	{
		if(uuid == null) return null;
		loadDatabase();
		Long l = bannedLength.get(uuid);
		if(l == null) return 0L;
		return l;
	}

	public static String getBanLengthFormatted(UUID uuid)
	{
		if(uuid == null) return null;
		long current = System.currentTimeMillis();
		long endOfBan = getEndOfBan(uuid);
		long millis = endOfBan - current;
		System.out.println(GregorianCalendar.getInstance().getTimeInMillis());

		int seconds = 0;
		int minutes = 0;
		int hours = 0;
		int days = 0;
		int weeks = 0;
		int months = 0;
		int years = 0;
		int decades = 0;
		int centuries = 0;

		while(millis > 1000L)
		{
			millis -= 1000L;
			seconds++;
		}
		while(seconds > 60)
		{
			seconds -= 60;
			minutes++;
		}
		while(minutes > 60)
		{
			minutes -= 60;
			hours++;
		}
		while(hours > 24)
		{
			hours -= 24;
			days++;
		}
		while(days > 7)
		{
			days -= 7;
			weeks++;
		}
		while(weeks > 4)
		{
			weeks -= 4;
			months++;
		}
		while(months > 12)
		{
			months -= 12;
			years++;
		}
		while(years > 10)
		{
			years -= 10;
			decades++;
		}
		while(decades > 10)
		{
			decades -= 10;
			centuries++;
		}

		String format = "0 Seconds";

		if(centuries > 0) 
		{
			format = centuries + " Centuries, " + decades + " Decades, " + years + " Years, " + months + " Months, " + weeks + " Weeks, " + days + " Days," + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(decades > 0) 
		{
			format = decades + " Decades, " + years + " Years, " + months + " Months, " + weeks + " Weeks, " + days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(years > 0) 
		{
			format = years + " Years, " + months + " Months, " + weeks + " Weeks, " + days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(months > 0)
		{
			format = months + " Months, " + weeks + " Weeks, " + days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(weeks > 0)
		{
			format = weeks + " Weeks, " + days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(days > 0)
		{
			format = days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(hours > 0)
		{
			format = hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(minutes > 0)
		{
			format = minutes + " Minutes, and " + seconds + " Seconds";
			return format;
		}
		if(seconds > 0) 
		{
			format = seconds + " Seconds";
			return format;
		}

		return format;
	}

	public static void tempban(OfflinePlayer p, Long length, String reason)
	{
		banned.put(p.getUniqueId(), true);
		bannedReason.put(p.getUniqueId(), reason);

		long current = System.currentTimeMillis();
		long millis = length;
		long endOfBan = current + millis;


		bannedLength.put(p.getUniqueId(), Long.valueOf(endOfBan));
		saveDatabase();

		if(p.isOnline())
		{
			Player onlinep = (Player) p;
			onlinep.kickPlayer(Util.blobcatraz + "�4You have been banned:\n�r" + reason + "\n�9" + getBanLengthFormatted(p.getUniqueId()));
		}
	}

	public static void ban(OfflinePlayer p, String reason)
	{
		banned.put(p.getUniqueId(), true);
		bannedReason.put(p.getUniqueId(), reason);
		saveDatabase();

		if(p.isOnline())
		{
			Player onlinep = (Player) p;
			onlinep.kickPlayer(Util.blobcatraz + "�4You have been banned:\n�r" + reason);
		}

	}

	public static void unban(OfflinePlayer p)
	{
		banned.put(p.getUniqueId(), false);
		bannedReason.put(p.getUniqueId(), null);
		bannedLength.put(p.getUniqueId(), null);
		saveDatabase();
	}

	public static boolean getAFKStatus(Player p)
	{
		loadDatabase();

		if(p == null) return false;
		UUID uuid = p.getUniqueId();

		Boolean afk = afkstatus.get(uuid);
		if(afk == null) return false;

		return afk;
	}

	public static void setAFK(Player p)
	{
		loadDatabase();
		if(p == null) return;
		UUID uuid = p.getUniqueId();

		afkstatus.put(uuid, true);
		saveDatabase();
	}

	public static void setNotAFK(Player p)
	{
		loadDatabase();
		if(p == null) return;
		UUID uuid = p.getUniqueId();

		afkstatus.put(uuid, false);
		saveDatabase();
	}
}