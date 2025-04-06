//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.solegendary.reignofnether.building;

import com.solegendary.reignofnether.ability.Abilities;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public abstract class Building {
    private static final int BASE_MS_PER_BUILD = 500;
    public final float MELEE_DAMAGE_MULTIPLIER = 0.2F;
    public String name;
    public String structureName;
    public ResourceLocation icon;
    public final boolean isCapitol;
    public int msToNextBuild = 500;
    public final float MIN_BLOCKS_PERCENT = 0.5F;
    protected float explodeChance = 0.3F;
    protected float explodeRadius = 2.0F;
    protected float fireThreshold = 0.75F;
    protected float buildTimeModifier = 1.0F;
    protected float repairTimeModifier = 1.25F;
    protected int highestBlockCountReached = 2;
    public Block portraitBlock;
    public boolean canAcceptResources = false;
    public int serverBlocksPlaced = 1;
    private int totalBlocksEverBroken = 0;
    private long ticksToExtinguish = 0L;
    private final long TICKS_TO_EXTINGUISH = 100L;
    private final long TICKS_TO_SPAWN_ANIMALS_MAX = 1800L;
    private long ticksToSpawnAnimals = 0L;
    private final int MAX_ANIMALS = 8;
    private final int ANIMAL_SPAWN_BLOCK_RANGE = 70;
    private final int ANIMAL_SPAWN_RANGE_MIN = 15;
    private final int ANIMAL_SPAWN_RANGE_MAX = 80;

    public int captureRange = 20;
    public boolean capturable = false;
    public boolean invulnerable = false;
    public boolean shouldDestroyOnReset = true;

    public ResourceCost cost;
    public boolean selfBuilding = false;
    public final ArrayList<Block> startingBlockTypes = new ArrayList();
    protected final Abilities abilities = new Abilities();

    public Abilities getAbilities() {
        return this.abilities;
    }

    public Building(String structureName, ResourceCost cost, boolean isCapitol) {
        this.structureName = structureName;
        this.cost = cost;
        this.isCapitol = isCapitol;
    }

    public float getMeleeDamageMult() {
        return 0.2F;
    }

    public ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(this.structureName, level);
    }

    public BuildingPlacement createBuildingPlacement(Level level, BlockPos pos, Rotation rotation, String ownerName) {
        return new BuildingPlacement(this, level, pos, rotation, ownerName, BuildingUtils.getAbsoluteBlockData(this.getRelativeBlockData(level), level, pos, rotation), this.isCapitol);
    }

    protected static ArrayList<BuildingBlock> getCulledBlocks(ArrayList<BuildingBlock> blocks, Level level) {
        blocks.removeIf((b) -> shouldCullBlock(new BlockPos(0, 0, 0), b, level));
        return blocks;
    }

    public static boolean shouldCullBlock(BlockPos originPos, BuildingBlock b, Level level) {
        BlockState bs = b.getBlockState();

        boolean isFenceOrAir = b.getBlockState().getBlock() instanceof AirBlock ||
                b.getBlockState().getBlock() instanceof FenceBlock;
        BlockPos bp = b.getBlockPos().offset(originPos);

        // if the block in the world matches this exactly, don't cull it, instead just consider it to be our block too
        BlockState bsWorld = level.getBlockState(bp);

        if (bsWorld.getBlock() == Blocks.OBSIDIAN)
            return false;
        if (bsWorld.equals(bs))
            return false;
        if ((bsWorld.isAir() || !bsWorld.getFluidState().isEmpty()) && !isFenceOrAir)
            return false;

        // cull if overlaps another bridge block that isn't built yet
        if (BuildingUtils.isPosInsideAnyBuilding(level.isClientSide, bp))
            return true;

        // cull if fence is adjacent to another solid block (or a bridge block, even if air)
        for (BlockPos bpAdj : List.of(bp.north(), bp.south(), bp.east(), bp.west())) {
            BlockState bsWorldAdj = level.getBlockState(bpAdj);
            if (isFenceOrAir && !bsWorldAdj.isAir() && BuildingUtils.isPosInsideAnyBuilding(level.isClientSide, bpAdj))
                return true;
        }
        return level.getBlockState(bp).isSolid() || (isFenceOrAir && level.getBlockState(bp.below()).isSolid());
    }

    public abstract Faction getFaction();

    public int getUpgradeLevel(BuildingPlacement placement) {
        return 0;
    }

    public abstract AbilityButton getBuildButton(Keybinding var1);
}
