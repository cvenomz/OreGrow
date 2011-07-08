package me.cvenomz.OreGrow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.block.Block;

public class MysqlManager {

    private String username;
    private String password;
    private String host;
    private String databaseName;
    private String tableName;
    private String url;
    private Connection conn;
    
    public MysqlManager(String host, String databaseName, String username, String password)
    {
        this.host = host;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
        
        this.tableName = "OreGrow";
    }
    
    public synchronized void initialize() throws Exception
    {
        establishConnection();
        CheckTable();
    }
    
    private synchronized void establishConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
        url = "jdbc:mysql://"+host+"/"+databaseName;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection(url, username, password);
        //pluginRef.debugMessage("Connection attempt to database -- done");
    }
    
    public synchronized void closeConnection() throws SQLException
    {
        conn.close();
        //pluginRef.debugMessage("Connection close attempt -- done");
    }
    
    public synchronized void resetConnection() throws Exception
    {
        closeConnection();
        establishConnection();
    }
    
    private synchronized boolean tableExists()throws Exception
    {
        Statement s = conn.createStatement();
        s.executeQuery("SHOW TABLES");
        ResultSet rs = s.getResultSet();
        boolean ret = false;
        while (rs.next())
        {
            if (rs.getString(1).equalsIgnoreCase(tableName))
                ret = true;
        }
        return ret;
    }
    
    private synchronized void CheckTable()throws Exception
    {
        Statement s = conn.createStatement();
        if (!tableExists())
            s.executeUpdate("CREATE TABLE " + tableName + " (id INT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT, PRIMARY KEY (id), world varchar(50), x INT NOT NULL, y INT NOT NULL, z INT NOT NULL, bitch INT NOT NULL )");
    }
    
    public synchronized int getDatabaseBitch(String world, int x, int y, int z) throws SQLException
    {
        Statement s = conn.createStatement();
        s.executeQuery("SELECT bitch FROM " + tableName + " WHERE world='"+world + "' AND x="+x + " AND y="+y + " AND z="+z);
        ResultSet rs = s.getResultSet();
        int bitch = -1;
        while (rs.next())
        {
            bitch = rs.getInt("bitch");
        }
        return bitch;
    }
    
    public synchronized void addFurnace(Block b) throws SQLException
    {
        Statement s = conn.createStatement();
        s.executeUpdate("INSERT INTO " + tableName + " (world, x, y, z, bitch) VALUES ('"+b.getWorld().getName()+"',"+b.getX()+","+b.getY()+","+b.getZ()+","+0+")");
    }
    
    public synchronized void setDatabaseBitch(Block b, int value) throws SQLException
    {
        Statement s = conn.createStatement();
        s.executeUpdate("UPDATE " + tableName + " SET bitch="+value + " WHERE world='"+b.getWorld().getName() + "' AND x="+b.getX() + " AND y="+b.getY() + " AND z="+b.getZ());
    }
    
    public synchronized void removeFurnace(Block b) throws SQLException
    {
        Statement s = conn.createStatement();
        s.executeUpdate("DELETE FROM " + tableName + " WHERE world='"+b.getWorld().getName() + "' AND x="+b.getX() + " AND y="+b.getY() + " AND z="+b.getZ());
    }
    
    public synchronized ResultSet getFurnaces() throws SQLException
    {
        Statement s = conn.createStatement();
        s.executeQuery("SELECT * FROM " + tableName);
        ResultSet rs = s.getResultSet();
        return rs;
    }
    
}
