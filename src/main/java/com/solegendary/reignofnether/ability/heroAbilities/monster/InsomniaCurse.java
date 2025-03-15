package com.solegendary.reignofnether.ability.heroAbilities.monster;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
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

public class InsomniaCurse extends HeroAbility {

    // TODO:
    // [❌] PhantomUnit that can attack buildings and fixate on a target
    // [❌] Phantoms should despawn after a set number of attacks
    // [❌] Should be able to curse buildings too
    // [❌] Can have set number of charges

    public InsomniaCurse(HeroUnit hero) {
        super(hero, 3, UnitAction.INSOMNIA_CURSE, 20, 12, 0, true);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Curse of Insomnia",
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/phantom.png"),
            hotkey,
            () -> CursorClientEvents.getLeftClickAction() == UnitAction.INSOMNIA_CURSE,
            () -> rank > 0,
            () -> true,
            () -> CursorClientEvents.setLeftClickAction(UnitAction.INSOMNIA_CURSE),
            null,
            getTooltipLines(),
            this
        );
    }

    private static final float PHANTOM_DAMAGE = 5;
    private static final float PHANTOM_DAMAGE_BONUS_PER_SOUL = 5;

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.insomnia_curse"), true),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.insomnia_curse.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.insomnia_curse.tooltip2", PHANTOM_DAMAGE, PHANTOM_DAMAGE_BONUS_PER_SOUL))
        );
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of(
            fcs(I18n.get("abilities.reignofnether.insomnia_curse.rank1"), rank == 0),
            fcs(I18n.get("abilities.reignofnether.insomnia_curse.rank2"), rank == 1),
            fcs(I18n.get("abilities.reignofnether.insomnia_curse.rank3"), rank == 2)
        );
    }

    public void use(Level level, Unit unitUsing, LivingEntity targetEntity) {

    }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) {

    }
}
