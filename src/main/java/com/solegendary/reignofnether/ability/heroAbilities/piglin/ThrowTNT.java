package com.solegendary.reignofnether.ability.heroAbilities.piglin;

//Throws a TNT item which, when it hits the ground, becomes a full block of TNT that explodes after a delay
//Higher levels raise the damage
//Greed is Good reduces the cooldown

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class ThrowTNT extends HeroAbility {

    public ThrowTNT(HeroUnit hero) {
        super(hero, 3, UnitAction.THROW_TNT, 25, 3, 3, false);
    }


}
