package com.solegendary.reignofnether.ability.heroAbilities.monster;

//Summons an empowered zombie pair (only one pair at a time)
//Higher levels give the units armor and weapons, then enchanted armor and weapons
//Soul Siphon raises their base size/damage/health

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
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.unit.UnitClientEvents.sendUnitCommand;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class RaiseDead extends HeroAbility {

    public RaiseDead(HeroUnit hero) {
        super(hero, 3, UnitAction.RAISE_DEAD, 60, 0, 0, false);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Raise Dead",
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/zombie.png"),
                hotkey,
                () -> false,
                () -> rank > 0,
                () -> true,
                () -> sendUnitCommand(UnitAction.RAISE_DEAD),
                null,
                getTooltipLines(),
                this
        );
    }

    private static final float BONUS_HEALTH_PER_SOUL = 2;
    private static final float BONUS_DAMAGE_PER_SOUL = 0.3f;

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.raise_dead"), true),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.raise_dead.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.raise_dead.tooltip2")),
                fcs(I18n.get("abilities.reignofnether.raise_dead.tooltip3", BONUS_HEALTH_PER_SOUL, BONUS_DAMAGE_PER_SOUL))
        );
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.raise_dead.rank1"), rank == 0),
                fcs(I18n.get("abilities.reignofnether.raise_dead.rank2"), rank == 1),
                fcs(I18n.get("abilities.reignofnether.raise_dead.rank3"), rank == 2)
        );
    }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) {

    }
}
