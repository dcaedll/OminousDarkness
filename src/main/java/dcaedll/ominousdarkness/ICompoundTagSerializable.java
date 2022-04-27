package dcaedll.ominousdarkness;

import net.minecraft.nbt.*;

public interface ICompoundTagSerializable
{
	void serializeNBT(CompoundNBT tag);
	
	void deserializeNBT(CompoundNBT tag);
}