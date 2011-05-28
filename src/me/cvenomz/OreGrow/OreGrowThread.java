package me.cvenomz.OreGrow;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

public class OreGrowThread implements Runnable{

	private OreGrow oreGrow;
	
	public OreGrowThread(OreGrow og)
	{
		oreGrow = og;
	}
	@Override
	public void run() {
		List<Block> list = oreGrow.getFurnaces();
		//CraftFurnace f;
		Furnace f;
		int cur,prev;
		for (Block block : list)
		{
			//f = new CraftFurnace(block);
			f = (Furnace)block.getState();
			ItemStack item = f.getInventory().getItem(2);
			if (item.getType() == Material.COBBLESTONE)
			{
				//oreGrow.log.info(item.toString() + "  -  " + item.getAmount());
				cur = item.getAmount();
				prev = oreGrow.getFurnaceValue(oreGrow.blockToString(block));
				if (cur > prev)
				{
					grow(block, cur);
				}
				oreGrow.setFurnaceValue(oreGrow.blockToString(block), cur);
			}
		}
	}
	
	private void grow(Block block, int amount)
	{
		Block root = null;
		if (block.getRelative(BlockFace.SOUTH).getType().compareTo(Material.REDSTONE_WIRE) == 0)
		{
			root = block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH);
		}
		if (block.getRelative(BlockFace.WEST).getType().compareTo(Material.REDSTONE_WIRE) == 0)
		{
			root = block.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST);
		}
		if (block.getRelative(BlockFace.NORTH).getType().compareTo(Material.REDSTONE_WIRE) == 0)
		{
			root = block.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH);
		}
		if (block.getRelative(BlockFace.EAST).getType().compareTo(Material.REDSTONE_WIRE) == 0)
		{
			root = block.getRelative(BlockFace.EAST).getRelative(BlockFace.EAST);
		}
		
		if (root == null)
			return;
		if (root.getType() == Material.IRON_BLOCK)
		{
			if (amount % 2 == 0)
			{
				addOre(root);
			}
		}
		if (root.getType() == Material.GOLD_BLOCK)
		{
			if (amount % 4 == 0)
			{
				addOre(root);
			}
		}
		if (root.getType() == Material.DIAMOND_BLOCK)
		{
			if (amount % 8 == 0)
			{
				addOre(root);
			}
		}
	}
	
	private void addOre(Block root)
	{
		Material targetMaterial = null;
		switch (root.getType())
		{
		case IRON_BLOCK : targetMaterial = Material.IRON_ORE;break;
		case GOLD_BLOCK : targetMaterial = Material.GOLD_ORE;break;
		case DIAMOND_BLOCK : targetMaterial = Material.DIAMOND_ORE;break;
		}
		
		int x,z;
		int rx,ry,rz;
		World world = root.getWorld();
		rx = root.getX();
		ry = root.getY();
		rz = root.getZ();
		for (int i=0; i<3; i++)
		{
			x = (int) (((Math.random() * 2.0) + 1.0) * (Math.pow(-1,((int)(Math.random()*10)))));
			z = (int) (((Math.random() * 2.0) + 1.0) * (Math.pow(-1,((int)(Math.random()*10)))));
			if (world.getBlockAt(rx+x, ry+i+1, rz+z).getType() == Material.AIR)
			{
				world.getBlockAt(rx, ry+i+1, rz).setType(Material.GLOWSTONE);
				world.getBlockAt(rx+x, ry+i+1, rz+z).setType(targetMaterial);
				i=3; //break from for loop
			}
		}
	}

}
