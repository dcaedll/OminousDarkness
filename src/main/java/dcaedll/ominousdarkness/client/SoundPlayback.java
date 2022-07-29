package dcaedll.ominousdarkness.client;

import dcaedll.ominousdarkness.capability.*;
import dcaedll.ominousdarkness.config.*;
import dcaedll.ominousdarkness.sound.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.player.*;
import net.minecraft.sounds.*;

public class SoundPlayback
{
	public static DarknessSoundInstance soundInstance;
	
	public static final void playDarknessSoundEffects(LocalPlayer player)
	{
		if (player.isCreative() || player.isSpectator() || !ConfigHandler.getCommonCustom().playSoundEffect.get())
		{
			if (soundInstance != null) soundInstance.doStop();
			return;
		}
		
		if (soundInstance == null || soundInstance.isStopped())
		{
			soundInstance = new DarknessSoundInstance(SoundEventHandler.DARKNESS_SOUND_EVENT.get(), SoundSource.AMBIENT); 
			Minecraft.getInstance().getSoundManager().play(soundInstance);
		}
		player.getCapability(DarknessHandlerProvider.CAP).ifPresent(cap ->
		{
			float factor = cap.get_factor();
			soundInstance.factor = factor;
			soundInstance.setPos(player.getEyePosition());
		});
	}
	
	public static final void onClientLevelLoad(ClientLevel level)
	{
		soundInstance = null;
	}
}