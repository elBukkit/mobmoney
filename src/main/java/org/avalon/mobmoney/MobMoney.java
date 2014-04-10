package org.avalon.mobmoney;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MobMoney extends JavaPlugin {
	public final Logger l = Logger.getLogger("Minecraft");
	public Economy econ = null;
	//public HashMap<String, Integer> max = new HashMap<String, Integer>();
	//public HashMap<String, Integer> min = new HashMap<String, Integer>();
	public HashMap<String, Double> max = new HashMap<String, Double>();
	public HashMap<String, Double> min = new HashMap<String, Double>();
	public ArrayList<String> worldblacklist = new ArrayList<String>();
	List<String> blacklist;
	
	
	public final MobKillListener MobKill = new MobKillListener(this);
	
	
	public String pickup;
	public Sound sound;
	public float volume;
	public float pitch;
	public ItemStack i;
	
	public boolean CheckSpawnEgg;
	public boolean CheckSpawner;
	public boolean CheckCreative;
	public boolean pickupmessage;
	public boolean doubleevent;
	public int chance;
	
	public void onEnable() {
		if (!setupEconomy()) {
			getLogger().warning(String.format("[%s] - Could not load Plugin duo no Vault dependency found.", new Object[] { getDescription().getName() }));
		getServer().getPluginManager().disablePlugin(this);
		return;	
	} else {
		getLogger().info("[MobGroups] Hooked " + econ.getName());
	}
		
		FileUtils.home = this.getDataFolder();
		File configlist = new File(this.getDataFolder().getAbsolutePath() + File.separator + "config.yml");
		if (!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		if (!configlist.exists()) {
			FileUtils.setupMainConfig();	
		}
		FileUtils.updateConfig();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(MobKill, this);
		setupreward();
		setupvar();
		
	}
	
	public void onDisable() {
		
	}
	
	  private boolean setupEconomy() {
		    if (getServer().getPluginManager().getPlugin("Vault") == null) {
		      return false;
		    }
		    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		    if (rsp == null) {
		      return false;
		    }
		    econ = (Economy)rsp.getProvider();
		    return econ != null;
		  }
	  
	  
	  private void setupreward() {
		  ConfigurationSection conf = this.getConfig().getConfigurationSection("rewards");
		  
		  for (String key : conf.getKeys(false)) {
			  
			  ConfigurationSection v = conf.getConfigurationSection(key);
			  String k = key.toUpperCase();
			  
			  EntityType type = EntityType.valueOf(k);
			  if (type == null) {
				  l.warning(key + " is not a valid mob name.");
				  continue;
			  }
			  min.put(type.name(), v.getDouble("minimum"));
			  max.put(type.name(), v.getDouble("maximum")); 
		  }
	  }
	  
	  private void setupvar() {
		  
			pickup =  this.getConfig().getString("language.pickup");
			sound = Sound.valueOf(getConfig().getString("main.pickupsound"));
			volume = getConfig().getInt("main.pickupsound-volume");
			pitch = getConfig().getInt("main.pickupsound-pitch");
			
			CheckSpawnEgg = getConfig().getBoolean("main.AllowSpawnEggs");
			CheckCreative = getConfig().getBoolean("main.AllowCreativeMode");
			CheckSpawner = getConfig().getBoolean("main.AllowMonsterSpawner");
			pickupmessage = getConfig().getBoolean("main.sendpickupmessage");
			blacklist = getConfig().getStringList("World_Blacklist");
			doubleevent = getConfig().getBoolean("main.double_event");
			chance = getConfig().getInt("main.ChanceToDropMoney");
			
			String reward = getConfig().getString("main.droppedItem").replace(':', ' ');
			String[] r = reward.split(" ");
			
			Material mat = Material.getMaterial(r[0].toUpperCase());
			if (mat == null) {
				l.warning(r[0] + " is not a valid material name!");
			}
			int SubID;
			if (r.length == 1) {
				SubID = 0;
			} else {
				SubID = Integer.parseInt(r[1]);
			}
			i = new ItemStack(mat, 1, (short) SubID);
		  
		  
		  
		  
	  }
	  
	   public boolean onCommand(CommandSender sender, Command cmd,
				String commandLabel, String[] args) {
		       if (sender instanceof Player) {
		        Player player = (Player) sender; 	
		   
			   if (commandLabel.equalsIgnoreCase("MobMoney") && args.length == 1) {
				   if (args[0].equalsIgnoreCase("reload")) {
				     if (player.hasPermission("mobmoney.reload")) {
				   this.reloadConfig();
				   this.max.clear();
				   this.min.clear();
				   this.blacklist.clear();
				   setupreward();
				   setupvar();
				   player.sendMessage(ChatColor.GREEN + "Reloaded Configuration.");
				   return true;
				        } else { player.sendMessage(ChatColor.RED + "You do not have enough permissions to perform this command."); }
				     } else { 
				    	 player.sendMessage(ChatColor.GREEN + "=== MobMoney ===");
				    	 player.sendMessage(ChatColor.GOLD + "/mobmoney reload - Reload the configuration.");
				    	 player.sendMessage(ChatColor.GREEN + "=== MobMoney ===");
				    	 return false;
				     }
				   } else { 
				    	 player.sendMessage(ChatColor.GREEN + "=== MobMoney ===");
				    	 player.sendMessage(ChatColor.GOLD + "/mobmoney reload - Reload the configuration.");
				    	 player.sendMessage(ChatColor.GREEN + "=== MobMoney ===");
				    	 return false;
				     }
			   } else {
				   if (commandLabel.equalsIgnoreCase("MobMoney") && args.length == 1) {
					   if (args[0].equalsIgnoreCase("reload")) {
						   this.max.clear();
						   this.min.clear();
						   this.blacklist.clear();
						   setupreward();
						   setupvar();
						   System.out.println("[MobMoney] Reloaded Configuration");
						   return true;
					   } else { System.out.println("Wrong Command Usage. Use /mobmoney reload"); return false; }
				   }else { System.out.println("Wrong Command Usage. Use /mobmoney reload"); return false; }
				   
				   
				   
			   }
		   return true;
	   }

}
