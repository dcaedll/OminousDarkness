package dcaedll.ominousdarkness.net;

import dcaedll.ominousdarkness.*;
import net.minecraft.resources.*;
import net.minecraftforge.fmllegacy.network.*;
import net.minecraftforge.fmllegacy.network.simple.*;

public class PacketHandler
{
	private static int _packetId = 0;
	
	public static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(OminousDarkness.MODID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	
	public static void init()
	{
		CHANNEL_INSTANCE.registerMessage(_packetId++, DarknessPacket.class, DarknessPacket::encode, DarknessPacket::decode, DarknessPacket::handle);
	}
}