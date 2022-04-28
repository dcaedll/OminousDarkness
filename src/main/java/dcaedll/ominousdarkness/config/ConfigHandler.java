package dcaedll.ominousdarkness.config;

import java.util.*;

import org.apache.commons.lang3.tuple.*;

import dcaedll.ominousdarkness.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.config.*;
import net.minecraftforge.fml.event.config.*;

public class ConfigHandler
{
	public static final List<Pair<?, ForgeConfigSpec>> configList = new ArrayList<>();
	public static Pair<ConfigCommon, ForgeConfigSpec> common;
	
	public static void init()
	{
		configList.add(common = new ForgeConfigSpec.Builder().configure(ConfigCommon::new));
	}
	
	public static void register()
	{
    	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common.getRight());
	}
	
	public static void configLoading(final ModConfigEvent.Loading event)
	{
		DarknessProcessor.reloadEffects();
	}
	
	public static void configReloading(final ModConfigEvent.Reloading event)
	{
	}
	
	public static ConfigCommon getCommonCustom()
	{
		return common.getLeft();
	}
}