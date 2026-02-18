package com.alaharranhonor.vfl.network;

import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent.Context;

public class ServerboundToggleMinersFlashlight {
   public static void encode(ServerboundToggleMinersFlashlight pMsg, FriendlyByteBuf pBuf) {
   }

   public static ServerboundToggleMinersFlashlight decode(FriendlyByteBuf pBuf) {
      return new ServerboundToggleMinersFlashlight();
   }

   public static class Handler {
      public static void handle(ServerboundToggleMinersFlashlight pMsg, Supplier<Context> pCtx) {
         ((Context)pCtx.get()).enqueueWork(() -> {
            ServerPlayer player = ((Context)pCtx.get()).getSender();
            player.getItemBySlot(EquipmentSlot.HEAD).getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(IFlashlight::toggle);
         });
         ((Context)pCtx.get()).setPacketHandled(true);
      }
   }
}
