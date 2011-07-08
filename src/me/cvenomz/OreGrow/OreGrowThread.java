package me.cvenomz.OreGrow;

import java.util.logging.Logger;

public class OreGrowThread implements Runnable{

	private MysqlManager database;
	private Logger log;
	
	public OreGrowThread(MysqlManager mm)
	{
	    database = mm;
	    log = Logger.getLogger("Minecraft");
	}

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            database.resetConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.severe("[OreGrow] Could not reset database connection");
            e.printStackTrace();
        }
    }
	
	

}
