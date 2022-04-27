package dcaedll.ominousdarkness;

import javax.annotation.*;

import dcaedll.ominousdarkness.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class DarknessEffect
{
	private Effect _mobEffect;
	private String _name;
	private boolean _indefinite;
	private int _duration = 40;
	private int _power = 1;
	private float _factor = 0.0f;
	
	public String get_registryName()
	{
		return _name;
	}
	
	public int get_duration()
	{
		return _duration;
	}
	
	public void set_duration(int duration)
	{
		_duration = (int)MathHelper.clamp(duration, 0, Integer.MAX_VALUE);
		set_indefinite(false);
	}
	
	public int get_power()
	{
		return _power;
	}
	
	public void set_power(int value)
	{
		_power = (int)MathHelper.clamp(value, 1, Integer.MAX_VALUE);
	}
	
	public float get_factor()
	{
		return _factor;
	}
	
	public void set_factor(float value)
	{
		_factor = MathHelper.clamp(value, 0.0f, 1.0f);
	}
	
	public boolean get_indefinite()
	{
		return _indefinite;
	}
	
	public void set_indefinite(boolean value)
	{
		_indefinite = value;
	}
	
	@Nullable
	public Effect get_mobEffect()
	{
		return _mobEffect;
	}

	@SuppressWarnings("deprecation")
	public DarknessEffect(@Nonnull String registryName)
	{
		_name = registryName;
		_mobEffect = Registry.MOB_EFFECT.get(new ResourceLocation(get_registryName()));
		set_indefinite(true);
	}
	
	public void apply(@Nonnull PlayerEntity player)
	{
		if (_mobEffect != null)
			player.addEffect(newEffectInstance());
	}
	
	private EffectInstance newEffectInstance()
	{
		EffectInstance i = new EffectInstance(_mobEffect, get_duration(), get_power() - 1);
		i.setNoCounter(get_indefinite());
		return i;
	}
}