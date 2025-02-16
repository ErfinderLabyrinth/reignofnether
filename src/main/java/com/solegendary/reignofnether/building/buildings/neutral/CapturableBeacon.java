package com.solegendary.reignofnether.building.buildings.neutral;

import com.solegendary.reignofnether.building.BuildingBlock;
import com.solegendary.reignofnether.building.BuildingBlockData;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.gamerules.GameruleClient;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
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
import java.util.List;

import static com.solegendary.reignofnether.building.BuildingUtils.getAbsoluteBlockData;

public class CapturableBeacon extends Beacon {

    public final static String buildingName = "The Beacon";
    public final static String structureName = "beacon_t5";
    public final static ResourceCost cost = ResourceCost.Building(0,0,0,0);

    public CapturableBeacon(Level level, BlockPos originPos, Rotation rotation, String ownerName) {
        super(level, originPos, rotation, ownerName);

        this.blocks = getAbsoluteBlockData(getRelativeBlockData(level), level, originPos, rotation);

        this.name = buildingName;
        this.ownerName = ownerName;
        this.portraitBlock = Blocks.BEACON;
        this.icon = new ResourceLocation("minecraft", "textures/item/nether_star.png");

        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popSupply = cost.population;
        this.buildTimeModifier = 1.0f;

        this.capturable = true;
        this.invulnerable = true;
        this.shouldDestroyOnReset = false;
        this.selfBuilding = true;

        this.startingBlockTypes.add(Blocks.NETHERITE_BLOCK);

        this.explodeChance = 0.2f;
    }

    public static ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(structureName, level);
    }

    public static AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(
                buildingName,
                new ResourceLocation("minecraft", "textures/item/nether_star.png"),
                hotkey,
                () -> BuildingClientEvents.getBuildingToPlace() == CapturableBeacon.class,
                TutorialClientEvents::isEnabled,
                () -> BuildingClientEvents.getBuildings().stream().filter(b -> b instanceof CapturableBeacon).toList().isEmpty(),
                () -> BuildingClientEvents.setBuildingToPlace(CapturableBeacon.class),
                null,
                List.of(
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.upgraded_beacon"), Style.EMPTY.withBold(true)),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.upgraded_beacon.tooltip3"), Style.EMPTY),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.upgraded_beacon.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.upgraded_beacon.tooltip2"), Style.EMPTY),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon.tooltip3"), Style.EMPTY)
                ),
                null
        );
    }
}











