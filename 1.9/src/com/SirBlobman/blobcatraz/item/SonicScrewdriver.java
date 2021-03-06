package com.SirBlobman.blobcatraz.item;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.SirBlobman.blobcatraz.Blobcatraz;
import com.SirBlobman.blobcatraz.Util;

@SuppressWarnings({"deprecation", "unused"})
public class SonicScrewdriver implements Listener 
{
	private Blobcatraz plugin;
	
	public static ItemStack sonic()
	{
		ItemStack sonic = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = sonic.getItemMeta();
		
		meta.setDisplayName("�fSonic Screwdriver");
		sonic.setItemMeta(meta);
		
		return sonic;
	}
	
	@EventHandler
	public void onSonicUse(PlayerInteractEvent e) throws Exception 
	{
		ItemStack sonic = sonic();
		
		if (!e.getPlayer().hasPermission("blobcatraz.sonic.use")) 
		{
			return;
		}
		if (!e.getPlayer().getItemInHand().equals(sonic)) 
		{
			return;
		}
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) 
		{
			Block b = e.getClickedBlock();
			Block above_b = b.getRelative(BlockFace.UP, 1);
			Player p = e.getPlayer();
			Location l = p.getLocation();
// Sonic Devices can open iron doors
			if (b.getType() == Material.IRON_DOOR_BLOCK) 
			{
				if (b.getData() >= 8) 
				{
					b = b.getRelative(BlockFace.DOWN);
				}
				if (b.getData() < 4) 
				{
					b.setData((byte) (b.getData() + 4));
					b.getWorld().playEffect(b.getLocation(), Effect.DOOR_TOGGLE, 0);
				} 
				else 
				{
					b.setData((byte) (b.getData() - 4));
					b.getWorld().playEffect(b.getLocation(), Effect.DOOR_TOGGLE, 0);
				}
				Util.soundSonic(p);
			}
// They can also open iron trapdoors
			if (b.getType() == Material.IRON_TRAPDOOR) 
			{
				if ((b.getData() < 4) || ((b.getData() > 7) && (b.getData() < 12))) 
				{
					b.setData((byte) (b.getData() + 4));
					b.getWorld().playEffect(b.getLocation(), Effect.DOOR_TOGGLE, 0);
				} 
				else 
				{
					b.setData((byte) (b.getData() - 4));
					b.getWorld().playEffect(b.getLocation(), Effect.DOOR_TOGGLE, 0);
				}
				Util.soundSonic(p);
			}
//They can also turn off unpowered (glitched) lamps
			if(b.getType() == Material.REDSTONE_LAMP_ON)
			{
				b.setType(Material.REDSTONE_LAMP_OFF);
				Util.soundSonic(p);
			}
//Changes tnt to primed
			if(b.getType() == Material.TNT)
			{
				b.setType(Material.AIR);
				p.playSound(l, Sound.ENTITY_TNT_PRIMED, 1000, 1);
				TNTPrimed t = b.getWorld().spawn(new Location(b.getWorld(), b.getX() + 0.5, b.getY() + 0.5,b.getZ() + 0.5), TNTPrimed.class);
				Util.soundSonic(p);
			}
//In one episode, it even broke a ladder
			if(b.getType() == Material.LADDER)
			{
				b.breakNaturally();
				Util.soundSonic(p);
			}
//It can also break glass
			if(b.getType() == Material.GLASS || b.getType() == Material.THIN_GLASS || b.getType() == Material.STAINED_GLASS || b.getType() == Material.STAINED_GLASS_PANE)
			{
				ItemStack type = new ItemStack(b.getType());;
				b.breakNaturally();
				p.getInventory().addItem(type);
				Util.soundSonic(p);
			}
//It can attempt to light a portal
			if(b.getType() == Material.OBSIDIAN && above_b.getType() == Material.AIR)
			{
				above_b.setType(Material.FIRE);
				Util.soundSonic(p);
			}
//Breaks webs instantly
			if(b.getType() == Material.WEB)
			{
				b.breakNaturally();
				Util.soundSonic(p);
			}
		}
	}
}
