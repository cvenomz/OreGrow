package me.cvenomz.OreGrow;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class OGPlayerListener extends PlayerListener {
	
	private OreGrowHandler handler;
	
	public OGPlayerListener(OreGrowHandler ogh)
	{
		handler = ogh;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Block block = e.getClickedBlock();
		if (block != null && (block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE))
		{
			handler.addFurnace(block);
		}
	}

}
