package dcaedll.ominousdarkness.net;

import java.util.function.*;

import dcaedll.ominousdarkness.*;
import net.minecraft.network.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fmllegacy.network.*;

public class DarknessPacket
{
	public float factor;
	
	public DarknessPacket()
	{
		
	}
	
	public DarknessPacket(float factor)
	{
		this.factor = factor;
	}
	
	public static void encode(DarknessPacket packet, FriendlyByteBuf buf)
	{
		buf.writeFloat(packet.factor);
	}
	
	public static DarknessPacket decode(FriendlyByteBuf buf)
	{
		DarknessPacket packet = new DarknessPacket();
		packet.factor = buf.readFloat();
		return packet;
	}
	
	public static void handle(DarknessPacket packet, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
			{
				DarknessProcessor.receiveDarknessUpdate(packet.factor);
			});
		});
		ctx.get().setPacketHandled(true);
	}
}