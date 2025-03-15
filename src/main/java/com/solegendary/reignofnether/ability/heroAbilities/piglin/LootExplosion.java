package com.solegendary.reignofnether.ability.heroAbilities.piglin;

//Throws out a huge amount of random weapons and armour that your units automatically equip upon pickup
//All equipment has limited durability
//Greed is Good raises the amount of equipment thrown

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class LootExplosion extends HeroAbility {

    public LootExplosion(HeroUnit hero) {
        super(hero, 3, UnitAction.LOOT_EXPLOSION, 240, 0, 0, false);
    }


}
