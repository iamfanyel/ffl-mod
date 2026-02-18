package com.alaharranhonor.vfl.datagen.client;

import com.alaharranhonor.vfl.registry.ItemSetup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGen extends ItemModelProvider {
   public ItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
      super(output, "vfl", existingFileHelper);
   }

   protected void registerModels() {
      this.basicItem((Item)ItemSetup.FLASHLIGHT.get());
      this.basicItem((Item)ItemSetup.FLASHLIGHT_ULTRA.get());
      this.basicItem((Item)ItemSetup.HELMET_MINERS.get());
      this.basicItem((Item)ItemSetup.HELMET_MINERS_ULTRA.get());
   }
}
