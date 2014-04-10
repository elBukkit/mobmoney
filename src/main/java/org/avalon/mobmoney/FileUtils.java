package org.avalon.mobmoney;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileUtils {

	public static File home;

	public static void setupMainConfig() {

		File configlist = new File(home.getAbsolutePath() + File.separator
				+ "config.yml");

		try {
			configlist.createNewFile();
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(configlist);

			cfg.set("main.AllowSpawnEggs", true);
			cfg.set("main.AllowMonsterSpawner", true);
			cfg.set("main.AllowCreativeMode", true);
			cfg.set("main.pickupsound", "ITEM_PICKUP");
			cfg.set("main.pickupsound-volume", 5);
			cfg.set("main.pickupsound-pitch", 1);
			cfg.set("main.droppedItem", "Gold_Ingot");
			cfg.set("main.sendpickupmessage", true);
			cfg.set("main.double_event", false);
			cfg.set("main.ChanceToDropMoney", 100);

			cfg.set("rewards.bat.minimum", 5);
			cfg.set("rewards.bat.maximum", 10);
			cfg.set("rewards.blaze.minimum", 5);
			cfg.set("rewards.blaze.maximum", 10);
			cfg.set("rewards.cave_spider.minimum", 5);
			cfg.set("rewards.cave_spider.maximum", 10);
			cfg.set("rewards.chicken.minimum", 5);
			cfg.set("rewards.chicken.maximum", 10);
			cfg.set("rewards.cow.minimum", 5);
			cfg.set("rewards.cow.maximum", 10);
			cfg.set("rewards.creeper.minimum", 5);
			cfg.set("rewards.creeper.maximum", 10);
			cfg.set("rewards.ender_dragon.minimum", 5);
			cfg.set("rewards.ender_dragon.maximum", 10);
			cfg.set("rewards.enderman.minimum", 5);
			cfg.set("rewards.enderman.maximum", 10);
			cfg.set("rewards.ghast.minimum", 5);
			cfg.set("rewards.ghast.maximum", 10);
			cfg.set("rewards.giant.minimum", 5);
			cfg.set("rewards.giant.maximum", 10);
			cfg.set("rewards.horse.minimum", 5);
			cfg.set("rewards.horse.maximum", 10);
			cfg.set("rewards.iron_golem.minimum", 5);
			cfg.set("rewards.iron_golem.maximum", 10);
			cfg.set("rewards.magma_cube.minimum", 5);
			cfg.set("rewards.magma_cube.maximum", 10);
			cfg.set("rewards.mushroom_cow.minimum", 5);
			cfg.set("rewards.mushroom_cow.maximum", 10);
			cfg.set("rewards.ocelot.minimum", 5);
			cfg.set("rewards.ocelot.maximum", 10);
			cfg.set("rewards.pig.minimum", 5);
			cfg.set("rewards.pig.maximum", 10);
			cfg.set("rewards.pig_zombie.minimum", 5);
			cfg.set("rewards.pig_zombie.maximum", 10);
			cfg.set("rewards.sheep.minimum", 5);
			cfg.set("rewards.sheep.maximum", 10);
			cfg.set("rewards.silverfish.minimum", 5);
			cfg.set("rewards.silverfish.maximum", 10);
			cfg.set("rewards.skeleton.minimum", 5);
			cfg.set("rewards.skeleton.maximum", 10);
			cfg.set("rewards.slime.minimum", 5);
			cfg.set("rewards.slime.maximum", 10);
			cfg.set("rewards.snowman.minimum", 5);
			cfg.set("rewards.snowman.maximum", 10);
			cfg.set("rewards.spider.minimum", 5);
			cfg.set("rewards.spider.maximum", 10);
			cfg.set("rewards.squid.minimum", 5);
			cfg.set("rewards.squid.maximum", 10);
			cfg.set("rewards.villager.minimum", 5);
			cfg.set("rewards.villager.maximum", 10);
			cfg.set("rewards.witch.minimum", 5);
			cfg.set("rewards.witch.maximum", 10);
			cfg.set("rewards.wither.minimum", 5);
			cfg.set("rewards.wither.maximum", 10);
			cfg.set("rewards.wolf.minimum", 5);
			cfg.set("rewards.wolf.maximum", 10);
			cfg.set("rewards.zombie.minimum", 5);
			cfg.set("rewards.zombie.maximum", 10);
			cfg.set("World_Blacklist", Arrays.asList("world1", "world2"));

			cfg.set("language.pickup", "&ePicked up %money%");

			cfg.save(configlist);
		} catch (IOException ex) {
		}

	}

	public static void updateConfig() {

		File configlist = new File(home.getAbsolutePath() + File.separator + "config.yml");

		try {
			FileConfiguration cfg = YamlConfiguration
					.loadConfiguration(configlist);
			int u = 0;
			if (!cfg.isSet("main.double_event")) {
				cfg.set("main.double_event", false);
				u++;
			}
			if (!cfg.isSet("main.ChanceToDropMoney")) {
				cfg.set("main.ChanceToDropMoney", 100);
				u++;
			}
			if (!cfg.isSet("World_Blacklist")) {
				cfg.set("World_Blacklist", Arrays.asList("world1", "world2"));
				u++;
			}

			if (u != 0)
				System.out.println("[MobMoney] Updated " + u + " lines in your configuration.");

			cfg.save(configlist);
		} catch (IOException ex) {
		}

	}

}
