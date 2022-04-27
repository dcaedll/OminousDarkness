package dcaedll.ominousdarkness;

import net.minecraft.util.*;

public class DarknessDamageSource
{
	public static final DamageSource DARKNESS
		= (new DamageSource(OminousDarkness.MODID.concat(".darkness"))).bypassArmor().bypassInvul();
}