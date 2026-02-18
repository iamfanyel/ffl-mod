package com.alaharranhonor.vfl.capability;

import com.alaharranhonor.vfl.config.ConfigData;
import com.alaharranhonor.vfl.config.ModConfigs;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

public class Flashlight implements IFlashlight {
   private static final String FLASHLIGHT_TAG = "Flashlight";
   private static final String ACTIVE_TAG = "Active";
   private static final String CHARGE_TAG = "Charge";
   private final ItemStack stack;
   private final int maxCharge;
   private final Ingredient repairItem;
   private float maxDistance;
   private float strength;
   private boolean active;
   private int charge;

   public Flashlight(ItemStack stack, boolean ultra) {
      this.stack = stack;
      this.maxCharge = ultra ? (Integer)ModConfigs.ultraBatteryLife.get() : (Integer)ModConfigs.batteryLife.get();
      this.repairItem = Ingredient.of(new ItemLike[]{ultra ? Items.REDSTONE_BLOCK : Items.REDSTONE});
      this.maxDistance = ultra ? 25.0F : 10.0F;
      this.strength = ultra ? 1.5F : 0.5F;
      this.charge = this.maxCharge;
      this.load(this.stack.getOrCreateTagElement("Flashlight"));
   }

   public boolean isActive() {
      return this.active;
   }

   public void toggle() {
      this.setActive(!this.active);
   }

   public void setActive(boolean pActive) {
      this.active = pActive;
      this.save();
   }

   public int getCharge() {
      return this.charge;
   }

   public void setCharge(int pCharge) {
      this.charge = Mth.clamp(pCharge, 0, this.maxCharge);
      this.save();
   }

   public int getMaxCharge() {
      return this.maxCharge;
   }

   public float getBattery() {
      return (float)this.charge / (float)this.maxCharge;
   }

   public Ingredient repairItem() {
      return this.repairItem;
   }

   public float getMaxDistance() {
      return this.maxDistance;
   }

   public float getStrength() {
      return this.strength;
   }

   public CompoundTag save() {
      CompoundTag tag = this.stack.getOrCreateTagElement("Flashlight");
      tag.putBoolean("Active", this.active);
      tag.putInt("Charge", this.charge);
      return tag;
   }

   public void load(CompoundTag tag) {
      this.active = tag.getBoolean("Active");
      this.charge = tag.getInt("Charge");
   }

   public static double getFlicker(Level pLevel, Entity pEntity) {
      AABB box = pEntity.getBoundingBox().inflate((double)(Integer)ModConfigs.flickerRange.get());
      double blockFlicker = pLevel.getBlockStates(box).mapToDouble((state) -> {
         Iterator var1 = ModConfigs.FLICKERS.iterator();

         ConfigData flicker;
         ResourceLocation name;
         do {
            if (!var1.hasNext()) {
               return 0.0D;
            }

            flicker = (ConfigData)var1.next();
            name = ForgeRegistries.BLOCKS.getKey(state.getBlock());
         } while(!flicker.isBlock() || !flicker.id().equals(name));

         return (double)flicker.multiplier();
      }).max().orElse(0.0D);
      double entityFlicker = pLevel.getEntities(pEntity, box).stream().mapToDouble((e) -> {
         Iterator var1 = ModConfigs.FLICKERS.iterator();

         ConfigData flicker;
         ResourceLocation name;
         do {
            if (!var1.hasNext()) {
               return 0.0D;
            }

            flicker = (ConfigData)var1.next();
            name = ForgeRegistries.ENTITY_TYPES.getKey(e.getType());
         } while(flicker.isBlock() || !flicker.id().equals(name));

         return (double)flicker.multiplier();
      }).max().orElse(0.0D);
      return Math.max(entityFlicker, blockFlicker);
   }

   public static int getChargeDrain(Level pLevel, Entity pEntity) {
      AABB box = pEntity.getBoundingBox().inflate((double)(Integer)ModConfigs.drainRange.get());
      int blockDrain = pLevel.getBlockStates(box).mapToInt((state) -> {
         Iterator var1 = ModConfigs.DRAINS.iterator();

         ConfigData drain;
         ResourceLocation name;
         do {
            if (!var1.hasNext()) {
               return 1;
            }

            drain = (ConfigData)var1.next();
            name = ForgeRegistries.BLOCKS.getKey(state.getBlock());
         } while(!drain.isBlock() || !drain.id().equals(name));

         return (int)drain.multiplier();
      }).max().orElse(1);
      int entityDrain = pLevel.getEntities(pEntity, box).stream().mapToInt((e) -> {
         Iterator var1 = ModConfigs.DRAINS.iterator();

         ConfigData drain;
         ResourceLocation name;
         do {
            if (!var1.hasNext()) {
               return 1;
            }

            drain = (ConfigData)var1.next();
            name = ForgeRegistries.ENTITY_TYPES.getKey(e.getType());
         } while(drain.isBlock() || !drain.id().equals(name));

         return (int)drain.multiplier();
      }).max().orElse(1);
      return Math.max(entityDrain, blockDrain);
   }
}
