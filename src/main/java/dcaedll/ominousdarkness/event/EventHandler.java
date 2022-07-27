package dcaedll.ominousdarkness.event;

import dcaedll.ominousdarkness.*;
import dcaedll.ominousdarkness.capability.*;
import dcaedll.ominousdarkness.sound.*;
import net.minecraft.client.entity.player.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;

public class EventHandler
{
    @SubscribeEvent
    public void attachCapabilities(final AttachCapabilitiesEvent<Entity> event)
    {
    	if (!(event.getObject() instanceof PlayerEntity)) return;
    	
    	event.addCapability(DarknessHandlerProvider.RESOURCE, new DarknessHandlerProvider());
    }
    
    @SubscribeEvent
    public void playerTick(final TickEvent.PlayerTickEvent event)
	{
    	if (event.phase == TickEvent.Phase.END)
    	{
    		if (event.side == LogicalSide.SERVER && event.player instanceof ServerPlayerEntity)
    			DarknessProcessor.tickPlayer((ServerPlayerEntity)event.player);
    		else if (event.side == LogicalSide.CLIENT && event.player instanceof ClientPlayerEntity)
    			SoundEventHandler.playDarknessSoundEffects((ClientPlayerEntity)event.player);
    	}
	}

    @SubscribeEvent
    public void levelLoad(final WorldEvent.Load event)
    {
    	IWorld level = event.getWorld();
    	if (level instanceof ClientWorld)
    		SoundEventHandler.onClientLevelLoad((ClientWorld)level);
    }
    
    @SubscribeEvent
    public void playerClone(final PlayerEvent.Clone event)
    {
    	
    }
}