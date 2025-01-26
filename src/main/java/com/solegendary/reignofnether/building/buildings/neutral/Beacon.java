package com.solegendary.reignofnether.building.buildings.neutral;

import com.solegendary.reignofnether.building.*;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.player.PlayerServerEvents;
import com.solegendary.reignofnether.research.researchItems.*;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.sounds.SoundAction;
import com.solegendary.reignofnether.sounds.SoundClientboundPacket;
import com.solegendary.reignofnether.time.TimeClientEvents;
import com.solegendary.reignofnether.util.Faction;
import com.solegendary.reignofnether.util.MiscUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

import static com.solegendary.reignofnether.building.BuildingUtils.getAbsoluteBlockData;

public class Beacon extends ProductionBuilding implements RangeIndicator {

    public final static String buildingName = "Beacon";
    public final static String structureName = "beacon_t0";
    public final static String structureNameT1 = "beacon_t1";
    public final static String structureNameT2 = "beacon_t2";
    public final static String structureNameT3 = "beacon_t3";
    public final static String structureNameT4 = "beacon_t4";
    public final static String structureNameT5 = "beacon_t5";
    public final static ResourceCost cost = ResourceCost.Building(0,0,0,0);

    public final static int MAX_UPGRADE_LEVEL = 5;

    public final static int TICKS_TO_WIN = 24000; // 20mins

    private MobEffect auraEffect = null;

    public boolean capturable = true;
    public boolean invulnerable = true;
    public boolean shouldDestroyOnReset = false;

    public Beacon(Level level, BlockPos originPos, Rotation rotation, String ownerName) {
        super(level, originPos, rotation, ownerName, getAbsoluteBlockData(getRelativeBlockData(level), level, originPos, rotation), false);
        this.name = buildingName;
        this.ownerName = ownerName;
        this.portraitBlock = Blocks.BEACON;
        this.icon = new ResourceLocation("minecraft", "textures/item/nether_star.png");

        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popSupply = cost.population;

        this.startingBlockTypes.add(Blocks.CHISELED_STONE_BRICKS);

        this.explodeChance = 0.2f;

        if (level.isClientSide())
            this.productionButtons = Arrays.asList(
                ResearchBeaconLevel1.getStartButton(this, Keybindings.keyQ),
                ResearchBeaconLevel2.getStartButton(this, Keybindings.keyQ),
                ResearchBeaconLevel3.getStartButton(this, Keybindings.keyQ),
                ResearchBeaconLevel4.getStartButton(this, Keybindings.keyQ),
                ResearchBeaconLevel5.getStartButton(this, Keybindings.keyQ)
            );

        if (!level.isClientSide) {
            PlayerServerEvents.sendMessageToAllPlayers("buildings.neutral.reignofnether.beacon.build_warning",
                    true, ownerName);
            SoundClientboundPacket.playSoundForAllPlayers(SoundAction.CHAT);
        }
    }

    public MobEffect getAuraEffect() { return auraEffect; }

    public void setAuraEffect(MobEffect effect) {
        // turn off the beacon
        // after delay, turn on the beacon and change the effect
        auraEffect = effect;
    }

    @Override
    public void tick(Level tickLevel) {
        super.tick(tickLevel);
        if (tickLevel.isClientSide && tickAgeAfterBuilt > 0 && tickAgeAfterBuilt % 100 == 0)
            updateBorderBps();
    }

    @Override
    public void onBuilt() {
        super.onBuilt();
        sendWarning("completed_warning");
    }

    public void sendWarning(String msg) {
        if (!level.isClientSide) {
            PlayerServerEvents.sendMessageToAllPlayersNoNewlines("");
            PlayerServerEvents.sendMessageToAllPlayersNoNewlines("buildings.neutral.reignofnether.beacon." + msg,
                    true, ownerName);
            PlayerServerEvents.sendMessageToAllPlayersNoNewlines("buildings.neutral.reignofnether.beacon.time_to_win",
                    false, ownerName, PlayerServerEvents.getBeaconWinTime(ownerName));
            PlayerServerEvents.sendMessageToAllPlayersNoNewlines("");
            SoundClientboundPacket.playSoundForAllPlayers(SoundAction.CHAT);
        }
    }

    public void activate(BeaconEffect beaconEffect) {
        // TODO: activate the beam and set the effect
        // set other abilities on cooldown
    }

    public static final int RANGE = 40;
    private final Set<BlockPos> borderBps = new HashSet<>();

    private int getBorderRange() {
        return isBuilt && isUpgraded() ? RANGE : 0;
    }

    @Override
    public void updateBorderBps() {
        if (!level.isClientSide())
            return;
        this.borderBps.clear();
        this.borderBps.addAll(MiscUtil.getRangeIndicatorCircleBlocks(centrePos,
                getBorderRange() - TimeClientEvents.VISIBLE_BORDER_ADJ, level));
    }

    @Override
    public Set<BlockPos> getBorderBps() {
        return borderBps;
    }

    @Override
    public boolean showOnlyWhenSelected() {
        return true;
    }

    @Override
    public boolean canDestroyBlock(BlockPos relativeBp) {
        BlockPos worldBp = relativeBp.offset(this.originPos);
        Block block = this.getLevel().getBlockState(worldBp).getBlock();
        return block != Blocks.BEACON;
    }

    public void changeStructure(int structureLevel) {
        String newStructureName = switch (structureLevel) {
            case 1 -> Beacon.structureNameT1;
            case 2 -> Beacon.structureNameT2;
            case 3 -> Beacon.structureNameT3;
            case 4 -> Beacon.structureNameT4;
            case 5 -> Beacon.structureNameT5;
            default -> Beacon.structureName;
        };
        ArrayList<BuildingBlock> newBlocks = BuildingBlockData.getBuildingBlocks(newStructureName, this.getLevel());
        this.blocks = getAbsoluteBlockData(newBlocks, this.getLevel(), originPos, rotation);
        super.refreshBlocks();
    }

    public Faction getFaction() {return Faction.NONE;}

    public static ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(structureName, level);
    }

    public static AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(
                buildingName,
                new ResourceLocation("minecraft", "textures/item/nether_star.png"),
                hotkey,
                () -> BuildingClientEvents.getBuildingToPlace() == Beacon.class,
                () -> false,
                () -> !BuildingClientEvents.getBuildings().stream().filter(b -> b instanceof Beacon).toList().isEmpty(),
                () -> BuildingClientEvents.setBuildingToPlace(Beacon.class),
                null,
                List.of(
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon"), Style.EMPTY.withBold(true)),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon.tooltip2"), Style.EMPTY),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon.tooltip3"), Style.EMPTY)
                ),
                null
        );
    }

    public int getUpgradeLevel() {
        for (BuildingBlock block : blocks) {
            if (block.getBlockState().getBlock() == Blocks.NETHERITE_BLOCK)
                return 5;
            if (block.getBlockState().getBlock() == Blocks.DIAMOND_BLOCK)
                return 4;
            if (block.getBlockState().getBlock() == Blocks.EMERALD_BLOCK)
                return 3;
            if (block.getBlockState().getBlock() == Blocks.GOLD_BLOCK)
                return 2;
            if (block.getBlockState().getBlock() == Blocks.IRON_BLOCK)
                return 1;
        }
        return 0;
    }

    public boolean isUpgraded() {
        return getUpgradeLevel() > 0;
    }
}











