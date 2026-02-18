package com.alaharranhonor.vfl.item;

import com.alaharranhonor.vfl.capability.Flashlight;
import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlashlightItem extends Item {
   private final boolean isUltra;

   public FlashlightItem(Properties pProperties, boolean isUltra) {
      super(pProperties);
      this.isUltra = isUltra;
   }

   @Override
   public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlight) -> {
         pTooltipComponents.add(Component.literal(String.format("Battery: %d%%", (int)(flashlight.getBattery() * 100.0F))).withStyle(ChatFormatting.GRAY));
      });
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
      ItemStack held = pPlayer.getItemInHand(pUsedHand);
      held.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlight) -> {
         if (flashlight.getBattery() > 0.0F) {
            flashlight.toggle();
         }
      });
      return InteractionResultHolder.sidedSuccess(held, pLevel.isClientSide());
   }

   @Override
   public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
      boolean selected = pIsSelected;
      if (!selected && pEntity instanceof Player player) {
         selected = player.getMainHandItem() == pStack;
      }
      final boolean isSelected = selected;

      pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlight) -> {
         if (flashlight.isActive() && !isSelected) {
            flashlight.setActive(false);
         }

         if (isSelected && flashlight.isActive()) {
            int charge = flashlight.getCharge();
            int drain = Flashlight.getChargeDrain(pLevel, pEntity);
            charge -= drain;
            flashlight.setCharge(charge);
            if (charge <= 0) {
               flashlight.setActive(false);
            }
         }

      });
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      return false;
   }

   @Override
   public boolean isBarVisible(ItemStack pStack) {
      return true;
   }

   @Override
   public int getBarWidth(ItemStack pStack) {
      return (Integer)pStack.getCapability(CapabilitySetup.FLASHLIGHT).map((flashlight) -> {
         return Math.round(flashlight.getBattery() * 13.0F);
      }).orElse(0);
   }

   @Override
   public int getBarColor(ItemStack pStack) {
      float color = (Float)pStack.getCapability(CapabilitySetup.FLASHLIGHT).map(IFlashlight::getBattery).orElse(0.0F);
      return Mth.hsvToRgb(color / 3.0F, 1.0F, 1.0F);
   }

   @Nullable
   public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable CompoundTag nbt) {
      return new ICapabilityProvider() {
         final LazyOptional<IFlashlight> flashlight = LazyOptional.of(() -> {
            return new Flashlight(stack, FlashlightItem.this.isUltra);
         });

         @NotNull
         public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CapabilitySetup.FLASHLIGHT.orEmpty(cap, this.flashlight);
         }
      };
   }
}
