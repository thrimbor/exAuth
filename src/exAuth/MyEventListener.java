package exAuth;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class MyEventListener implements Listener
{
	private exAuth plugin;
	
	public MyEventListener (exAuth exAuth_instance)
	{
		this.plugin = exAuth_instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockBreaking (BlockBreakEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockDamage (BlockDamageEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockPlace (BlockPlaceEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
		}
		return;
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerChatEvent (PlayerChatEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			String[] args = event.getMessage().split(" ");
			
			if (!args[0].equals("/login"))
				event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerInteract (PlayerInteractEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin (PlayerJoinEvent event)
	{
		// save the player's inventory
		Player p = event.getPlayer();
		this.plugin.saved_inventories.put(p.getName(), p.getInventory().getContents());
		
		// clear it
		p.getInventory().clear();
		
		// send the user a nice message
		p.sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + this.plugin.join_string);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerMove (PlayerMoveEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
			event.getPlayer().teleport(event.getFrom());
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerPickupItem (PlayerPickupItemEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerDropItem (PlayerDropItemEvent event)
	{
		if (!this.plugin.authenticated_players.contains(event.getPlayer().getName()))
		{
			//event.getPlayer().sendMessage(ChatColor.RED + "[SERVER] " + ChatColor.WHITE + "You are not allowed to do this!");
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerQuit (PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		
		if (this.plugin.authenticated_players.contains(p.getName()))
		{
			// log the player out
			this.plugin.authenticated_players.remove(p.getName());
		}
		else
		{
			// restore his inventory
			p.getInventory().setContents(this.plugin.saved_inventories.get(p.getName()));
			// remove his inventory from the backup-list
			this.plugin.saved_inventories.remove(p.getName());
		}
	}
	
	
}
