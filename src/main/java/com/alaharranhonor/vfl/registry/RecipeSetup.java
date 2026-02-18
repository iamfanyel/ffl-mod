package com.alaharranhonor.vfl.registry;

import com.alaharranhonor.vfl.recipe.ChargeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSetup {
   public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS;
   public static final DeferredRegister<RecipeType<?>> TYPES;
   public static final RegistryObject<RecipeType<ChargeRecipe>> CHARGE_RECIPE_TYPE;
   public static final RegistryObject<SimpleCraftingRecipeSerializer<ChargeRecipe>> CHARGE_RECIPE_SERIALIZER;

   public static void init(IEventBus bus) {
      TYPES.register(bus);
      SERIALIZERS.register(bus);
   }

   static {
      SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "vfl");
      TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "vfl");
      CHARGE_RECIPE_TYPE = TYPES.register("charge", () -> {
         return new RecipeType<ChargeRecipe>() {
         };
      });
      CHARGE_RECIPE_SERIALIZER = SERIALIZERS.register("charge", () -> {
         return new SimpleCraftingRecipeSerializer(ChargeRecipe::new);
      });
   }
}
