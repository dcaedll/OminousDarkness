package dcaedll.ominousdarkness.capability;

import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.Capability.*;

public class DarknessStorage implements IStorage<IDarknessEmbrace>
{

	@Override
	public INBT writeNBT(Capability<IDarknessEmbrace> capability, IDarknessEmbrace instance, Direction side)
	{
		CompoundNBT tag = new CompoundNBT();
		instance.serializeNBT(tag);
		return tag;
	}

	@Override
	public void readNBT(Capability<IDarknessEmbrace> capability, IDarknessEmbrace instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT)
		{
			CompoundNBT cnbt = (CompoundNBT)nbt;
			instance.deserializeNBT(cnbt);
		}
	}
}