package com.solegendary.reignofnether.building.buildings.neutral;

import com.mojang.math.Vector3d;
import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.EnchantAbility;
import com.solegendary.reignofnether.building.*;
import com.solegendary.reignofnether.building.buildings.villagers.TownCentre;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.time.TimeClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialStage;
import com.solegendary.reignofnether.util.Faction;
import com.solegendary.reignofnether.util.MiscUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

import static com.solegendary.reignofnether.building.BuildingUtils.getAbsoluteBlockData;

public class HealingFountain extends Building implements RangeIndicator {

    public final static String buildingName = "Healing Fountain";
    public final static String structureName = "healing_fountain";
    public final static ResourceCost cost = ResourceCost.Building(0,0,0,0);

    private final ArrayList<BuildingBlock> waterBlocks;

    public HealingFountain(Level level, BlockPos originPos, Rotation rotation, String ownerName) {
        super(level, originPos, rotation, ownerName, getAbsoluteBlockData(getRelativeBlockData(level), level, originPos, rotation), false);
        this.name = buildingName;
        this.ownerName = ownerName;
        this.portraitBlock = Blocks.PRISMARINE;
        this.icon = new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/prismarine.png");

        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popSupply = cost.population;
        this.buildTimeModifier = 0.8f;

        this.startingBlockTypes.add(Blocks.POLISHED_ANDESITE);
        this.startingBlockTypes.add(Blocks.STONE_BRICK_STAIRS);
        this.startingBlockTypes.add(Blocks.GRASS_BLOCK);
        this.startingBlockTypes.add(Blocks.SPRUCE_TRAPDOOR);

        this.capturable = false;
        this.invulnerable = true;
        this.shouldDestroyOnReset = false;

        List<BuildingBlock> wbs = blocks.stream().filter(b -> b.getBlockPos().getY() < centrePos.getY() &&
                b.getBlockState().getBlock() == Blocks.WATER).toList();
        this.waterBlocks = new ArrayList<>(wbs);
    }

    @Override
    public void onBuilt() {
        super.onBuilt();
    }

    public void tick(Level tickLevel) {
        super.tick(tickLevel);

        List<LivingEntity> nearbyEntities = MiscUtil.getEntitiesWithinRange(
                new Vector3d(this.centrePos.getX(), this.centrePos.getY(), this.centrePos.getZ()),
                RANGE,
                LivingEntity.class,
                this.level);

        for (LivingEntity le : nearbyEntities)
            if (tickAgeAfterBuilt % 100 == 0) // only 1hp/4s
                le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));

        // spawn random healing particle
        if (!waterBlocks.isEmpty()) {
            Collections.shuffle(waterBlocks);
            int col = 16262179; // red healing effect
            BlockPos bp = waterBlocks.get(0).getBlockPos();
            double d0 = (double)(col >> 16 & 255) / 255.0;
            double d1 = (double)(col >> 8 & 255) / 255.0;
            double d2 = (double)(col >> 0 & 255) / 255.0;
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, bp.getX(), bp.getY() + 1, bp.getZ(), d0, d1, d2);
        }
    }

    public Faction getFaction() {return Faction.NONE;}

    public static ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(structureName, level);
    }

    public static AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(
            HealingFountain.buildingName,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/prismarine.png"),
            hotkey,
            () -> BuildingClientEvents.getBuildingToPlace() == HealingFountain.class,
            () -> false,
            () -> true,
            () -> BuildingClientEvents.setBuildingToPlace(HealingFountain.class),
            null,
            List.of(
                FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.healing_fountain"), Style.EMPTY.withBold(true)),
                FormattedCharSequence.forward("", Style.EMPTY),
                FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.healing_fountain.tooltip1"), Style.EMPTY),
                FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.healing_fountain.tooltip2"), Style.EMPTY)
            ),
            null
        );
    }

    public static final int RANGE = 20;
    private final Set<BlockPos> borderBps = new HashSet<>();

    @Override
    public void updateBorderBps() {
        if (!level.isClientSide())
            return;
        this.borderBps.clear();
        this.borderBps.addAll(MiscUtil.getRangeIndicatorCircleBlocks(centrePos,
                RANGE - TimeClientEvents.VISIBLE_BORDER_ADJ, level));
    }

    @Override
    public Set<BlockPos> getBorderBps() {
        return borderBps;
    }

    @Override
    public boolean showOnlyWhenSelected() {
        return true;
    }

}
