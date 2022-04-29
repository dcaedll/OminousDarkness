package dcaedll.ominousdarkness;

import java.util.*;

import javax.annotation.*;

import dcaedll.ominousdarkness.capability.*;
import dcaedll.ominousdarkness.config.*;
import dcaedll.ominousdarkness.net.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.fmllegacy.network.*;

public class DarknessProcessor
{
	public static final int EFFECT_REAPPLICATION_RATE = 10;
	
	private static DarknessEffect[] _eff;
	private static float _effLowerBound = 0.0f;
	
	public static void tickPlayer(@Nonnull final ServerPlayer player)
	{
		if (player.isCreative() || player.isSpectator())
			return;
		
    	player.getCapability(DarknessHandlerProvider.CAP).ifPresent(cap ->
    	{
    		if (cap instanceof DarknessHandler)
    		{
    			DarknessHandler dh = (DarknessHandler)cap;
    			if (_validatePlayerDim(player, dh))
    			{
    				_tickDarkness(player, dh);
    				_shareDarknessUpdate(player, dh);
    			}
    		}
    	});
	}
	
	public static void onConfigSetUp()
	{
		reloadEffects();
	}
	
	public static void reloadEffects()
	{
		_eff = _parseEffects(ConfigHandler.getCommonCustom().effects.get(), ConfigHandler.getCommonCustom().growth.get().floatValue() * 20.0f);
		_updateEffectsLowerBound();
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static void receiveDarknessUpdate(float factor)
	{
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null)
		{
			player.getCapability(DarknessHandlerProvider.CAP).ifPresent(cap -> ((DarknessHandler)cap).set_factor(factor));
		}
	}
	
	private static void _shareDarknessUpdate(ServerPlayer player, DarknessHandler dh)
	{
		if (dh.dirty)
		{
			DarknessPacket packet = new DarknessPacket(dh.get_factor());
			PacketHandler.CHANNEL_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
			dh.dirty = false;
		}
	}
	
	private static void _tickDarkness(final Player player, final DarknessHandler dh)
	{
		int th = _getLightLevelThreshold();
		int total = _calcTotalLightValue(player);
		if (total <= th)
		{
			if (dh.update(true) && _getDarknessKills())
			{
				_kill(player);
				return;
			}
			if (dh.aboveZero())
			{
				_handleEffects(player, dh);
			}
		}
		else
		{
			if (dh.update(false))
			{
				dh.reappliedEffects.clear();
				dh.damageDelayCounter = 0;
				dh.damageCounter = 0;
			}
			dh.effectCounter = 0;
		}
		
		if (dh.aboveZero())
		{
			if (dh.damageDelayCounter + 1 >= _getDamageDelay() * 20)
			{
				dh.damageCounter--;
				if (dh.damageCounter <= 0)
				{
					_damage(player);
					dh.damageCounter = (int)(_getDamageInterval() * 20);
				}
			}
			else dh.damageDelayCounter++;
			if (dh.reTickCounter++ >= EFFECT_REAPPLICATION_RATE)
			{
				_reapplyEffects(player, dh);
				dh.reTickCounter = 0;
			}
		}
	}
	
	private static void _kill(Player player)
	{
		player.hurt(DarknessDamageSource.DARKNESS, Float.MAX_VALUE);
	}
	
	private static void _damage(Player player)
	{
		player.hurt(DarknessDamageSource.DARKNESS, _getDamage());
	}
	
	private static DarknessEffect[] _parseEffects(List<? extends Object> effs, float growthTicks)
	{
		DarknessEffect[] arr = new DarknessEffect[effs.size()];
		for (int j = 0; j < effs.size(); j++)
		{
			String s = (String)effs.get(j);
			String[] split = s.split("(?=\\[)");
			DarknessEffect de = new DarknessEffect(split[0]);
			if (de.get_mobEffect() == null)
				continue;
			
			if (split.length > 1)
			{
				for (int i = 1; i < split.length; i++)
				{
					if (split[i].length() >= 5 && split[i].startsWith("[") && split[i].endsWith("]"))
					{
						String[] paramSplit = split[i].split("\\=");
						if (paramSplit.length == 2 && paramSplit[0].length() > 1 && paramSplit[1].length() > 1)
						{
							paramSplit[0] = paramSplit[0].substring(1);
							paramSplit[1] = paramSplit[1].substring(0, paramSplit[1].length() - 1);
							
							String param = paramSplit[0].toLowerCase();
							try
							{
								if (param.equals("duration"))
								{
									de.set_duration(Integer.parseInt(paramSplit[1]) * 20);
								}
								else if (param.equals("level"))
								{
									de.set_power(Integer.parseInt(paramSplit[1]));
								}
								else if (param.equals("timing"))
								{
									String dim = paramSplit[1].substring(paramSplit[1].length() - 1);
									float num = Float.valueOf(paramSplit[1].substring(0, paramSplit[1].length() - 1));
									if (dim.equals("%"))
									{
										de.set_factor(num / 100.0f);
									}
									else if (dim.equals("s"))
									{
										de.set_factor(num * 20.0f / growthTicks);
									}
								}
							}
							catch (Exception e)
							{
								_logEffectParameterError(paramSplit[0], paramSplit[1]);
							}
						}
					}
				}
			}
			
			arr[j] = de;
		}
		
		// sort by factor in asc order
		for (int i = 0; i < arr.length; i++)
		{
			for (int j = 0; j > i && j < arr.length; j++)
			{
				if (arr[i].get_factor() < arr[j].get_factor())
				{
					DarknessEffect de = arr[i];
					arr[i] = arr[j];
					arr[j] = de;
				}
			}
		}
		
		return arr;
	}
	
