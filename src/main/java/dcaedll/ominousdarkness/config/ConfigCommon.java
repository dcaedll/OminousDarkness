package dcaedll.ominousdarkness.config;

import java.util.*;

import net.minecraftforge.common.*;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class ConfigCommon
{
	public final IntValue lightLevelThreshold;
	public final DoubleValue delay;
	public final DoubleValue growth;
	public final DoubleValue falloff;
	public final BooleanValue darknessKills;
	public final ConfigValue<List<? extends Object>> dimBlacklist;
	public final BooleanValue dimListAsWhitelist;
	
	public final DoubleValue damage;
	public final DoubleValue damageInterval;
	public final DoubleValue damageDelay;
	
	public final ConfigValue<List<? extends Object>> shinyItems;
	public final ConfigValue<List<? extends Object>> effects;
	
	public ConfigCommon(ForgeConfigSpec.Builder builder)
	{
		builder.comment("The basic configuration for how the darkness should behave").push("darkness");
		
		int max = 65536;
		lightLevelThreshold = builder
				.comment("", "The light level threshold (inclusive), at and below which the darkness will start consuming a player over time")
				.defineInRange("light_level_threshold", 4, -max, max);
		delay = builder
				.comment("", "The time (in seconds) a player has to spend in the darkness before it starts accumulating",
						"The timer gets reset once the player is in a lit enough area, and starts ticking again once in the darkness and the player's darkness level is at 0")
				.defineInRange("delay", 4.0f, 0.0f, max);
		growth = builder
				.comment("", "The time (in seconds) it takes for the darkness to fully consume a player",
						"In this context, 0 would mean that the darkness should consume the player instantly, once in an unlit area")
				.defineInRange("growth_time", 7.0f, 0.0f, max);
		falloff = builder
				.comment("", "The time (in seconds) it takes for the darkness to fall off",
						"In this context, 0 would mean that the darkness should fall off instantly, once in a lit enough area")
				.defineInRange("falloff_time", 2.0f, 0.0f, max);
		darknessKills = builder
				.comment("", "Whether the darkness should kill a player upon fully consuming them")
				.define("darkness_kills", true);
		
		builder.comment("Any dimension-related configuration").push("dimension");
		
		List<String> dimPath = new ArrayList<String>();
		dimPath.add("dim_blacklist");
		
		dimBlacklist = builder
				.comment("", "The list of dimension registry names where the effects of the darkness should be disabled",
				"e.g., \"minecraft:overworld\", \"minecraft:the_nether\", \"minecraft:the_end\"")
				.defineListAllowEmpty(dimPath, () -> new ArrayList<>(), ConfigCommon::_itemIsNotBlankString);
		dimListAsWhitelist = builder
				.comment("", "Whether to use dimension blacklist as whitelist instead")
				.define("dim_blacklist_as_whitelist", false);
		
		builder.pop();
		builder.comment("Any damage-over-time-related configuration").push("damage");
		
		damage = builder
				.comment("", "The amount of damage (in half-hearts) the darkness deals at customizable [damage_interval] intervals")
				.defineInRange("damage", 0.0f, 0.0f, max);
		damageInterval = builder
				.comment("", "The interval (in seconds) at which the darkness damages a player",
						"For example, 3 would mean that the darkness will hit the player every 3 seconds")
				.defineInRange("damage_interval", 3.0f, 0.0f, max);
		damageDelay = builder
				.comment("", "The delay (in seconds) after which a player will start taking damage",
						"This timer starts ticking once the darkness begins consuming the player")
				.defineInRange("damage_delay", 0.0f, 0.0f, max);
		
		builder.pop();
		builder.comment("Miscellaneous configuration").push("misc");
		
		List<String> shinyItemsPath = new ArrayList<String>();
		shinyItemsPath.add("shiny_items");
		List<String> shinyItemsDef = _initVanillaShinyItems();
		
		shinyItems = builder
				.comment("", "Items that should add to the total light value when a player is holding them in either hand",
						"An item should be included as follows: \"item_registry_name$N\", where N is an additive light value",
						"$N can be omitted, in this case it is implied that the item has the light value of 15",
						"If the player is holding two items specified in this list (one in each hand), their light values are summed",
						"Stack size does not participate in calculations")
				.defineListAllowEmpty(shinyItemsPath, () -> shinyItemsDef, ConfigCommon::_itemIsNotBlankString);
		
		List<String> effectsPath = new ArrayList<String>();
		effectsPath.add("effects");
		List<String> effectsDef = new ArrayList<String>();
		
		effects = builder
				.comment("", "Any additional effects to apply to a player",
						"An effect should be included as follows: \"effect_registry_name[duration=A][level=B][timing=C]\"",
						"Duration is a number and determines the duration of the effect in seconds, defaults to infinite (for as long as the darkness level persists)",
						"Level is a number and determines the power of the effect, defaults to 1",
						"Timing is a number followed by either '%' or 's' (for percentage or seconds respectively) and determines the timestamp at which the effect occurs, defaults to 0s",
						"Any parameters can be omitted, in this case they are set to their default values",
						"Examples:",
						"\"minecraft:hunger[duration=2][timing=50%]\" would apply Hunger I to a player for 2 seconds roughly halfway through (that is, if growth_time is set to 20, the effect would be applied at 10 seconds)",
						"\"minecraft:slowness[timing=2.8s][level=2]\" would apply Slowness II to a player for as long as they are being consumed by the darkness, starting at 2.8 seconds",
						"\"minecraft:strength\" would apply Strength I to a player right after they start gaining the darkness level, with the effect persisting for as long as their darkness level is higher than 0")
				.defineListAllowEmpty(effectsPath, () -> effectsDef, ConfigCommon::_itemIsNotBlankString);
	}
	
	private static boolean _itemIsNotBlankString(Object item)
	{
		return item instanceof String && !((String)item).isBlank();
	}
	
	private static ArrayList<String> _initVanillaShinyItems()
	{
		ArrayList<String> list = new ArrayList<String>();
		String[] shiny = new String[]
		{
			"beacon$8",
			"conduit$8",
			"glowstone$8",
			"jack_o_lantern$8",
			"lantern$8",
			"soul_lantern$5",
			"sea_lantern$8",
			"shroomlight$8",
			"glow_berries$3",
			"end_rod$7",
			"torch$7",
			"crying_obsidian$5",
			"enchanting_table$4",
			"ender_chest$4",
			"glow_lichen$4",
			"redstone_torch$2",
			"small_amethyst_bud$1",
			"medium_amethyst_bud$1",
			"large_amethyst_bud$2",
			"amethyst_cluster$3",
			"magma_block$2",
			"brewing_stand$1",
			"brown_mushroom$1",
			"dragon_egg$1",
			"end_portal_frame$1",
			"light$8",
			"ender_pearl$1",
			"ender_eye$1",
			"experience_bottle$1",
			"redstone$1",
			"lava_bucket$8",
			"spectral_arrow$3",
			"enchanted_golden_apple$3",
			"glow_ink_sac$2",
			"amethyst_shard$1",
			"nether_star$8",
			"glistering_melon_slice$2",
			"glowstone_dust$4",
			"blaze_powder$1",
			"blaze_rod$1",
			"magma_cream$1",
		};
		for (int i = 0; i < shiny.length; i++)
		{
			list.add("minecraft:".concat(shiny[i]));
		}
		
		return list;
	}
}