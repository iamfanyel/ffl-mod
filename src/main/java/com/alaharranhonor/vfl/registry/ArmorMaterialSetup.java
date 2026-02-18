package com.alaharranhonor.vfl.registry;

import java.util.EnumMap;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable.EnumCodec;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public enum ArmorMaterialSetup implements StringRepresentable, ArmorMaterial {
   MINERS("vfl:miners", 15, Util.make(new EnumMap<>(Type.class), (map) -> {
      map.put(Type.BOOTS, 2);
      map.put(Type.LEGGINGS, 5);
      map.put(Type.CHESTPLATE, 6);
      map.put(Type.HELMET, 2);
   }), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
      return Ingredient.of(new ItemLike[]{Items.IRON_INGOT});
   }),
   MINERS_ULTRA("vfl:miners_ultra", 15, Util.make(new EnumMap<>(Type.class), (map) -> {
      map.put(Type.BOOTS, 2);
      map.put(Type.LEGGINGS, 5);
      map.put(Type.CHESTPLATE, 6);
      map.put(Type.HELMET, 2);
   }), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
      return Ingredient.of(new ItemLike[]{Items.IRON_INGOT});
   });

   private static final EnumMap<Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(Type.class), (p_266653_) -> {
      p_266653_.put(Type.BOOTS, 13);
      p_266653_.put(Type.LEGGINGS, 15);
      p_266653_.put(Type.CHESTPLATE, 16);
      p_266653_.put(Type.HELMET, 11);
   });
   private final String name;
   private final int durabilityMultiplier;
   private final EnumMap<Type, Integer> protectionFunctionForType;
   private final int enchantmentValue;
   private final SoundEvent sound;
   private final float toughness;
   private final float knockbackResistance;
   private final LazyLoadedValue<Ingredient> repairIngredient;

   private ArmorMaterialSetup(String pName, int pDurabilityMultiplier, EnumMap<Type, Integer> pProtectionFunctionForType, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient) {
      this.name = pName;
      this.durabilityMultiplier = pDurabilityMultiplier;
      this.protectionFunctionForType = pProtectionFunctionForType;
      this.enchantmentValue = pEnchantmentValue;
      this.sound = pSound;
      this.toughness = pToughness;
      this.knockbackResistance = pKnockbackResistance;
      this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
   }

   @Override
   public int getDurabilityForType(Type pType) {
      return (Integer)HEALTH_FUNCTION_FOR_TYPE.get(pType) * this.durabilityMultiplier;
   }

   @Override
   public int getDefenseForType(Type pType) {
      return (Integer)this.protectionFunctionForType.get(pType);
   }

   @Override
   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   @Override
   public SoundEvent getEquipSound() {
      return this.sound;
   }

   @Override
   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public float getToughness() {
      return this.toughness;
   }

   @Override
   public float getKnockbackResistance() {
      return this.knockbackResistance;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   // $FF: synthetic method
   private static ArmorMaterialSetup[] $values() {
      return new ArmorMaterialSetup[]{MINERS, MINERS_ULTRA};
   }
}
