package me.cvenomz.OreGrow;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

public class OreGrowThread implements Runnable{

	//private OreGrow plugin;
	private OreGrowHandler handler;
	private int ironInterval = 1;
	private int goldInterval = 2;
	private int diamondInterval = 4;
	
	public OreGrowThread(OreGrow og, OreGrowHandler ogh)
	{
		//plugin = og;
	    handler = ogh;
	}

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
	
	

}
