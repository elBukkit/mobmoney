package org.avalon.mobmoney;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MobKillListener implements Listener {

	public static MobMoney plugin;

	public MobKillListener(MobMoney instance) {
		plugin = instance;
	}

	private HashSet<UUID> Check = new HashSet<UUID>();

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.SPAWNER_EGG && !plugin.CheckSpawnEgg) {
			Check.add(e.getEntity().getUniqueId());
			return;
		}

		else if (e.getSpawnReason() == SpawnReason.SPAWNER && !plugin.CheckSpawner) {
			Check.add(e.getEntity().getUniqueId());
			return;
		}
	}

	@EventHandler
	public void onMobKill(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {

			boolean spawnedFromSpawner = Check.remove(e.getEntity().getUniqueId());
			Player p = (Player) e.getEntity().getKiller();
			if (spawnedFromSpawner)
				return;
			if (!plugin.CheckCreative && e.getEntity().getKiller().getGameMode() == GameMode.CREATIVE)
				return;
			if (plugin.blacklist.contains(e.getEntity().getWorld().getName()))
				return;
			if (!p.hasPermission("mobmoney.receive"))
				return;
			if (new Random().nextInt(100) <= plugin.chance) {
				if (plugin.max.containsKey(e.getEntityType().name())) {
					if (Item(e.getEntityType().name()) != null) {
						if (!plugin.doubleevent) {
							e.getEntity()
									.getWorld()
									.dropItem(e.getEntity().getLocation(),
											Item(e.getEntityType().name()));
						} else {
							e.getEntity()
									.getWorld()
									.dropItem(e.getEntity().getLocation(),
											Item(e.getEntityType().name()));
							e.getEntity()
									.getWorld()
									.dropItem(e.getEntity().getLocation(),
											Item(e.getEntityType().name()));
						}
						return;
					}
				}
			}
		}
	}

	private ItemStack Item(String mobname) {
		ItemStack i = plugin.i;
		ItemMeta meta = i.getItemMeta();
		double max = plugin.max.get(mobname);
		double min = plugin.min.get(mobname);
		if (max == 0.0 && min == 0.0) {
			return null;
		} else {
			// int random = new Random().nextInt(max-min) + min;
			Random r = new Random();
			double result = min + (max - min) * r.nextDouble();
			if (result == 0.0) {
				return null;
			}
			meta.setDisplayName(plugin.econ.currencyNameSingular());
			List<String> lore = new ArrayList<String>();
			lore.add(RoundTo2Decimals(result));
			meta.setLore(lore);
			i.setItemMeta(meta);
			return i;
		}
	}

	private String RoundTo2Decimals(double val) {
		DecimalFormat df2 = new DecimalFormat("###.##");
		return df2.format(val);
	}

	protected float getAmount(ItemStack itemStack) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta == null)
			return 0.0f;
		List<String> lore = meta.getLore();
		if (lore.size() == 0)
			return 0.0f;
		float value = 0;
		try {
			value = Float.parseFloat(lore.get(0));
		} catch (Throwable ex) {
			return value;
		}

		return value * itemStack.getAmount();
	}

	@EventHandler
	public void onMoneyPickup(PlayerPickupItemEvent e) {
		if (e.getItem().getItemStack().hasItemMeta()) {
			ItemStack i = e.getItem().getItemStack();
			Player p = e.getPlayer();
			if (i.getItemMeta().getDisplayName() != null && i.getItemMeta().getDisplayName().equals(plugin.econ.currencyNameSingular())) {
				float money = getAmount(i);

				e.setCancelled(true);
				e.getItem().remove();
				if (money == 0.0) {
					e.setCancelled(true);
					e.getItem().remove();
					return;
				}
				if (add(p, money)) {
					if (plugin.pickupmessage) {
						String rewardMessage = plugin.pickup.replace(
								"%money%",
								RoundTo2Decimals(money) + " " + plugin.econ.currencyNameSingular());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', rewardMessage));
					}
					p.playSound(p.getLocation(), plugin.sound, plugin.volume, plugin.pitch);
				}
			}
		}
	}

	private boolean add(Player player, float money) {
		EconomyResponse r = plugin.econ.depositPlayer(player.getName(), money);
		if (r.transactionSuccess()) {
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "Error while adding money to your account");
			return false;
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.isCancelled()) {
			HumanEntity ent = e.getWhoClicked();
			if (ent instanceof Player) {
				Player player = (Player) ent;
				Inventory inv = e.getInventory();
				if (inv instanceof AnvilInventory) {
					InventoryView view = e.getView();
					int rawSlot = e.getRawSlot();
					if (rawSlot == view.convertSlot(rawSlot)) {
						if (rawSlot == 2) {
							ItemStack item = e.getCurrentItem();
							if (item != null) {
								ItemMeta meta = item.getItemMeta();
								if (meta != null) {
									if (meta.hasDisplayName()) {
										String displayName = meta
												.getDisplayName();

										if (displayName.equals(plugin.econ.currencyNameSingular())) {
											e.setCancelled(true);
											player.sendMessage(ChatColor.RED + "You cannot rename this item.");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
