package com.alaharranhonor.vfl.item;

import com.alaharranhonor.vfl.capability.Flashlight;
import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.client.model.HelmetMinersModel;
import com.alaharranhonor.vfl.client.registry.KeybindSetup;
import com.alaharranhonor.vfl.registry.ArmorMaterialSetup;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HelmetMinersItem extends ArmorItem {
   private final boolean isUltra;

   public HelmetMinersItem(Properties pProperties, boolean isUltra) {
      super(isUltra ? ArmorMaterialSetup.MINERS_ULTRA : ArmorMaterialSetup.MINERS, Type.HELMET, pProperties);
      this.isUltra = isUltra;
   }

   @Override
   public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      Component keyName = KeybindSetup.TOGGLE_HELMET.getKey().getDisplayName().copy().withStyle(ChatFormatting.AQUA);
      pTooltipComponents.add(keyName.copy().append(Component.literal(" to Toggle").withStyle(ChatFormatting.GRAY)));
      pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlight) -> {
         pTooltipComponents.add(Component.literal(String.format("Battery: %d%%", (int)(flashlight.getBattery() * 100.0F))).withStyle(ChatFormatting.GRAY));
      });
   }

   @Override
   public void onArmorTick(ItemStack pStack, Level pLevel, Player pPlayer) {
      pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlight) -> {
         if (flashlight.isActive()) {
            int charge = flashlight.getCharge();
            --charge;
            flashlight.setCharge(charge);
            if (charge <= 0) {
               flashlight.setActive(false);
            }
         }

      });
   }

   @Override
   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      consumer.accept(new IClientItemExtensions() {
         private HelmetMinersModel<LivingEntity> model;

         @NotNull
         public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            if (this.model == null) {
               this.model = new HelmetMinersModel(Minecraft.getInstance().getEntityModels().bakeLayer(HelmetMinersModel.LAYER_LOCATION));
            }

            return this.model;
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
            return new Flashlight(stack, HelmetMinersItem.this.isUltra);
         });

         @NotNull
         public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CapabilitySetup.FLASHLIGHT.orEmpty(cap, this.flashlight);
         }
      };
   }
}
