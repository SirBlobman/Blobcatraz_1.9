package com.SirBlobman.blobcatraz.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.SirBlobman.blobcatraz.config.ConfigBlobcatraz;
import com.SirBlobman.blobcatraz.config.ConfigDatabase;

public class CommandSpy implements Listener 
{
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e)
	{
		ConfigBlobcatraz.loadConfig();
		List<String> ignore = ConfigBlobcatraz.config.getStringList("commandspy.ignored commands");
		Player p = e.getPlayer();
		String c = e.getMessage().toLowerCase();
		for(Player p2 : Bukkit.getServer().getOnlinePlayers())
		{
			boolean canSpy = ConfigDatabase.getCanSpy(p2);
			if(canSpy)
			{
				for(String s : ignore)
				{
					String name1 = p.getName();
					String name2 = p2.getName();
					String ignored = s.toLowerCase();
					if(c.startsWith(ignored)) return;
					if(name1.equalsIgnoreCase(name2)) return;
					p2.sendMessage("�7�l" + p.getName() + ":�7 " + e.getMessage());
					break;
				}
			}
		}
	}
}