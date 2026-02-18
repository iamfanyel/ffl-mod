package com.alaharranhonor.vfl.client;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.capability.Flashlight;
import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.client.registry.KeybindSetup;
import com.alaharranhonor.vfl.config.ModConfigs;
import com.alaharranhonor.vfl.network.PacketHandler;
import com.alaharranhonor.vfl.network.ServerboundToggleMinersFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import com.alaharranhonor.vfl.registry.FlashlightRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.math.Axis;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@EventBusSubscriber(
   value = {Dist.CLIENT},
   modid = "vfl"
)
public class ClientEventHandler {
   private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);
   public static PostChain flashlightEffect;

   @SubscribeEvent
   public static void triggerFlashlight(PlayerTickEvent event) {
      if (event.phase == Phase.START && event.side == LogicalSide.CLIENT) {
         if (event.player == Minecraft.getInstance().player) {
            Player player = event.player;
            Level level = player.level();
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            helmet.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlight) -> {
               while (KeybindSetup.TOGGLE_HELMET.consumeClick()) {
                  PacketHandler.INSTANCE.sendToServer(new ServerboundToggleMinersFlashlight());
                  flashlight.toggle();
               }
            });
            if (isFirstPerson()) {
               Tuple<ItemStack, Integer> active = getActiveFlashlightStack(player);
               ItemStack flashlightStack = active.getA();
               int type = active.getB();
               if (!flashlightStack.isEmpty()) {
                  flashlightStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent((flashlight) -> {
                     if (flashlight.isActive()) {
                        float battery = flashlight.getBattery();
                        double flicker = Flashlight.getFlicker(level, player);
                        if (!(Boolean)ModConfigs.enableFlicker.get()) {
                           flashShader();
                           return;
                        }

                        if (flicker == 0.0D && (double)battery > 0.15D) {
                           flashShader();
                           return;
                        }

                        double flashChance = 0.8500000238418579D;
                        if ((double)battery <= 0.15D) {
                           flashChance += (double)battery;
                        }

                        if (flicker > 0.0D) {
                           flashChance /= 1.0D + (flicker - 1.0D) * 0.1D;
                        }

                        if (level.random.nextDouble() < flashChance) {
                           flashShader();
                        }
                     }

                  });
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void renderShader(RenderLevelStageEvent event) {
      if (event.getStage() == Stage.AFTER_LEVEL) {
         if (flashlightEffect == null) {
            try {
               Minecraft mc = Minecraft.getInstance();
               PostChain postChain = new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), ModRef.res("shaders/post/flash.json"));
               postChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
               flashlightEffect = postChain;
            } catch (IOException var10) {
               ModRef.LOGGER.error("Could not load flashlight shader.", var10);
            }
         }

         Tuple<ItemStack, Integer> active = getActiveFlashlightStack(Minecraft.getInstance().player);
        ItemStack flashlightStack = active.getA();
        if (!flashlightStack.isEmpty()) {
           IFlashlight flashlight = flashlightStack.getCapability(CapabilitySetup.FLASHLIGHT).orElse(null);
           if (flashlight != null && flashlight.isActive() && isFirstPerson() && flashlightEffect != null) {
              Minecraft mc = Minecraft.getInstance();
              flashlightEffect.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
              flashlightEffect.process(event.getPartialTick());
           }
        }
      }
   }

   @SubscribeEvent
   public static void renderLight(Post event) {
      PoseStack pose = event.getPoseStack();
      Player entity = event.getEntity();
      Tuple<ItemStack, Integer> active = getActiveFlashlightStack(entity);
      ItemStack flashlight = active.getA();
      int type = active.getB();
      if (!flashlight.isEmpty()) {
         float handPos = 0.0F;
         if (type == 0) {
            handPos = -0.1F;
         }

         if (type == 1) {
            handPos = 0.1F;
         }

         pose.pushPose();
         pose.translate(0.0D, 1.0D, 0.0D);
         pose.scale(-1.0F, -1.0F, 1.0F);
         pose.pushPose();
         float xRot = entity.getXRot();
         float yRot = entity.getYRot();
         float length = 10.0F;
         float width = 1.5F;
         pose.pushPose();
         pose.translate(0.0D, type == 2 ? -1.0D + (entity.isCrouching() ? 0.25D : 0.0D) : 0.0D, 0.0D);
         pose.mulPose(Axis.YP.rotationDegrees(yRot + 180.0F));
         pose.mulPose(Axis.XP.rotationDegrees(90.0F - xRot));
         pose.scale(3.0F, 1.0F, 1.0F);
         pose.translate(handPos, 0.0F, 0.0F);
         Pose posestack$pose = pose.last();
         Matrix4f matrix4f1 = posestack$pose.pose();
         Matrix3f matrix3f1 = posestack$pose.normal();
         VertexConsumer lightConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(FlashlightRenderTypes.getFlashlight());
         shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0.0F, 0.0F);
         shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
         shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
         shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
         pose.popPose();
         pose.popPose();
         pose.popPose();
      }
   }

   private static void shineOriginVertex(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float u, float v) {
      builder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 50).uv(u + 0.5F, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240, 240).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
   }

   private static void shineLeftCornerVertex(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float x, float y, float u, float v) {
      builder.vertex(matrix, -HALF_SQRT_3 * y, x, 0.0F).color(200, 235, 255, 0).uv(u, v + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240, 240).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
   }

   private static void shineRightCornerVertex(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float x, float y, float u, float v) {
      builder.vertex(matrix, HALF_SQRT_3 * y, x, 0.0F).color(200, 235, 255, 0).uv(u + 1.0F, v + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240, 240).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
   }

   public static boolean isFirstPerson() {
      return Minecraft.getInstance().options.getCameraType().isFirstPerson();
   }

   private static Tuple<ItemStack, Integer> getActiveFlashlightStack(LivingEntity entity) {
      ItemStack mainItem = entity.getMainHandItem();
      ItemStack offItem = entity.getOffhandItem();
      ItemStack helmetItem = entity.getItemBySlot(EquipmentSlot.HEAD);
      if ((Boolean)mainItem.getCapability(CapabilitySetup.FLASHLIGHT).map(IFlashlight::isActive).orElse(false)) {
         return new Tuple<>(mainItem, entity.getMainArm() == HumanoidArm.RIGHT ? 0 : 1);
      } else if ((Boolean)offItem.getCapability(CapabilitySetup.FLASHLIGHT).map(IFlashlight::isActive).orElse(false)) {
         return new Tuple<>(offItem, entity.getMainArm() == HumanoidArm.RIGHT ? 1 : 0);
      } else {
         return (Boolean)helmetItem.getCapability(CapabilitySetup.FLASHLIGHT).map(IFlashlight::isActive).orElse(false) ? new Tuple<>(helmetItem, 2) : new Tuple<>(ItemStack.EMPTY, -1);
      }
   }

   private static void flashShader() {
      GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
   }
}
