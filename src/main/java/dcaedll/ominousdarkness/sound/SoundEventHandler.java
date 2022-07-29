package dcaedll.ominousdarkness.sound;

import dcaedll.ominousdarkness.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;

public class SoundEventHandler
{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OminousDarkness.MODID);
	public static final RegistryObject<SoundEvent> DARKNESS_SOUND_EVENT = registerSoundEvent("darkness_hissing");
	
	public static final RegistryObject<SoundEvent> registerSoundEvent(String name)
	{
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(OminousDarkness.MODID, name)));
	}
	
	public static final void register(IEventBus eventBus)
	{
		SOUND_EVENTS.register(eventBus);
	}
}