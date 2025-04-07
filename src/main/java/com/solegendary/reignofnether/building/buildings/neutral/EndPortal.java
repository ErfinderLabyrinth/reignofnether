package com.solegendary.reignofnether.building.buildings.neutral;

import com.solegendary.reignofnether.building.*;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialStage;
import com.solegendary.reignofnether.unit.units.neutral.EndermanProd;
import com.solegendary.reignofnether.unit.units.villagers.PillagerProd;
import com.solegendary.reignofnether.unit.units.villagers.VindicatorProd;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.solegendary.reignofnether.building.BuildingUtils.getAbsoluteBlockData;

public class EndPortal extends ProductionBuilding {

    public final static String buildingName = "End Portal";
    public final static String structureName = "end_portal";
    public final static ResourceCost cost = ResourceCost.Building(0,0,0,0);

    public EndPortal(Level level, BlockPos originPos, Rotation rotation, String ownerName) {
        super(level, originPos, rotation, ownerName, getAbsoluteBlockData(getRelativeBlockData(level), level, originPos, rotation), false);
        this.name = buildingName;
        this.ownerName = ownerName;
        this.portraitBlock = Blocks.END_PORTAL_FRAME;
        this.icon = new ResourceLocation("minecraft", "textures/block/end_portal_frame_top.png");

        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popSupply = cost.population;
        this.selfBuilding = true;

        this.capturable = true;
        this.invulnerable = true;
        this.shouldDestroyOnReset = false;

        this.startingBlockTypes.add(Blocks.DARK_PRISMARINE_STAIRS);

        this.explodeChance = 0.2f;

        updateButtons();
    }

    public void updateButtons() {
        if (level.isClientSide()) {
            this.productionButtons = Arrays.asList(
                EndermanProd.getStartButton(this, Keybindings.keyQ)
            );
        }
    }

    @Override
    public void onBuilt() {
        super.onBuilt();
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(centrePos.above(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().north(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().south(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().east(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().west(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().north().east(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().north().west(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().south().east(), Blocks.END_PORTAL.defaultBlockState());
            level.setBlockAndUpdate(centrePos.above().south().west(), Blocks.END_PORTAL.defaultBlockState());
        }
    }

    public Faction getFaction() {return Faction.NONE;}

    public static ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(structureName, level);
    }

    public static AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(
                buildingName,
                new ResourceLocation("minecraft", "textures/block/end_portal_frame_top.png"),
                hotkey,
                () -> BuildingClientEvents.getBuildingToPlace() == EndPortal.class,
                () -> false,
                () -> true,
                () -> BuildingClientEvents.setBuildingToPlace(EndPortal.class),
                null,
                List.of(
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.end_portal"), Style.EMPTY.withBold(true)),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.end_portal.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.end_portal.tooltip2"), Style.EMPTY)
                ),
                null
        );
    }
}
