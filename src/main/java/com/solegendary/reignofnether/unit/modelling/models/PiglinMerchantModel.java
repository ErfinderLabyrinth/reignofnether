// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package com.solegendary.reignofnether.unit.modelling.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.unit.modelling.animations.NecromancerAnimations;
import com.solegendary.reignofnether.unit.modelling.animations.PiglinMerchantAnimations;
import com.solegendary.reignofnether.unit.units.monsters.NecromancerUnit;
import com.solegendary.reignofnether.unit.units.piglins.PiglinMerchantUnit;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class PiglinMerchantModel<T extends Entity> extends KeyframeHierarchicalModel<T> {

	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ReignOfNether.MOD_ID, "piglin_merchant_layer"), "main");

	private final ModelPart main;
	private final ModelPart head;
	private final ModelPart earLeft;
	private final ModelPart earRight;
	private final ModelPart pack;
	private final ModelPart bone;
	private final ModelPart chainRight;
	private final ModelPart chainLeft;
	private final ModelPart wart;
	private final ModelPart wart2;
	private final ModelPart wart3;
	private final ModelPart body;
	private final ModelPart armRight;
	private final ModelPart armLeft;
	private final ModelPart legLeft;
	private final ModelPart legRight;

	public PiglinMerchantModel(ModelPart root) {
		this.main = root.getChild("main");
		this.head = this.main.getChild("head");
		this.earLeft = this.head.getChild("earLeft");
		this.earRight = this.head.getChild("earRight");
		this.pack = this.main.getChild("pack");
		this.bone = this.pack.getChild("bone");
		this.chainRight = this.bone.getChild("chainRight");
		this.chainLeft = this.bone.getChild("chainLeft");
		this.wart = this.bone.getChild("wart");
		this.wart2 = this.bone.getChild("wart2");
		this.wart3 = this.bone.getChild("wart3");
		this.body = this.main.getChild("body");
		this.armRight = this.main.getChild("armRight");
		this.armLeft = this.main.getChild("armLeft");
		this.legLeft = this.main.getChild("legLeft");
		this.legRight = this.main.getChild("legRight");
	}

	@Override
	public @NotNull ModelPart root() {
		return this.main;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, -2.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-5.0F, -6.0F, -9.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(61, 2).addBox(-2.0F, -2.0F, -10.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 0).addBox(-6.0F, -7.0F, -10.0F, 12.0F, 9.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(32, 5).addBox(-3.0F, 0.0F, -10.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 2).addBox(2.0F, 0.0F, -10.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(72, 0).mirror().addBox(-1.9239F, 0.3827F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.2F, -4.0F, -6.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(72, 0).addBox(0.2F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 18).addBox(-0.8F, 3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, -4.0F, -6.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition pack = main.addOrReplaceChild("pack", CubeListBuilder.create().texOffs(76, 82).mirror().addBox(-10.3333F, -7.8333F, -4.8333F, 21.0F, 15.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(73, 110).addBox(-8.3333F, 7.1667F, -4.8333F, 16.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(81, 65).addBox(-11.8333F, -14.8333F, -4.8333F, 24.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3333F, -4.1667F, 10.8333F, 0.0873F, 0.0F, 0.0F));

		PartDefinition bone = pack.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(149, 47).mirror().addBox(14.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(82, 53).addBox(-16.0F, -1.0F, -1.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(149, 47).addBox(-18.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6667F, -10.8333F, -5.8333F, -0.0873F, 0.0F, 0.0F));

		PartDefinition chainRight = bone.addOrReplaceChild("chainRight", CubeListBuilder.create(), PartPose.offset(5.5F, 1.0F, -1.0F));

		PartDefinition chain_r1 = chainRight.addOrReplaceChild("chain_r1", CubeListBuilder.create().texOffs(136, 6).addBox(0.0F, -1.0F, -1.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 1.0F, -0.48F, 0.0F, 0.0F));

		PartDefinition chainLeft = bone.addOrReplaceChild("chainLeft", CubeListBuilder.create(), PartPose.offset(-7.5F, 1.0F, -1.0F));

		PartDefinition chain_r2 = chainLeft.addOrReplaceChild("chain_r2", CubeListBuilder.create().texOffs(136, 6).addBox(0.0F, -1.0F, -1.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 1.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition wart = bone.addOrReplaceChild("wart", CubeListBuilder.create().texOffs(104, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 1.0F, 0.0F));

		PartDefinition wart_r1 = wart.addOrReplaceChild("wart_r1", CubeListBuilder.create().texOffs(104, 0).addBox(-5.0F, -6.0F, -1.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 6.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition wart2 = bone.addOrReplaceChild("wart2", CubeListBuilder.create().texOffs(122, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, 1.0F, 0.0F));

		PartDefinition wart_r2 = wart2.addOrReplaceChild("wart_r2", CubeListBuilder.create().texOffs(122, 0).addBox(-5.0F, -6.0F, -1.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 6.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition wart3 = bone.addOrReplaceChild("wart3", CubeListBuilder.create().texOffs(116, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, 1.0F, 0.0F));

		PartDefinition wart_r3 = wart3.addOrReplaceChild("wart_r3", CubeListBuilder.create().texOffs(116, 0).addBox(-5.0F, -6.0F, -1.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 6.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 24).addBox(-8.0F, -16.0F, -4.0F, 16.0F, 19.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(12, 76).addBox(-8.0F, -3.0F, -4.0F, 16.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 3.0F));

		PartDefinition armRight = main.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(89, 27).addBox(-1.0F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -5.0F, 1.5F));

		PartDefinition armLeft = main.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(89, 27).mirror().addBox(-4.0F, -1.0F, -3.0F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, -4.0F, 2.0F));

		PartDefinition legLeft = main.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(111, 30).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 7.0F, 3.0F));

		PartDefinition legRight = main.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(111, 30).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 10.0F, 3.0F));

		return LayerDefinition.create(meshdefinition, 160, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		PiglinMerchantUnit merchant = ((PiglinMerchantUnit) entity);

		AttributeInstance ms = merchant.getAttribute(Attributes.MOVEMENT_SPEED);
		if (ms == null)
			return;
		float speed = (float) ms.getValue() * 10;

		// any once-off animation like attack or cast spell
		if (merchant.activeAnimDef != null && merchant.activeAnimState != null && merchant.animateTicks > 0) {
			restartThenAnimate(merchant, merchant.activeAnimState, merchant.activeAnimDef, ageInTicks, merchant.animateScale);
		}
		// walk animation
		else if (!entity.isInWaterOrBubble() && limbSwingAmount > 0.001f) {
			restart(merchant, merchant.walkAnimState, PiglinMerchantAnimations.WALK, ageInTicks);
			animateWalk(PiglinMerchantAnimations.WALK, limbSwing, limbSwingAmount, speed, speed);
		}
		// idle animation
		else {
			restartThenAnimate(merchant, merchant.idleAnimState, PiglinMerchantAnimations.IDLE, ageInTicks);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}