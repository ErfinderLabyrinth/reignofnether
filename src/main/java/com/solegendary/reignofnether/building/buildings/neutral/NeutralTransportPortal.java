package com.solegendary.reignofnether.building.buildings.neutral;

import com.solegendary.reignofnether.building.BuildingBlock;
import com.solegendary.reignofnether.building.BuildingBlockData;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.NetherZone;
import com.solegendary.reignofnether.building.buildings.piglins.Portal;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
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

public class NeutralTransportPortal extends Portal {

    public final static String buildingName = "Neutral Transport Portal";
    public final static String structureName = "neutral_transport_portal";

    public final static ResourceCost cost = ResourceCost.Building(0,0,0,0);

    public BlockPos destination; // for transport portals

    public NetherZone netherConversionZone = null;

    public NeutralTransportPortal(Level level, BlockPos originPos, Rotation rotation, String ownerName) {
        super(level, originPos, rotation, ownerName, true);

        this.name = buildingName;
        this.ownerName = ownerName;
        this.portraitBlock = Blocks.BLUE_GLAZED_TERRACOTTA;
        this.icon = new ResourceLocation("minecraft", "textures/block/blue_glazed_terracotta.png");
        this.startingBlockTypes.add(Blocks.LAPIS_BLOCK);

        this.portalType = Portal.PortalType.TRANSPORT;
        this.invulnerable = true;
        this.shouldDestroyOnReset = false;
        this.selfBuilding = true;
    }

    public static ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(structureName, level);
    }

    @Override
    public double getMaxRange() {
        return 0;
    }

    @Override
    public double getStartingRange() {
        return 0;
    }

    public static AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(NeutralTransportPortal.buildingName,
                new ResourceLocation("minecraft", "textures/block/blue_glazed_terracotta.png"),
                hotkey,
                () -> BuildingClientEvents.getBuildingToPlace() == NeutralTransportPortal.class,
                () -> false,
                () -> true,
                () -> BuildingClientEvents.setBuildingToPlace(NeutralTransportPortal.class),
                null,
                List.of(FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.neutral_transport_portal"),
                                Style.EMPTY.withBold(true)
                        ),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.neutral_transport_portal.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.neutral_transport_portal.tooltip2"), Style.EMPTY)
                ),
                null
        );
    }
}




















