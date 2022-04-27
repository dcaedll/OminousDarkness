package dcaedll.ominousdarkness;

import net.minecraft.nbt.*;

public interface ICompoundTagSerializable
{
	void serializeNBT(CompoundTag tag);
	
	void deserializeNBT(CompoundTag tag);
}