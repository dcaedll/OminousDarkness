package dcaedll.ominousdarkness.event;

import dcaedll.ominousdarkness.*;
import dcaedll.ominousdarkness.capability.*;
import dcaedll.ominousdarkness.client.*;
import net.minecraft.client.entity.player.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.*;
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
    	if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER && event.player instanceof ServerPlayerEntity)
    	{
    		DarknessProcessor.tickPlayer((ServerPlayerEntity)event.player);
    	}
	}
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void localPlayerTick(final TickEvent.PlayerTickEvent event)
    {
    	if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT && event.player instanceof ClientPlayerEntity)
    	{
    		SoundPlayback.playDarknessSoundEffects((ClientPlayerEntity)event.player);
    	}
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void localLevelLoad(final WorldEvent.Load event)
    {
    	IWorld level = event.getWorld();
    	if (level instanceof ClientWorld)
    		SoundPlayback.onClientLevelLoad((ClientWorld)level);
    }
    
    @SubscribeEvent
    public void playerClone(final PlayerEvent.Clone event)
    {
    	
    }
}