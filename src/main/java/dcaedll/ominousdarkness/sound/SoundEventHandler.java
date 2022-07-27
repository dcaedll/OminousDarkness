package dcaedll.ominousdarkness.sound;

import dcaedll.ominousdarkness.*;
import dcaedll.ominousdarkness.capability.*;
import dcaedll.ominousdarkness.config.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.player.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;

public class SoundEventHandler
{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OminousDarkness.MODID);
	public static final RegistryObject<SoundEvent> DARKNESS_SOUND_EVENT = registerSoundEvent("darkness_hissing");
	public static DarknessSoundInstance soundInstance;
	
	public static final RegistryObject<SoundEvent> registerSoundEvent(String name)
	{
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(OminousDarkness.MODID, name)));
	}
	
	public static final void register(IEventBus eventBus)
	{
		SOUND_EVENTS.register(eventBus);
	}
	
	public static final void playDarknessSoundEffects(LocalPlayer player)
	{
		if (player.isCreative() || player.isSpectator() || !ConfigHandler.getCommonCustom().playSoundEffect.get())
		{
			if (soundInstance != null) soundInstance.doStop();
			return;
		}
		
		if (soundInstance == null || soundInstance.isStopped())
		{
			soundInstance = new DarknessSoundInstance(DARKNESS_SOUND_EVENT.get(), SoundSource.AMBIENT); 
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