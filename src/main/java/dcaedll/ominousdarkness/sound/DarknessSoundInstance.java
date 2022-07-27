package dcaedll.ominousdarkness.sound;

import dcaedll.ominousdarkness.config.*;
import net.minecraft.client.audio.*;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.*;

public class DarknessSoundInstance extends TickableSound
{
	public float factor = 0;
	public float maxVolume = ConfigHandler.getCommonCustom().soundEffectVolume.get().floatValue();
	
	public DarknessSoundInstance(SoundEvent event, SoundCategory source)
	{
		super(event, source);
		volume = 0;
		delay = 0;
		looping = true;
	}

	@Override
	public void tick()
	{
		volume = factor * maxVolume;
	}
	
	public void setPos(Vector3d pos)
	{
		x = pos.x();
		y = pos.y();
		z = pos.z();
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