package com.solegendary.reignofnether.building.buildings.piglins;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.ability.abilities.ConnectPortal;
import com.solegendary.reignofnether.ability.abilities.DisconnectPortal;
import com.solegendary.reignofnether.ability.abilities.GotoPortal;
import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.Buildings;
import com.solegendary.reignofnether.building.buildings.placements.PortalPlacement;
import com.solegendary.reignofnether.building.production.ProductionBuilding;
import com.solegendary.reignofnether.building.production.ProductionItems;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
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

import static com.solegendary.reignofnether.building.BuildingUtils.getAbsoluteBlockData;

public class Portal extends ProductionBuilding {
    public final static float NON_NETHER_BUILD_TIME_MODIFIER = 2.0f;


    public final static String buildingName = "Basic Portal";
    public final static String structureName = "portal_basic";

    public final static String buildingNameCivilian = "Civilian Portal";
    public final static String structureNameCivilian = "portal_civilian";

    public final static String buildingNameMilitary = "Military Portal";
    public final static String structureNameMilitary = "portal_military";

    public final static String buildingNameTransport = "Transport Portal";
    public final static String structureNameTransport = "portal_transport";

    public final static ResourceCost cost = ResourceCosts.BASIC_PORTAL;

    public Portal() {
        this(false);
    }


    public Portal(boolean capitol) {
        super(structureName, cost, capitol);
        this.name = buildingName;
        this.portraitBlock = Blocks.GRAY_GLAZED_TERRACOTTA;
        this.icon = new ResourceLocation("minecraft", "textures/block/gray_glazed_terracotta.png");

        this.buildTimeModifier = 1.2f;

        this.canSetRallyPoint = false;

        this.startingBlockTypes.add(Blocks.NETHER_BRICKS);

        Ability connectPortal = new ConnectPortal();
        this.abilities.add(connectPortal, Keybindings.keyQ);
        Ability gotoPortal = new GotoPortal();
        this.abilities.add(gotoPortal, Keybindings.keyW);
        Ability disconnectPortal = new DisconnectPortal();
        this.abilities.add(disconnectPortal, Keybindings.keyE);

        this.productions.add(ProductionItems.RESEARCH_PORTAL_FOR_CIVILIAN, Keybindings.keyQ);
        this.productions.add(ProductionItems.RESEARCH_PORTAL_FOR_MILITARY, Keybindings.keyW);
        this.productions.add(ProductionItems.RESEARCH_PORTAL_FOR_TRANSPORT, Keybindings.keyE);
    }

    public Faction getFaction() {
        return Faction.PIGLINS;
    }

    @Override
    public int getUpgradeLevel(BuildingPlacement placement) {
        if (placement instanceof PortalPlacement portalPlacement) {
            return portalPlacement.portalType != PortalPlacement.PortalType.BASIC ? 1 : 0;
        }else {
            return 0;
        }
    }

    @Override
    public PortalPlacement createBuildingPlacement(Level level, BlockPos pos, Rotation rotation, String ownerName) {
        return new PortalPlacement(this, level, pos, rotation, ownerName, getCulledBlocks(getAbsoluteBlockData(getRelativeBlockData(level), level, pos, rotation), level), false);
    }

    public AbilityButton getBuildButton(Keybinding hotkey) {
        ResourceLocation key = ReignOfNetherRegistries.BUILDING.getKey(this);
        String name = I18n.get("buildings." + getFaction().name().toLowerCase() + "." + key.getNamespace() + "." + key.getPath());
        return new AbilityButton(name,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/portal.png"),
            hotkey,
            () -> BuildingClientEvents.getBuildingToPlace() == Buildings.PORTAL,
            () -> false,
            () -> BuildingClientEvents.hasFinishedBuilding(Buildings.CENTRAL_PORTAL) || ResearchClient.hasCheat(
                "modifythephasevariance"),
            () -> BuildingClientEvents.setBuildingToPlace(Buildings.PORTAL),
            null,
            List.of(FormattedCharSequence.forward(I18n.get("buildings.piglins.reignofnether.portal"),
                    Style.EMPTY.withBold(true)
                ),
                ResourceCosts.getFormattedCost(cost),
                FormattedCharSequence.forward("", Style.EMPTY),
                FormattedCharSequence.forward(I18n.get("buildings.piglins.reignofnether.portal.tooltip1"), Style.EMPTY),
                FormattedCharSequence.forward(I18n.get("buildings.piglins.reignofnether.portal.tooltip2"), Style.EMPTY),
                FormattedCharSequence.forward("", Style.EMPTY),
                FormattedCharSequence.forward(I18n.get("buildings.piglins.reignofnether.portal.tooltip3"), Style.EMPTY)
            ),
            null
        );
    }

    public double getMaxRange() {
        return 20;
    }

    public double getStartingRange() {
        return 3;
    }
}




















