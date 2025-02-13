package com.solegendary.reignofnether.mixin;

import com.solegendary.reignofnether.building.BuildingUtils;
import com.solegendary.reignofnether.building.buildings.monsters.SpiderLair;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WebBlock.class)
public abstract class WebBlockMixin {
    @Inject(
            method = "entityInside",
            at = @At("HEAD"),
            cancellable = true
    )
    private void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity, CallbackInfo ci) {
        if (BuildingUtils.findBuilding(pLevel.isClientSide, pPos) instanceof SpiderLair spiderLair &&
            pEntity instanceof Unit unit && unit.getFaction() == Faction.MONSTERS)
            ci.cancel();
    }
}
