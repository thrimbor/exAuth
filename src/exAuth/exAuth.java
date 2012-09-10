package exAuth;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class exAuth extends JavaPlugin
{
	public static exAuth plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public MyEventListener eventListener;
	public PluginDescriptionFile pdfFile;
	
	public ArrayList<String> authenticated_players = new ArrayList<String>();
	public HashMap<String, ItemStack[]> saved_inventories = new HashMap<String, ItemStack[]>();
	
	public HashMap<String, Boolean> player_state;
	//private Connection con = null;
	private String sql_user;
	private String sql_pass;
	private String sql_url;
	private String sql_table;
	private String sql_userfield;
	private String sql_passfield;
	private String sql_encryption;
	public String join_string;
	private String login_missing_parameter_string;
	
	@Override
	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled.");
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("login"))
		{
			if (sender instanceof Player)
			{
				try
				{
					this.log_in((Player) sender, args[0]);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					((Player) sender).sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + this.login_missing_parameter_string);
				}
				
				return true;
			} else
				this.logger.info(this.pdfFile.getName() + ": The /login command can only be used in game");
		}
		else if (cmd.getName().equalsIgnoreCase("logout"))
		{
			if (sender instanceof Player)
			{
				this.log_out((Player) sender);
				return true;
			} else
				this.logger.info(this.pdfFile.getName() + ": The /login command can only be used in game");
		}
		return false;
	}
	
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		this.pdfFile = this.getDescription();
		
		// create an instance of the jdbc mysql-driver
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e)
		{
			this.logger.info(pdfFile.getName() + ": Could not create jdbc driver instance");
			e.printStackTrace();
			return;
		}
		
		this.join_string = this.getConfig().getString("string.join");
		this.login_missing_parameter_string = this.getConfig().getString("string.login_missing_parameter");
		
		// collect the necessary mysql-data
		String sql_host = this.getConfig().getString("db.host");
		String sql_port = this.getConfig().getString("db.port");
		String sql_db = this.getConfig().getString("db.database");
		this.sql_user = this.getConfig().getString("db.user");
		this.sql_pass = this.getConfig().getString("db.password");
		this.sql_url = "jdbc:mysql://" + sql_host + ":" + sql_port + "/" + sql_db;
		
		// save the other needed options for later usage
		this.sql_table = this.getConfig().getString("db.table");
		this.sql_userfield = this.getConfig().getString("db.userfield");
		this.sql_passfield = this.getConfig().getString("db.passfield");
		this.sql_encryption = this.getConfig().getString("db.encryption");
		
		// now register the events
		this.eventListener = new MyEventListener(this);
		pm.registerEvents(this.eventListener, this);
		
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");
	}
	
	private void log_in (Player player, String password)
	{
		if (this.authenticated_players.contains(player.getName()))
		{
			player.sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are already logged in.");
			return;
		}
		
		Connection con;
		try
		{
			con = DriverManager.getConnection(this.sql_url, this.sql_user, this.sql_pass);
		}
		catch (SQLException e)
		{
			this.logger.info(pdfFile.getName() + ": Could not connect to database. Login of " + player.getName() + " failed.");
			e.printStackTrace();
			return;
		}
		
		Statement stmt;
		ResultSet rs = null;
		String hash = "";
		
		if (this.sql_encryption.equalsIgnoreCase("MD5"))
			hash = Encryption.MD5(password);
		else if (this.sql_encryption.equalsIgnoreCase("SHA-256"))
			hash = Encryption.SHA256(password);
		else if (this.sql_encryption.equalsIgnoreCase("SHA-512"))
			hash = Encryption.SHA512(password);
		else if (this.sql_encryption.equalsIgnoreCase("SHA1"))
			hash = Encryption.SHA1(password);
		
		String query = ("SELECT COUNT(*) AS counter FROM " + this.sql_table + " WHERE " + this.sql_userfield + "='" + player.getName() + "' AND " + this.sql_passfield + "='" + hash + "';");
		
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return;
		}
		
		try
		{
			rs.next();
			if (rs.getInt("counter") == 1)
			{
				this.authenticated_players.add(player.getName());
				player.sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are now logged in.");
				// restore his inventory
				player.getInventory().setContents(this.saved_inventories.get(player.getName()));
				// remove his inventory from the backup-list
				this.saved_inventories.remove(player.getName());
			}
			else
			{
				player.sendMessage(ChatColor.RED + "[SERVER]" + ChatColor.WHITE + "Incorrect username/password");
			}
			
			con.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return;
		}
		return;
	}
	
	private void log_out (Player player)
	{
		if (!this.authenticated_players.contains(player.getName()))
		{
			player.sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are already logged out.");
			return;
		}
		
		this.authenticated_players.remove(player.getName());
		player.sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are now logged out.");
		
		// save the player's inventory
		this.saved_inventories.put(player.getName(), player.getInventory().getContents());
		
		// clear it
		player.getInventory().clear();
	}
	
}
