package dcaedll.ominousdarkness;

//import org.slf4j.*;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.Logger;

//import com.mojang.logging.*;

import dcaedll.ominousdarkness.client.*;
import dcaedll.ominousdarkness.config.*;
import dcaedll.ominousdarkness.event.*;
import dcaedll.ominousdarkness.net.*;
import net.minecraftforge.common.*;
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
    	
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigHandler::configLoading);
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigHandler::configReloading);
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::_setup);
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::_clientSetup);
    	MinecraftForge.EVENT_BUS.register(new EventHandler());
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