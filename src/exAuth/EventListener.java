package exAuth;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class EventListener implements Listener
{
	private exAuth plugin;
	
	public EventListener (exAuth exAuth_instance) {
		this.plugin = exAuth_instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockBreaking (BlockBreakEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockDamage (BlockDamageEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockPlace (BlockPlaceEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
		}
		return;
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerChatEvent (AsyncPlayerChatEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			String[] args = event.getMessage().split(" ");
			
			if (!args[0].equals("/login"))
				event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerInteract (PlayerInteractEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin (PlayerJoinEvent event) {
		this.plugin.playerJoined(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerMove (PlayerMoveEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().teleport(event.getFrom());
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerPickupItem (PlayerPickupItemEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerDropItem (PlayerDropItemEvent event) {
		if (!this.plugin.isAuthenticated(event.getPlayer())) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerQuit (PlayerQuitEvent event) {
		this.plugin.playerQuit(event.getPlayer());
	}
	
}
