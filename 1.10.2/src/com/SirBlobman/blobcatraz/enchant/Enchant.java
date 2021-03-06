/**
 * 
 */
package com.SirBlobman.blobcatraz.enchant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Enchant
{
	/**
	 * Has a chance to turn Zombie Villagers back into Villagers
	 */
	HEAL_ZOMBIE("�7Cure"),
	/**
	 * Uses enderpearls in your inventory instead of arrows
	 */
	ENDER("�0Ender"),
	/**
	 * Creates an explosion when right-clicked
	 */
	EXPLODE("�4�lExplosive"),
	/**
	 * Spawns fireballs when right-clicked
	 */
	FIREBALL("�7Fireball"),
	/**
	 * Makes an entity glow when it is hit
	 */
	GLOW("�a�lSpectral"),
	/**
	 * Has a chance to make your enemy drop their armor
	 */
	LOOSE("�7Loosen Up"),
	/**
	 * Has a chance to do extra damage at night
	 */
	NIGHT_WOLF("�8Night �7Wolf"),
	/**
	 * Makes an entity instantly die when it is right-clicked
	 */
	ONE_SHOT("�7Insta Kill"),
	/**
	 * Has a chance to poison the enemy
	 */
	POISON("�2Poison Ivy"),
	/**
	 * Makes an entity fly up into the air when damaged
	 */
	FLOAT("�7Levitate"),
	/**
	 * Takes health and gives it to the damager
	 */
	DRAIN_HP("�8�lLife Steal"),
	/**
	 * Takes XP and gives it to the damager
	 */
	DRAIN_XP("�6XP Steal"),
	/**
	 * Slows down your enemy!
	 * <br/>Also has a chance to spawn a tiny slime
	 */
	SLOW("�aSlimy"),
	/**
	 * Chance to summon lightning on your enemy
	 */
	STRIKE("�7Thunder �6Strike"),
	/**
	 * When you have a full set of armor you get Strength II
	 */
	STRONG("�7Strength"),
	/**
	 * Gives an entity the Wither Effect when hit
	 */
	WITHER("�8Wither");
	
	private final String name;
	
	Enchant(String name) {this.name = name;}
	
	/**
	 * Get the enchant's name. Example: <font color=green><b>Slimy</b></font> instead of SLOW
	 * @return Name of the custom enchanment
	 */
	public String getName() {return name;}
	
	public static List<String> validEnchants()
	{
		List<String> valid = new ArrayList<String>();
		for(Enchant e : values()) valid.add(e.name());
		Collections.sort(valid);
		return valid;
	}
}