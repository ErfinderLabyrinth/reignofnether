package com.solegendary.reignofnether.mixin;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.BuildingUtils;
import com.solegendary.reignofnether.building.buildings.neutral.Beacon;
import com.solegendary.reignofnether.building.buildings.neutral.EndPortal;
import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frustum.class)
public class FrustumMixin {
//    // I have no idea why this is needed but without it the game freezes and gets stuck inside
//    // this function forever a few seconds after activating orthoView
//    @Inject(
//            method = "cubeInFrustum",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void cubeCompletelyInFrustum(
//            double pMinX, double pMinY, double pMinZ, double pMaxX, double pMaxY, double pMaxZ, CallbackInfoReturnable<Boolean> cir
//    ) {
//        if (OrthoviewClientEvents.isEnabled()) {
//            cir.setReturnValue(true);
//        }
//    }

    // see IForgeBlockEntity.getRenderBoundingBox()
    @Inject(
            method = "isVisible(Lnet/minecraft/world/phys/AABB;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void isVisible(AABB aabb, CallbackInfoReturnable<Boolean> cir) {
        // aabb is infinite only for some block entities: structure, beacon, end portal
        boolean infAABB = aabb.equals(IForgeBlockEntity.INFINITE_EXTENT_AABB);

        Player player = Minecraft.getInstance().player;
        float zoom = Math.max(30, OrthoviewClientEvents.getZoom()) * 2;

        if (player != null && OrthoviewClientEvents.isEnabled() && infAABB) {
            for (Building building : BuildingClientEvents.getBuildings()) {
                if (building instanceof Beacon beacon ||
                    building instanceof EndPortal endPortal) {
                    BlockPos equalYBp = new BlockPos(player.getOnPos().getX(), building.centrePos.getY(), player.getOnPos().getZ());
                    if (building.centrePos.distSqr(equalYBp) < (zoom * zoom))
                        cir.setReturnValue(true);
                }
            }
        }
    }
}