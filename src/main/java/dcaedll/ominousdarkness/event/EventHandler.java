package dcaedll.ominousdarkness.event;

import dcaedll.ominousdarkness.*;
import dcaedll.ominousdarkness.capability.*;
import dcaedll.ominousdarkness.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.player.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.level.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;

public class EventHandler
{
    @SubscribeEvent
    public void registerCapabilities(final RegisterCapabilitiesEvent event)
    {
    	event.register(IDarknessEmbrace.class);
    }
    
    @SubscribeEvent
    public void attachCapabilities(final AttachCapabilitiesEvent<Entity> event)
    {
    	if (!(event.getObject() instanceof Player)) return;
    	
    	event.addCapability(DarknessHandlerProvider.RESOURCE, new DarknessHandlerProvider());
    }
    
    @SubscribeEvent
    public void playerTick(final TickEvent.PlayerTickEvent event)
	{
    	if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER && event.player instanceof ServerPlayer)
    	{
    		DarknessProcessor.tickPlayer((ServerPlayer)event.player);
    	}
	}
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void localPlayerTick(final TickEvent.PlayerTickEvent event)
    {
    	if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT && event.player instanceof LocalPlayer)
    	{
    		SoundPlayback.playDarknessSoundEffects((LocalPlayer)event.player);
    	}
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void levelLoad(final LevelEvent.Load event)
    {
    	LevelAccessor level = event.getLevel();
    	if (level instanceof ClientLevel)
    		SoundPlayback.onClientLevelLoad((ClientLevel)level);
    }
}