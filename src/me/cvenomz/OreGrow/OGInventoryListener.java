package me.cvenomz.OreGrow;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryListener;

public class OGInventoryListener extends InventoryListener {

    private OreGrowHandler handler;
    private int ironInterval    = 1;
    private int goldInterval    = 2;
    private int diamondInterval = 4;
    
    public OGInventoryListener(OreGrowHandler ogh)
    {
        handler = ogh;
    }
    
    @Override
    public void onFurnaceSmelt(FurnaceSmeltEvent e)
    {
        if (e.getSource().getType() == Material.COAL_ORE)
        {
            int dbBitch = handler.getDatabaseBitch(e.getFurnace());
            handler.setDatabaseBitch(e.getFurnace(), ++dbBitch);
            boolean resetBitch = grow(e.getFurnace(), dbBitch);
            if (resetBitch)
            {
                handler.setDatabaseBitch(e.getFurnace(), 0);
            }
        }
    }
    
    private boolean grow(Block block, int amount)
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
            return false;
        if (root.getType() == Material.IRON_BLOCK)
        {
            if (amount % ironInterval == 0)
            {
                addOre(root);
                return true;
            }
        }
        if (root.getType() == Material.GOLD_BLOCK)
        {
            if (amount % goldInterval == 0)
            {
                addOre(root);
                return true;
            }
        }
        if (root.getType() == Material.DIAMOND_BLOCK)
        {
            if (amount % diamondInterval == 0)
            {
                addOre(root);
                return true;
            }
        }
        
        return false;
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
        
        //int x,z;
        //int rx,ry,rz;
        //World world = root.getWorld();
        //rx = root.getX();
        //ry = root.getY();
        //rz = root.getZ();
        boolean completed = false;
        Block tmp = root,tmp2;
        int height = 1;
        List<Block> availible;
        /*for (int i=0; i<3; i++)
        {
            x = (int) (((Math.random() * 2.0) + 1.0) * (Math.pow(-1,((int)(Math.random()*10)))));
            z = (int) (((Math.random() * 2.0) + 1.0) * (Math.pow(-1,((int)(Math.random()*10)))));
            if (world.getBlockAt(rx+x, ry+i+1, rz+z).getType() == Material.AIR)
            {
                world.getBlockAt(rx, ry+i+1, rz).setType(Material.GLOWSTONE);
                world.getBlockAt(rx+x, ry+i+1, rz+z).setType(targetMaterial);
                i=3; //break from for loop
            }
        }*/
        while (!completed && height < 4)
        {
            tmp = tmp.getFace(BlockFace.UP);
            height++;
            availible = getOpenNeighbors(tmp, targetMaterial);
            if (availible.size() > 0)
            {
                tmp2 = availible.get((int)(Math.random() * availible.size()));
                if (tmp.getType() == Material.AIR)
                    tmp.setType(Material.GLOWSTONE);
                build(tmp2, targetMaterial);
                completed = true;
            }
        }
        
    }
    
    private List<Block> getAirNeighbors(Block block) //gets neighboring (horizontal) blocks that are not occupied
    {
        LinkedList<Block> list = new LinkedList<Block>();
        if (block.getFace(BlockFace.SOUTH).getType() == Material.AIR)
            list.add(block.getFace(BlockFace.SOUTH));
        if (block.getFace(BlockFace.WEST).getType() == Material.AIR)
            list.add(block.getFace(BlockFace.WEST));
        if (block.getFace(BlockFace.NORTH).getType() == Material.AIR)
            list.add(block.getFace(BlockFace.NORTH));
        if (block.getFace(BlockFace.EAST).getType() == Material.AIR)
            list.add(block.getFace(BlockFace.EAST));
        
        return list;
    }
    
    private List<Block> getOpenNeighbors(Block block, Material target) //returns neighboring blocks that are not fully surrounded (horizontally)
    {
        LinkedList<Block> list = new LinkedList<Block>();
        Block tmp;
        
        tmp = block.getFace(BlockFace.SOUTH);
        if ((tmp.getType() == target || tmp.getType() == Material.AIR) && getAirNeighbors(tmp).size() != 0)
            list.add(tmp);
        tmp = block.getFace(BlockFace.WEST);
        if ((tmp.getType() == target || tmp.getType() == Material.AIR) && getAirNeighbors(tmp).size() != 0)
            list.add(tmp);
        tmp = block.getFace(BlockFace.NORTH);
        if ((tmp.getType() == target || tmp.getType() == Material.AIR) && getAirNeighbors(tmp).size() != 0)
            list.add(tmp);
        tmp = block.getFace(BlockFace.EAST);
        if ((tmp.getType() == target || tmp.getType() == Material.AIR) && getAirNeighbors(tmp).size() != 0)
            list.add(tmp);
        
        return list;
    }
    
    private void build(Block block, Material target)
    {
        if (block.getType() != target)
        {
            block.setType(target);
        }
        else
        {
            List<Block> list = getAirNeighbors(block);
            list.get((int)(Math.random() * list.size())).setType(target);
        }
        
    }
}
