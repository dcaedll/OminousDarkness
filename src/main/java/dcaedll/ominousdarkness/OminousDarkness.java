package dcaedll.ominousdarkness;

import org.apache.logging.log4j.*;

import dcaedll.ominousdarkness.client.*;
import dcaedll.ominousdarkness.config.*;
import dcaedll.ominousdarkness.event.*;
import dcaedll.ominousdarkness.net.*;
import dcaedll.ominousdarkness.sound.SoundEventHandler;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.*;

@Mod(OminousDarkness.MODID)
public class OminousDarkness
{
	public static final String MODID = "ominousdarkness";
    public static final Logger LOGGER = LogManager.getLogger();
	
    static
    {
    	ConfigHandler.init();
    }
    
    public OminousDarkness()
    {
    	ConfigHandler.register();
    	
    	IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
    	eventBus.addListener(ConfigHandler::configLoading);
    	eventBus.addListener(ConfigHandler::configReloading);
    	eventBus.addListener(this::_setup);
    	eventBus.addListener(this::_clientSetup);
    	MinecraftForge.EVENT_BUS.register(new EventHandler());
    	SoundEventHandler.register(eventBus);
    }
    
    private void _setup(final FMLCommonSetupEvent event)
    {
    	LOGGER.info("Embracing the darkness...");
    	PacketHandler.init();
    }
    
    private void _clientSetup(final FMLClientSetupEvent event)
    {
    	DarknessGuiHandler.init();
    }
}