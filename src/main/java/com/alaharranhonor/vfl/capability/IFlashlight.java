package com.alaharranhonor.vfl.capability;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IFlashlight {
   boolean isActive();

   void toggle();

   void setActive(boolean var1);

   int getCharge();

   void setCharge(int var1);

   int getMaxCharge();

   float getBattery();

   Ingredient repairItem();

   float getMaxDistance();

   float getStrength();
}
