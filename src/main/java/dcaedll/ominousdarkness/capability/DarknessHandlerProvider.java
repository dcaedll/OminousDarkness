package dcaedll.ominousdarkness.capability;

import javax.annotation.*;

import dcaedll.ominousdarkness.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.*;

public class DarknessHandlerProvider implements ICapabilitySerializable<CompoundTag>
{
	public static final ResourceLocation RESOURCE = new ResourceLocation(OminousDarkness.MODID, "darkness_handler");
	public static final Capability<IDarknessEmbrace> CAP = CapabilityManager.get(new CapabilityToken<>() {});
	
	private final IDarknessEmbrace _cap;
	private final LazyOptional<IDarknessEmbrace> _lazyOpt;
	
	public DarknessHandlerProvider()
	{
		_cap = new DarknessHandler();
		_lazyOpt = LazyOptional.of(() -> _cap);
	}
	
	@Override
	public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side)
	{
		return CAP.orEmpty(cap, _lazyOpt);
	}

	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag tag = new CompoundTag();
		_cap.serializeNBT(tag);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		_cap.deserializeNBT(nbt);
	}
}