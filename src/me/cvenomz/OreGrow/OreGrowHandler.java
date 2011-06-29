package me.cvenomz.OreGrow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class OreGrowHandler {
    
    private MysqlManager database;
    private OreGrow plugin;

    public OreGrowHandler(OreGrow og, MysqlManager mm)
    {
        plugin = og;
        database = mm;
    }
    
    public void addFurnace(Block block)
    {
        int bitch = getDatabaseBitch(block);
        if (bitch < 0)
            try {
                database.addFurnace(block);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
    }
    
    public int getDatabaseBitch(Block b)
    {
        int ret = 0;
        try {
            ret = database.getDatabaseBitch(b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }
    
    public void setDatabaseBitch(Block b, int value)
    {
        try {
            database.setDatabaseBitch(b, value);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public Map<Block,Integer> getFurnaces()
    {
        Map<Block,Integer> map = new HashMap<Block,Integer>();
        Block b;
        try {
            ResultSet rs = database.getFurnaces();
            while (rs.next())
            {
                b = plugin.getServer().getWorld(rs.getString("world")).getBlockAt(rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                if (b.getType() != Material.FURNACE && b.getType() != Material.BURNING_FURNACE)
                    database.removeFurnace(b);
                else
                    map.put(b, rs.getInt("bitch"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;

    }
}
