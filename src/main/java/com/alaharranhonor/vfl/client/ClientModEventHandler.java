package com.alaharranhonor.vfl.client;

import com.alaharranhonor.vfl.client.model.HelmetMinersModel;
import com.alaharranhonor.vfl.client.registry.KeybindSetup;
import java.util.Objects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   value = {Dist.CLIENT},
   modid = "vfl",
   bus = Bus.MOD
)
public class ClientModEventHandler {
   @SubscribeEvent
   public static void registerLayers(RegisterLayerDefinitions event) {
      event.registerLayerDefinition(HelmetMinersModel.LAYER_LOCATION, HelmetMinersModel::createBodyLayer);
   }

   @SubscribeEvent
   public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
      Objects.requireNonNull(event);
      KeybindSetup.init(event::register);
   }
}
