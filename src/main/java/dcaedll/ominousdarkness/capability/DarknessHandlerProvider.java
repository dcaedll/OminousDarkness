package dcaedll.ominousdarkness.capability;

import javax.annotation.*;

import dcaedll.ominousdarkness.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.*;

public class DarknessHandlerProvider implements ICapabilitySerializable<CompoundNBT>
{
	@CapabilityInject(IDarknessEmbrace.class)
	public static final Capability<IDarknessEmbrace> CAP = null;
	public static final ResourceLocation RESOURCE = new ResourceLocation(OminousDarkness.MODID, "darkness_handler");
	
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
	public CompoundNBT serializeNBT()
	{
		return (CompoundNBT)CAP.getStorage().writeNBT(CAP, _cap, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		CAP.getStorage().readNBT(CAP, _cap, null, nbt);
	}
}