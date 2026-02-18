package com.alaharranhonor.vfl.recipe;

import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import com.alaharranhonor.vfl.registry.RecipeSetup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ChargeRecipe extends CustomRecipe {
   public ChargeRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
      super(pId, pCategory);
   }

   @Override
   public boolean matches(CraftingContainer pContainer, Level pLevel) {
      ItemStack flashlightStack = ItemStack.EMPTY;
      ItemStack repairStack = ItemStack.EMPTY;

      for (int i = 0; i < pContainer.getContainerSize(); ++i) {
         ItemStack slotStack = pContainer.getItem(i);
         IFlashlight flashlight = slotStack.getCapability(CapabilitySetup.FLASHLIGHT).orElse(null);
         if (flashlight != null) {
            if (flashlight.getBattery() == 1.0F) {
               return false;
            }

            if (!flashlightStack.isEmpty()) {
               return false;
            }

            flashlightStack = slotStack;
         } else if (repairStack.isEmpty()) {
            repairStack = slotStack;
         }
      }

      final ItemStack repair = repairStack;
      return flashlightStack.getCapability(CapabilitySetup.FLASHLIGHT)
              .map(flashlightx -> flashlightx.repairItem().test(repair))
              .orElse(false);
   }

   @Override
   public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
      ItemStack flashlightStack = ItemStack.EMPTY;
      ItemStack redstoneStack = ItemStack.EMPTY;

      for (int i = 0; i < pContainer.getContainerSize(); ++i) {
         ItemStack slotStack = pContainer.getItem(i);
         if (!slotStack.is(Items.REDSTONE) && !slotStack.is(Items.REDSTONE_BLOCK)) {
            IFlashlight flashlight = slotStack.getCapability(CapabilitySetup.FLASHLIGHT).orElse(null);
            if (flashlight != null) {
               if (!flashlightStack.isEmpty()) {
                  return ItemStack.EMPTY;
               }

               flashlightStack = slotStack;
            }
         } else {
            if (!redstoneStack.isEmpty()) {
               return ItemStack.EMPTY;
            }

            redstoneStack = slotStack;
         }
      }

      if (!flashlightStack.isEmpty() && !redstoneStack.isEmpty()) {
         ItemStack chargedFlashlight = flashlightStack.copy();
         chargedFlashlight.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlightx) -> {
            flashlightx.setCharge(flashlightx.getMaxCharge());
         });
         return chargedFlashlight;
      } else {
         return ItemStack.EMPTY;
      }
   }

   @Override
   public boolean canCraftInDimensions(int pWidth, int pHeight) {
      return pWidth * pHeight >= 2;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return (RecipeSerializer<?>)RecipeSetup.CHARGE_RECIPE_SERIALIZER.get();
   }
}
