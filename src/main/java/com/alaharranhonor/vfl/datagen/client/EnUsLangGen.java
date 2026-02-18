package com.alaharranhonor.vfl.datagen.client;

import com.alaharranhonor.vfl.registry.ItemSetup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

public class EnUsLangGen extends LanguageProvider {
   public EnUsLangGen(PackOutput output) {
      super(output, "vfl", "en_us");
   }

   protected void addTranslations() {
      this.add((Item)ItemSetup.FLASHLIGHT.get(), "Flashlight");
      this.add((Item)ItemSetup.FLASHLIGHT_ULTRA.get(), "Ultra Flashlight");
      this.add((Item)ItemSetup.HELMET_MINERS.get(), "Miner's Helmet");
      this.add((Item)ItemSetup.HELMET_MINERS_ULTRA.get(), "Ultra Miner's Helmet");
      this.add("itemGroup.vfl.main", "Fanyel's Flashlights");
      this.add("key.vfl.toggle_helmet", "Toggle Miner's Helmet Flashlight");
      this.add("key.vfl.category", "Fanyel's Flashlights");
   }
}
