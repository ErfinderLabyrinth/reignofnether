package com.solegendary.reignofnether.building.buildings.neutral;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.NetherZone;
import com.solegendary.reignofnether.building.buildings.piglins.Portal;
import com.solegendary.reignofnether.building.buildings.placements.PortalPlacement;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;

import java.util.List;

public class NeutralTransportPortal extends Portal {

    public final static String buildingName = "Neutral Transport Portal";
    public final static String structureName = "neutral_transport_portal";

    public final static ResourceCost cost = ResourceCost.Building(0,0,0,0);

    public BlockPos destination; // for transport portals

    public NetherZone netherConversionZone = null;

    public NeutralTransportPortal() {
        super(true);
        ((Building)this).structureName = structureName;
        ((Building)this).cost = cost;

        this.name = buildingName;
        this.portraitBlock = Blocks.BLUE_GLAZED_TERRACOTTA;
        this.icon = new ResourceLocation("minecraft", "textures/block/blue_glazed_terracotta.png");
        this.startingBlockTypes.add(Blocks.LAPIS_BLOCK);

        this.invulnerable = true;
        this.shouldDestroyOnReset = false;
        this.selfBuilding = true;
    }

    @Override
    public double getMaxRange() {
        return 0;
    }

    @Override
    public double getStartingRange() {
        return 0;
    }

    @Override
    public Faction getFaction() {
        return Faction.NONE;
    }

    @Override
    public PortalPlacement createBuildingPlacement(Level level, BlockPos pos, Rotation rotation, String ownerName) {
        PortalPlacement placement = super.createBuildingPlacement(level, pos, rotation, ownerName);
        placement.portalType = PortalPlacement.PortalType.TRANSPORT;
        return placement;
    }

    public AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(NeutralTransportPortal.buildingName,
                new ResourceLocation("minecraft", "textures/block/blue_glazed_terracotta.png"),
                hotkey,
                () -> BuildingClientEvents.getBuildingToPlace() == this,
                () -> false,
                () -> true,
                () -> BuildingClientEvents.setBuildingToPlace(this),
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




















