package com.SirBlobman.blobcatraz.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.SirBlobman.blobcatraz.Util;
import com.SirBlobman.blobcatraz.config.ConfigShop;

public class CommandWorth implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args)
	{
		if(label.equals("worth"))
		{
			if(args.length == 0)
			{
				Player p = (Player)cs;
				if(p == null)
				{
					cs.sendMessage(Util.commandExecutorNotAPlayer);
					return true;
				}

				PlayerInventory pi = p.getInventory();
				ItemStack held = pi.getItemInHand();
				int hamount = held.getAmount();
				Material heldm = held.getType();

				p.sendMessage(Util.blobcatraz + "�5" + hamount + "�r of �5" + heldm.toString() + "�r can be sold for �5$" + ConfigShop.getSellPrice(heldm) * hamount);
				return true;
			}
			if(args.length == 1)
			{
				Material m = Material.getMaterial(args[0]);
				if(m == null)
				{
					cs.sendMessage(Util.blobcatraz + "�5" + args[0] + "�r is not a valid item");
					return false;
				}

				cs.sendMessage(Util.blobcatraz + "�51�r of �5" + args[0] + "�r can be sold for �5$" + ConfigShop.getSellPrice(m));
				return true;
			}
			if(args.length == 2)
			{
				Material m = Material.getMaterial(args[0]);
				int amount = Integer.parseInt(args[1]);
				if(m == null)
				{
					cs.sendMessage(Util.blobcatraz + "�5" + args[0] + "�r is not a valid item");
					return false;
				}

				cs.sendMessage(Util.blobcatraz + "�5" + amount + "�r of �5" + args[0] + "�r can be sold for �5$" + ConfigShop.getSellPrice(m) * amount);
				return true;
			}
		}
		if(label.equals("setworth"))
		{
			if(args.length == 1)
			{
				if(!(cs instanceof Player))
				{
					cs.sendMessage(Util.commandExecutorNotAPlayer);
					return true;
				}
				Player p = (Player) cs;

				PlayerInventory pi = p.getInventory();
				ItemStack held = pi.getItemInHand();
				Material heldm = held.getType();
				double price = Double.parseDouble(args[0]);

				ConfigShop.setSellPrice(heldm, price);
				p.sendMessage(Util.blobcatraz + "The sell price of �5" + heldm.toString() + "�r is now �5$" + price);
				return true;
			}
			if(args.length == 2)
			{
				Material m = Material.getMaterial(args[0]);
				if(m == null)
				{
					cs.sendMessage(Util.blobcatraz + "�5" + args[0] + "�r is not a valid item");
					return false;
				}
				double price = Double.parseDouble(args[1]);

				ConfigShop.setSellPrice(m, price);
				cs.sendMessage(Util.blobcatraz + "The sell price of �5" + m.toString() + "�r is now �5$" + price);
			}
		}

		return false;
	}
}