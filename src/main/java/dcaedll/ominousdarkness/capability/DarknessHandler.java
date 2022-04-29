package dcaedll.ominousdarkness.capability;

import java.util.*;

import dcaedll.ominousdarkness.*;
import dcaedll.ominousdarkness.config.*;
import dcaedll.ominousdarkness.util.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;

public class DarknessHandler implements IDarknessEmbrace
{
	public ResourceLocation dim;
	public boolean isInSuitableDim;
	
	public final List<DarknessEffect> reappliedEffects = new ArrayList<>();
	public int effectCounter;
	public int reTickCounter;
	public int damageDelayCounter;
	public int damageCounter;
	public boolean dirty = true;
	
	private float _factor;
	private float _growthTime;
	private float _growthTimeTicks;
	private float _growthStep;
	private float _falloffTime;
	private float _falloffTimeTicks;
	private float _falloffStep;
	
	private float _delayValue;
	private float _delay;
	private float _delayTicks;

	@Override
	public float get_factor()
	{
		return _factor;
	}
	
	@Override
	public void set_factor(float factor)
	{
		float f = _factor;
		_factor = MathHelper.clamp(factor, 0.0f, 1.0f);
		if (_factor != f)
			dirty = true;
	}
	
	public float get_growthTime()
	{
		return _growthTime;
	}

	public void set_growthTime(float value)
	{
		_growthTime = MathHelper.clamp(value, 0, Float.MAX_VALUE);
		_growthTimeTicks = _growthTime * 20;
		_growthStep = getGrowthInTicks() > 0 ? 1 / getGrowthInTicks() : 1;
	}
	
	public float get_falloffTime()
	{
		return _falloffTime;
	}

	public void set_falloffTime(float value)
	{
		_falloffTime = MathHelper.clamp(value, 0, Float.MAX_VALUE);
		_falloffTimeTicks = _falloffTime * 20;
		_falloffStep = getFalloffInTicks() > 0 ? 1 / getFalloffInTicks() : 1;
	}
	
	public float get_delay()
	{
		return _delay;
	}
	
	public void set_delay(float value)
	{
		_delay = MathHelper.clamp(value, 0, Float.MAX_VALUE);
		_delayTicks = _delay * 20;
	}
	
	public float get_delayValue()
	{
		return _delayValue;
	}
	
	public void set_delayValue(float value)
	{
		_delayValue = MathHelper.clamp(value, 0.0f, getDelayInTicks());
	}
	
	public DarknessHandler()
	{
		set_growthTime(ConfigHandler.getCommonCustom().growth.get().floatValue());
		set_falloffTime(ConfigHandler.getCommonCustom().falloff.get().floatValue());
		set_delay(ConfigHandler.getCommonCustom().delay.get().floatValue());
	}
	
	public float getGrowthInTicks()
	{
		return _growthTimeTicks;
	}
	
	public float getFalloffInTicks()
	{
		return _falloffTimeTicks;
	}
	
	public float getDelayInTicks()
	{
		return _delayTicks;
	}
	
	public boolean atFull()
	{
		return get_factor() >= 1;
	}
	
	public boolean atZero()
	{
		return get_factor() <= 0;
	}
	
	public boolean aboveZero()
	{
		return get_factor() > 0;
	}
	
	public boolean delayFinished()
	{
		return aboveZero() || (getDelayInTicks() > 0 ? get_delayValue() / getDelayInTicks() >= 1 : true);
	}
	
	public boolean update(boolean grow)
	{
		if (aboveZero())
			set_delayValue(0);
		
		if (grow)
		{
			if (!_tickDelay())
			{
				return false;
			}
			set_factor(get_factor() + _growthStep);
			return atFull();
		}
		set_factor(get_factor() - _falloffStep);
		return atZero();
	}

	@Override
	public void serializeNBT(CompoundNBT tag)
	{
		tag.putFloat("factor", get_factor());
		tag.putFloat("delay", get_delayValue());
		tag.putInt("damageDelayCounter", damageDelayCounter);
		tag.putInt("damageCounter", damageCounter);
	}

	@Override
	public void deserializeNBT(CompoundNBT tag)
	{
		set_factor(tag.getFloat("factor"));
		set_delayValue(tag.getFloat("delay"));
		damageDelayCounter = tag.getInt("damageDelayCounter");
		damageCounter = tag.getInt("damageCounter");
	}
	
	private boolean _tickDelay()
	{
		set_delayValue(get_delayValue() + 1);
		return delayFinished();
	}
}