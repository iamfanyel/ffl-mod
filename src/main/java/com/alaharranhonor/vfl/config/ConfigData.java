package com.alaharranhonor.vfl.config;

import net.minecraft.resources.ResourceLocation;

public record ConfigData(ResourceLocation id, boolean isBlock, float multiplier) {
   public ConfigData(ResourceLocation id, boolean isBlock, float multiplier) {
      this.id = id;
      this.isBlock = isBlock;
      this.multiplier = multiplier;
   }

   public ResourceLocation id() {
      return this.id;
   }

   public boolean isBlock() {
      return this.isBlock;
   }

   public float multiplier() {
      return this.multiplier;
   }
}
