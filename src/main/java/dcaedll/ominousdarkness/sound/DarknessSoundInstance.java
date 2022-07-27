package dcaedll.ominousdarkness.sound;

import dcaedll.ominousdarkness.config.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;

public class DarknessSoundInstance extends AbstractTickableSoundInstance
{
	public float factor = 0;
	public float maxVolume = ConfigHandler.getCommonCustom().soundEffectVolume.get().floatValue();
	
	public DarknessSoundInstance(SoundEvent event, SoundSource source, RandomSource random)
	{
		super(event, source, random);
		volume = 0;
		delay = 0;
		looping = true;
	}

	@Override
	public void tick()
	{
		volume = factor * maxVolume;
	}
	
	public void setPos(Vec3 pos)
	{
		x = pos.x;
		y = pos.y;
		z = pos.z;
	}
	
	public boolean canStartSilent()
	{
		return true;
	}
	
	public void doStop()
	{
		stop();
	}
}