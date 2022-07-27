package dcaedll.ominousdarkness.client;

import org.lwjgl.opengl.*;

import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.systems.*;

import dcaedll.ominousdarkness.capability.*;
import dcaedll.ominousdarkness.config.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.*;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.*;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class DarknessGuiHandler
{
	@SubscribeEvent
	public static void onRenderVignette(RenderGameOverlayEvent.Pre event)
	{
		if (event.getType() == ElementType.ALL)
			_renderDarknessEffect(event.getMatrixStack(), event.getWindow());
	}
	
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(DarknessGuiHandler.class);
	}
	
	private static void _renderDarknessEffect(MatrixStack matrixStack, MainWindow window)
	{
		if (!ConfigHandler.getCommonCustom().showOverlay.get())
			return;
		Minecraft mc = Minecraft.getInstance();
		ClientPlayerEntity player = mc.player;
		if (player.isCreative() || player.isSpectator())
			return;
		
		player.getCapability(DarknessHandlerProvider.CAP).ifPresent(cap ->
		{
			if (cap.get_factor() <= 0)
				return;
			
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mc.getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);
			RenderSystem.color4f(0.107f, 0.083f, 0.201f, cap.get_factor() * 0.96f);
		    _renderPortalIcon(mc, window.getGuiScaledWidth(), window.getGuiScaledHeight());
			RenderSystem.disableBlend();
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		});
	}
	
	private static void _renderPortalIcon(Minecraft mc, int screenWidth, int screenHeight)
	{
		TextureAtlasSprite portalSprite = mc.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
	    float u0 = portalSprite.getU0();
	    float v0 = portalSprite.getV0();
	    float u1 = portalSprite.getU1();
	    float v1 = portalSprite.getV1();
	    
	    Tessellator tesselator = Tessellator.getInstance();
	    BufferBuilder bufferbuilder = tesselator.getBuilder();
	    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	    bufferbuilder.vertex(0.0D, (double)screenHeight, -90.0d).uv(u0, v1).endVertex();
	    bufferbuilder.vertex((double)screenWidth, (double)screenHeight, -90.0d).uv(u1, v1).endVertex();
	    bufferbuilder.vertex((double)screenWidth, 0.0D, -90.0d).uv(u1, v0).endVertex();
	    bufferbuilder.vertex(0.0D, 0.0D, -90.0d).uv(u0, v0).endVertex();
	    tesselator.end();
	}
}