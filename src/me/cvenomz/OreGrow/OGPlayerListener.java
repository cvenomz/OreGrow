package me.cvenomz.OreGrow;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class OGPlayerListener extends PlayerListener {
	
	private OreGrow oreGrow;
	
	public OGPlayerListener(OreGrow og)
	{
		oreGrow = og;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Block block = e.getClickedBlock();
		if (block != null && (block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE))
		{
			oreGrow.addFurnace(block);
		}
	}

}
