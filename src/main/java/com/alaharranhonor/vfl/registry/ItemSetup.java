package com.alaharranhonor.vfl.registry;

import com.alaharranhonor.vfl.item.FlashlightItem;
import com.alaharranhonor.vfl.item.HelmetMinersItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemSetup {
   public static final DeferredRegister<Item> REGISTRY;
   public static final RegistryObject<Item> FLASHLIGHT;
   public static final RegistryObject<Item> FLASHLIGHT_ULTRA;
   public static final RegistryObject<Item> HELMET_MINERS;
   public static final RegistryObject<Item> HELMET_MINERS_ULTRA;

   public static void init(IEventBus bus) {
      REGISTRY.register(bus);
   }

   static {
      REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "vfl");
      FLASHLIGHT = REGISTRY.register("flashlight", () -> {
         return new FlashlightItem((new Properties()).durability(640), false);
      });
      FLASHLIGHT_ULTRA = REGISTRY.register("flashlight_ultra", () -> {
         return new FlashlightItem((new Properties()).durability(1280), true);
      });
      HELMET_MINERS = REGISTRY.register("helmet_miners", () -> {
         return new HelmetMinersItem(new Properties(), false);
      });
      HELMET_MINERS_ULTRA = REGISTRY.register("helmet_miners_ultra", () -> {
         return new HelmetMinersItem(new Properties(), true);
      });
   }
}