	private static void _logEffectParameterError(String param, String value)
	{
		OminousDarkness.LOGGER.error("Invalid effect parameter {} with value {}", param, value);
	}
	
	private static boolean _validatePlayerDim(ServerPlayer player, DarknessHandler dh)
	{
		if (player.getLevel().dimension().location() != dh.dim)
		{
			String dim = (dh.dim = player.getLevel().dimension().location()).toString();
			dh.isInSuitableDim = _dimIsWhitelist() ? _dimContains(dim) : !_dimContains(dim);
		}
		return dh.isInSuitableDim;
	}
	
	private static boolean _dimContains(String dim)
	{
		return ConfigHandler.getCommonCustom().dimBlacklist.get().contains(dim);
	}

	private static boolean _dimIsWhitelist()
	{
		return ConfigHandler.getCommonCustom().dimListAsWhitelist.get().booleanValue();
	}
	
	private static void _updateEffectsLowerBound()
	{
		_effLowerBound = Float.MAX_VALUE;
		for (DarknessEffect eff : _eff)
		{
			if (eff.get_factor() <= _effLowerBound)
				_effLowerBound = eff.get_factor();
		}
	}
	
	private static int _calcTotalLightValue(Player player)
	{
		return player.level.getMaxLocalRawBrightness(player.blockPosition())
				+ _getShinyValueForItems(player.getMainHandItem().getItem(), player.getOffhandItem().getItem());
	}
	
	private static int _getLightLevelThreshold()
	{
		return ConfigHandler.getCommonCustom().lightLevelThreshold.get().intValue();
	}
	
	private static boolean _getDarknessKills()
	{
		return ConfigHandler.getCommonCustom().darknessKills.get().booleanValue();
	}
	
	private static float _getDamageInterval()
	{
		return ConfigHandler.getCommonCustom().damageInterval.get().floatValue();
	}
	
	private static float _getDamageDelay()
	{
		return ConfigHandler.getCommonCustom().damageDelay.get().floatValue();
	}
	
	private static float _getDamage()
	{
		return ConfigHandler.getCommonCustom().damage.get().floatValue();
	}
	
	private static int _getShinyValueForItems(Item item1, Item item2)
	{
		boolean flag1 = false;
		boolean flag2 = false;
		int val = 0;
		List<? extends Object> shiny = ConfigHandler.getCommonCustom().shinyItems.get();
		
		for (Object item : shiny)
		{
			String[] split = _splitShinyItemString((String)item);
			if (!flag1 && item1.getRegistryName().toString().equals(split[0]))
			{
				val += (split.length >= 2 ? _getShinyValueForItem(item1, split[1]) : 0xF);
				flag1 = true;
			}
			if (!flag2 && item2.getRegistryName().toString().equals(split[0]))
			{
				val += (split.length >= 2 ? _getShinyValueForItem(item2, split[1]) : 0xF);
				flag2 = true;
			}
			if (flag1 && flag2) break;
		}
		
		return val;
	}
	
	private static String[] _splitShinyItemString(String item)
	{
		return item.split("\\$");
	}
	
	private static int _getShinyValueForItem(Item item, String val)
	{
		try
		{
			return Integer.parseInt(val);
		}
		catch(NumberFormatException e) {};
		return 0;
	}
	
	private static void _handleEffects(Player player, DarknessHandler dh)
	{
		float factor = dh.get_factor();
		if (factor < _effLowerBound)
			return;
		
		for (; dh.effectCounter < _eff.length && factor >= _eff[dh.effectCounter].get_factor(); dh.effectCounter++)
		{
			DarknessEffect de = _eff[dh.effectCounter];
			de.apply(player);
			if (de.get_indefinite() && !dh.reappliedEffects.contains(de))
			{
				dh.reappliedEffects.add(de);
			}
		}
	}
	
	private static void _reapplyEffects(Player player, DarknessHandler dh)
	{
		for (DarknessEffect de : dh.reappliedEffects)
		{
			de.apply(player);
		}
	}
}