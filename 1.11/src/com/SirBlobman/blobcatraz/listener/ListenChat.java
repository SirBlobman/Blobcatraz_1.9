package com.SirBlobman.blobcatraz.listener;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.SirBlobman.blobcatraz.config.ConfigBlobcatraz;
import com.SirBlobman.blobcatraz.utility.PlayerUtil;
import com.SirBlobman.blobcatraz.utility.Util;

public class ListenChat implements Listener
{
	private YamlConfiguration config = ConfigBlobcatraz.load();
	private boolean mute = config.getBoolean("chat.disabled");
	private boolean emoj = config.getBoolean("chat.emojis");
	private boolean ping = config.getBoolean("chat.ping");
	
	@EventHandler
	public void mute(AsyncPlayerChatEvent e)
	{
		if(e.isCancelled()) return;
		
		Player p = e.getPlayer();
		if(mute)
		{
			boolean b1 = p.hasPermission("blobcatraz.mute.bypass");
			if(!b1)
			{
				String msg = Util.prefix + Util.option("command.mute.muted");
				PlayerUtil.action1_11(p, msg);
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void ping(AsyncPlayerChatEvent e)
	{
		if(e.isCancelled()) return;
		
		Player p = e.getPlayer();
		if(ping)
		{
			String msg = e.getMessage().toLowerCase();
			Collection<? extends Player> online = Bukkit.getOnlinePlayers();
			for(Player on : online)
			{
				String n1 = p.getName().toLowerCase();
				String n2 = on.getName().toLowerCase();
				String disp = Util.uncolor(on.getDisplayName()).toLowerCase();
				
				boolean b1 = !n1.equals(n2);
				boolean b2 = msg.contains(n2);
				boolean b3 = msg.contains(disp);
				boolean b4 = (b2 || b3);
				boolean b5 = (b1 && b4);
				if(b5) PlayerUtil.ping(on);
			}
		}
	}
	
	@EventHandler
	public void emojis(AsyncPlayerChatEvent e)
	{
		if(e.isCancelled()) return;
		if(emoj)
		{
			String msg = e.getMessage();
			msg = Util.format(msg);
			e.setMessage(msg);
		}
	}
}