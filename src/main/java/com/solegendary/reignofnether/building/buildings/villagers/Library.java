package com.solegendary.reignofnether.building.buildings.villagers;

import com.mojang.math.Vector3d;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.ability.EnchantAbility;
import com.solegendary.reignofnether.ability.abilities.*;
import com.solegendary.reignofnether.building.*;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.research.researchItems.ResearchEvokerVexes;
import com.solegendary.reignofnether.research.researchItems.ResearchGrandLibrary;
import com.solegendary.reignofnether.research.researchItems.ResearchLingeringPotions;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.time.TimeClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
import com.solegendary.reignofnether.util.Faction;
import com.solegendary.reignofnether.util.MiscUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

import static com.solegendary.reignofnether.building.BuildingUtils.getAbsoluteBlockData;

public class Library extends ProductionBuilding implements RangeIndicator {

    public final static String buildingName = "Library";
    public final static String structureName = "library";
    public final static String upgradedStructureName = "library_grand";
    public final static ResourceCost cost = ResourceCosts.LIBRARY;

    public EnchantAbility autoCastEnchant = null;

    public Library(Level level, BlockPos originPos, Rotation rotation, String ownerName) {
        super(level,
            originPos,
            rotation,
            ownerName,
            getAbsoluteBlockData(getRelativeBlockData(level), level, originPos, rotation),
            false
        );
        this.name = buildingName;
        this.ownerName = ownerName;
        this.portraitBlock = Blocks.ENCHANTING_TABLE;
        this.icon = new ResourceLocation("minecraft", "textures/block/enchanting_table_top.png");

        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popSupply = cost.population;
        this.buildTimeModifier = 1.1f;

        this.canSetRallyPoint = false;

        this.startingBlockTypes.add(Blocks.OAK_LOG);
        this.startingBlockTypes.add(Blocks.SPRUCE_STAIRS);

        this.explodeChance = 0.2f;

        Ability enchantSharpness = new EnchantSharpness(this);
        this.abilities.add(enchantSharpness);
        Ability enchantQuickCharge = new EnchantQuickCharge(this);
        this.abilities.add(enchantQuickCharge);
        Ability enchantMaiming = new EnchantMaiming(this);
        this.abilities.add(enchantMaiming);
        Ability enchantMultishot = new EnchantMultishot(this);
        this.abilities.add(enchantMultishot);
        Ability enchantVigor = new EnchantVigor(this);
        this.abilities.add(enchantVigor);

        if (level.isClientSide()) {
            this.abilityButtons.add(enchantMaiming.getButton(Keybindings.keyQ));
            this.abilityButtons.add(enchantQuickCharge.getButton(Keybindings.keyW));
            this.abilityButtons.add(enchantSharpness.getButton(Keybindings.keyE));
            this.abilityButtons.add(enchantMultishot.getButton(Keybindings.keyR));
            this.abilityButtons.add(enchantVigor.getButton(Keybindings.keyT));
            this.productionButtons = Arrays.asList(
                ResearchLingeringPotions.getStartButton(this, Keybindings.keyY),
                ResearchEvokerVexes.getStartButton(this, Keybindings.keyU),
                ResearchGrandLibrary.getStartButton(this, Keybindings.keyI)
            );
        }
    }

    @Override
    public void tick(Level tickLevel) {
        super.tick(tickLevel);

        if (tickAgeAfterBuilt > 0 && tickAgeAfterBuilt % 8 == 0 && isBuilt && autoCastEnchant != null
            && autoCastEnchant.isOffCooldown()) {

            List<Mob> mobs = MiscUtil.getEntitiesWithinRange(new Vector3d(
                    this.centrePos.getX(),
                    this.centrePos.getY(),
                    this.centrePos.getZ()
                ),
                autoCastEnchant.range - 1,
                Mob.class,
                tickLevel
            ).stream().filter(e -> (
                autoCastEnchant.isCorrectUnitAndEquipment(e) && autoCastEnchant.canAfford(this)
                    && !autoCastEnchant.hasAnyEnchant(e)
            )).toList();

            if (!mobs.isEmpty()) {
                autoCastEnchant.use(tickLevel, this, mobs.get(0));
            }
        }
        if (tickLevel.isClientSide && tickAgeAfterBuilt > 0 && tickAgeAfterBuilt % 100 == 0)
            updateBorderBps();
    }

    public static final int RANGE = EnchantAbility.RANGE;
    private final Set<BlockPos> borderBps = new HashSet<>();

    private int getBorderRange() {
        return isBuilt ? RANGE : 0;
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
    public String getUpgradedName() {
        return I18n.get("buildings.villagers.reignofnether.library.upgraded");
    }

    public Faction getFaction() {
        return Faction.VILLAGERS;
    }

    public static ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(structureName, level);
    }

    public static AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(Library.buildingName,
            new ResourceLocation("minecraft", "textures/block/enchanting_table_top.png"),
            hotkey,
            () -> BuildingClientEvents.getBuildingToPlace() == Library.class,
            TutorialClientEvents::isEnabled,
            () -> BuildingClientEvents.hasFinishedBuilding(Barracks.buildingName) ||
                    ResearchClient.hasCheat("modifythephasevariance"),
            () -> BuildingClientEvents.setBuildingToPlace(Library.class),
            null,
            List.of(FormattedCharSequence.forward(I18n.get("buildings.villagers.reignofnether.library"),
                    Style.EMPTY.withBold(true)
                ),
                ResourceCosts.getFormattedCost(cost),
                FormattedCharSequence.forward("", Style.EMPTY),
                FormattedCharSequence.forward(I18n.get("buildings.villagers.reignofnether.library.tooltip1"),
                    Style.EMPTY
                ),
                FormattedCharSequence.forward(I18n.get("buildings.villagers.reignofnether.library.tooltip2"),
                    Style.EMPTY
                ),
                FormattedCharSequence.forward("", Style.EMPTY),
                FormattedCharSequence.forward(I18n.get("buildings.villagers.reignofnether.library.tooltip3"),
                    Style.EMPTY
                )
            ),
            null
        );
    }

    public void changeStructure(String newStructureName) {
        ArrayList<BuildingBlock> newBlocks = BuildingBlockData.getBuildingBlocks(newStructureName, this.getLevel());
        this.blocks = getAbsoluteBlockData(newBlocks, this.getLevel(), originPos, rotation);
        super.refreshBlocks();
    }

    // check that the flag is built based on existing placed blocks
    @Override
    public int getUpgradeLevel() {
        for (BuildingBlock block : blocks)
            if (block.getBlockState().getBlock() == Blocks.GLOWSTONE)
                return 1;
        return 0;
    }
}
