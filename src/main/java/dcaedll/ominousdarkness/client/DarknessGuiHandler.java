package dcaedll.ominousdarkness.client;

import org.lwjgl.opengl.*;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;

import dcaedll.ominousdarkness.capability.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.level.block.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.gui.*;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class DarknessGuiHandler
{
	public static void init()
	{
    	OverlayRegistry.registerOverlayAbove(ForgeIngameGui.VIGNETTE_ELEMENT, "darknesstakeme.vignette", DarknessGuiHandler::_renderDarknessEffect);
	}
	
	private static void _renderDarknessEffect(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int screenWidth, int screenHeight)
	{
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		player.getCapability(DarknessHandlerProvider.CAP).ifPresent(cap ->
		{
			if (cap.get_factor() <= 0)
				return;
			
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
			RenderSystem.setShaderColor(0.087f, 0.063f, 0.181f, cap.get_factor() * 0.93f);
		    RenderSystem.setShader(GameRenderer::getPositionTexShader);
		    _renderPortalIcon(mc, screenWidth, screenHeight);
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		});
	}
	
	private static void _renderPortalIcon(Minecraft mc, int screenWidth, int screenHeight)
	{
		TextureAtlasSprite portalSprite = mc.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
	    float u0 = portalSprite.getU0();
	    float v0 = portalSprite.getV0();
	    float u1 = portalSprite.getU1();
	    float v1 = portalSprite.getV1();
	    
	    Tesselator tesselator = Tesselator.getInstance();
	    BufferBuilder bufferbuilder = tesselator.getBuilder();
	    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
	    bufferbuilder.vertex(0.0D, (double)screenHeight, -90.0d).uv(u0, v1).endVertex();
	    bufferbuilder.vertex((double)screenWidth, (double)screenHeight, -90.0d).uv(u1, v1).endVertex();
	    bufferbuilder.vertex((double)screenWidth, 0.0D, -90.0d).uv(u1, v0).endVertex();
	    bufferbuilder.vertex(0.0D, 0.0D, -90.0d).uv(u0, v0).endVertex();
	    tesselator.end();
	}
}