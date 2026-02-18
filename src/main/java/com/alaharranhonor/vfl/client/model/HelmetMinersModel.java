package com.alaharranhonor.vfl.client.model;

import com.alaharranhonor.vfl.ModRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class HelmetMinersModel<T extends LivingEntity> extends HumanoidModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModRef.res("helmet_miners"), "main");

   public HelmetMinersModel(ModelPart root) {
      super(root, RenderType::entityTranslucent);
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
      PartDefinition part = mesh.getRoot();
      part.addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(0, 20).addBox(-5.0F, -8.0F, -7.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(24, 20).addBox(-1.5F, -11.0F, -6.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -10.0F, 0.0F));
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
      this.hat.visible = false;
      super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
   }
}
