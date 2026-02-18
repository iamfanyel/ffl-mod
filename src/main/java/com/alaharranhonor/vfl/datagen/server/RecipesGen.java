package com.alaharranhonor.vfl.datagen.server;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.registry.ItemSetup;
import com.alaharranhonor.vfl.registry.RecipeSetup;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

public class RecipesGen extends RecipeProvider {
   public RecipesGen(PackOutput output) {
      super(output);
   }

   @Override
   protected void buildRecipes(Consumer<FinishedRecipe> writer) {
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ItemSetup.FLASHLIGHT.get())
              .pattern("G")
              .pattern("S")
              .pattern("R")
              .define('G', Tags.Items.DUSTS_GLOWSTONE)
              .define('S', Tags.Items.RODS_WOODEN)
              .define('R', Tags.Items.DUSTS_REDSTONE)
              .group("vfl")
              .unlockedBy("has_glowstone_dust", has(Tags.Items.DUSTS_GLOWSTONE))
              .save(writer);

      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ItemSetup.FLASHLIGHT_ULTRA.get())
              .pattern("G")
              .pattern("S")
              .pattern("R")
              .define('G', Items.GLOWSTONE)
              .define('S', Tags.Items.RODS_WOODEN)
              .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
              .group("vfl")
              .unlockedBy("has_glowstone", has(Items.GLOWSTONE))
              .save(writer);

      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ItemSetup.HELMET_MINERS.get())
              .pattern(" F ")
              .pattern("SHS")
              .pattern(" S ")
              .define('F', (ItemLike)ItemSetup.FLASHLIGHT.get())
              .define('S', Tags.Items.STRING)
              .define('H', Items.IRON_HELMET)
              .group("vfl")
              .unlockedBy("has_flashlight", has((ItemLike)ItemSetup.FLASHLIGHT.get()))
              .save(writer);

      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ItemSetup.HELMET_MINERS_ULTRA.get())
              .pattern(" F ")
              .pattern("SHS")
              .pattern(" S ")
              .define('F', (ItemLike)ItemSetup.FLASHLIGHT_ULTRA.get())
              .define('S', Tags.Items.STRING)
              .define('H', Items.IRON_HELMET)
              .group("vfl")
              .unlockedBy("has_flashlight_ultra", has((ItemLike)ItemSetup.FLASHLIGHT_ULTRA.get()))
              .save(writer);

      SpecialRecipeBuilder.special((RecipeSerializer<? extends net.minecraft.world.item.crafting.CraftingRecipe>)RecipeSetup.CHARGE_RECIPE_SERIALIZER.get())
              .save(writer, ModRef.res("charging").toString());
   }
}
