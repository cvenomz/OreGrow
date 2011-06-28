package me.cvenomz.OreGrow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.CoalType;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.Coal;
import org.bukkit.plugin.java.JavaPlugin;

public class OreGrow extends JavaPlugin{

	File mainDirectory;
	//File furnacesFile;
	File configFile;
	Properties config;
	//Properties furnaces = new Properties();
	PlayerListener playerListener;
	OreGrowHandler oreGrowHandler;
	MysqlManager mysqlManager;
	String host, databaseName, username, password;
	Logger log = Logger.getLogger("Minecraft");
	String version = "0.5";
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		try {
            mysqlManager.closeConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.severe("[OreGrow] could not cleanly close MySQL database connection");
            e.printStackTrace();
        }
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		mainDirectory = new File("plugins" + File.separator + "OreGrow");
		config = new Properties();
		
		if (!mainDirectory.exists())
			mainDirectory.mkdir();
		
		try {
			//furnacesFile = new File(mainDirectory.getCanonicalPath() + File.separator + "furnaces.properties");
		    configFile = new File(mainDirectory.getCanonicalPath() + File.separator + "OreGrow.properties");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (!configFile.exists())
		{
			try {
				configFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{
			    
		}
		readConfig();
        mysqlManager = new MysqlManager(host, databaseName, username, password);
        try {
            mysqlManager.initialize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.severe("[OreGrow] version " + version + " could not initialize MySQL database manager");
            e.printStackTrace();
            
        }
        oreGrowHandler = new OreGrowHandler(this, mysqlManager);
		
		playerListener = new OGPlayerListener(oreGrowHandler);
		getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		
		FurnaceRecipe r = new FurnaceRecipe(new ItemStack(Material.COBBLESTONE, 1), Material.COAL_ORE);
		getServer().addRecipe(r);
		ShapelessRecipe s1 = new ShapelessRecipe(new ItemStack(Material.COAL_ORE, 1));
		s1.addIngredient(4, Material.COAL);
		getServer().addRecipe(s1);
		ShapelessRecipe s2 = new ShapelessRecipe(new ItemStack(Material.COAL_ORE, 1));
		Coal coal = new Coal(CoalType.CHARCOAL);
		s2.addIngredient(4, coal);
		getServer().addRecipe(s2);
		
		
		
		OreGrowThread oreGrowThread = new OreGrowThread(this, oreGrowHandler);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, oreGrowThread, 1, 1);
		//log.info(""+taskID);
		
		log.info("[OreGrow] version " + version + " initialized");
		
	}
	
	private void readConfig()
	{
	      try {
            config.load(new FileInputStream(configFile));
            
            host = config.getProperty("host");
            databaseName = config.getProperty("databaseName");
            username = config.getProperty("username");
            password = config.getProperty("password");
            
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}
	
	

}
