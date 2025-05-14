package com.solegendary.reignofnether.unit.interfaces;

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.hero.HeroClientboundPacket;
import com.solegendary.reignofnether.sounds.SoundAction;
import com.solegendary.reignofnether.sounds.SoundClientboundPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public interface HeroUnit extends Unit{

    int MAX_HERO_LEVEL = 10;

    float getHealthBonusPerLevel();
    float getAttackBonusPerLevel();
    float getBaseHealth();
    float getBaseAttack();

    int getSkillPoints();
    void setSkillPoints(int points);
    boolean isRankUpMenuOpen();
    void showRankUpMenu(boolean show);
    int getExperience();
    void setExperience(int experience);
    default int getChargesForSaveData() { return 0; } // track stats like necromancer souls in save data
    default void setChargesFromSaveData(int charges) { }

    default void setStatsForLevel() {
        AttributeInstance aiMaxHealth = ((LivingEntity) this).getAttribute(Attributes.MAX_HEALTH);
        if (aiMaxHealth != null)
            aiMaxHealth.setBaseValue(getBaseHealth() + ((getHeroLevel() - 1) * getHealthBonusPerLevel()));
        AttributeInstance aiAttackDamage = ((LivingEntity) this).getAttribute(Attributes.ATTACK_DAMAGE);
        if (aiAttackDamage != null)
            aiAttackDamage.setBaseValue(getBaseAttack() + ((getHeroLevel() - 1) * getAttackBonusPerLevel()));
    }

    default void addExperience(int amount) {
        if (((LivingEntity) this).level().isClientSide())
            return;
        int levelBefore = getHeroLevel();
        if (levelBefore >= MAX_HERO_LEVEL)
            return;

        setExperience(getExperience() + amount);
        int levelDiff = getHeroLevel() - levelBefore;

        HeroClientboundPacket.setExperience(((LivingEntity) this).getId(), getExperience());
        if (levelDiff > 0) {
            setSkillPoints(getSkillPoints() + levelDiff);
            HeroClientboundPacket.setSkillPoints(((LivingEntity) this).getId(), getSkillPoints());
            SoundClientboundPacket.playSoundAtPos(SoundAction.LEVEL_UP, ((LivingEntity) this).getOnPos());
            setStatsForLevel();
            ((LivingEntity) this).heal(levelDiff * getHealthBonusPerLevel());
        }
    }

    // we always track total exp and then reduce down for the UI
    default int getHeroLevel() {
        int level = 0;
        int expToNextLevel = 200;
        int exp = getExperience();
        do {
            level += 1;
            exp -= expToNextLevel;
            expToNextLevel += 100;
        } while (exp > 0 && level < MAX_HERO_LEVEL);
        return level;
    }

    // @ 4000 exp, show 500/900 (level 8)
    default int getExpOnCurrentLevel() {
        if (getHeroLevel() >= MAX_HERO_LEVEL)
            return 0;
        int expToNextLevel = 200;
        int expCount = 0;
        int exp = getExperience();
        while (expCount < exp) {
            if (expCount + expToNextLevel > exp) {
                return exp - expCount;
            }
            expCount += expToNextLevel;
            expToNextLevel += 100;
        }
        return 0;
    }

    default int getExpToNextlevel() {
        if (getHeroLevel() >= MAX_HERO_LEVEL)
            return 0;
        return (getHeroLevel() + 1) * 100;
    }

    default List<HeroAbility> getHeroAbilities() {
        return getAbilities().stream()
                .filter(a -> a instanceof HeroAbility)
                .map(a -> (HeroAbility) a)
                .toList();
    }
}
