package com.solegendary.reignofnether.ability.heroAbilities.piglin;

//Causes other abilities to use your resources to gain improved effects
//Higher levels raise the effects but also cost of the buff
//This can be toggled this on and off

// only uses resources in multiples of 100

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceName;
import com.solegendary.reignofnether.resources.Resources;
import com.solegendary.reignofnether.resources.ResourcesServerEvents;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.unit.UnitClientEvents.sendUnitCommand;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class GreedIsGoodPassive extends HeroAbility {

    public boolean active = false;
    public int maxResourcesPerCast = 100;

    public GreedIsGoodPassive(HeroUnit hero) {
        super(hero, 3, UnitAction.TOGGLE_GREED_IS_GOOD_PASSIVE, 0, 0, 0, false);
    }

    public boolean rankUp() {
        if (super.rankUp()) {
            if (rank == 1) {
                maxResourcesPerCast = 100;
            } else if (rank == 2) {
                maxResourcesPerCast = 200;
            } else if (rank == 3) {
                maxResourcesPerCast = 300;
            }
            return true;
        }
        return false;
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Greed is Good",
                new ResourceLocation("minecraft", "textures/block/gold_block.png"),
                hotkey,
                () -> active,
                () -> rank == 0,
                () -> true,
                () -> sendUnitCommand(UnitAction.TOGGLE_GREED_IS_GOOD_PASSIVE),
                null,
                getTooltipLines(),
                this
        );
    }

    @Override
    public Button getRankUpButton() {
        return super.getRankUpButtonProtected(
                "Greed is Good",
                new ResourceLocation("minecraft", "textures/block/gold_block.png")
        );
    }

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.greed_is_good") + " " + rankString(), true),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.tooltip2", maxResourcesPerCast)),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.tooltip3"))
        );
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.greed_is_good"), true),
                fcs(I18n.get("abilities.reignofnether.level_req", getLevelRequirement()), getLevelReqStyle()),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.tooltip2", maxResourcesPerCast)),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.can_be_toggled")),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.rank1"), rank == 0),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.rank2"), rank == 1),
                fcs(I18n.get("abilities.reignofnether.greed_is_good.rank3"), rank == 2)
        );
    }

    public int checkAndSpendResources(ResourceName resName) {
        int totalSpent = 0;
        String ownerName = ((Unit) hero).getOwnerName();
        if (active && !((LivingEntity) hero).level().isClientSide()) {
            for (Resources resources : ResourcesServerEvents.resourcesList) {
                if (resources.ownerName.equals(ownerName)) {
                    for (int i = 0; i < rank; i++) {
                        Resources resToSpend = new Resources(((Unit) hero).getOwnerName(), 0, 0, 0);
                        if (resName == ResourceName.FOOD && resources.food >= 100) {
                            resToSpend.food += 100;
                            totalSpent += 100;
                        } else if (resName == ResourceName.WOOD && resources.wood >= 100) {
                            resToSpend.wood += 100;
                            totalSpent += 100;
                        } else if (resName == ResourceName.ORE && resources.ore >= 100) {
                            resToSpend.ore += 100;
                            totalSpent += 100;
                        }
                        ResourcesServerEvents.addSubtractResources(resToSpend);
                    }
                }
            }
        }
        return totalSpent;
    }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        active = !active;
    }
}
