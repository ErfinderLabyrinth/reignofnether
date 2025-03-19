package com.solegendary.reignofnether.unit.interfaces;

import io.netty.util.internal.MathUtil;

public interface HeroUnit {

    int MAX_HERO_LEVEL = 10;

    int getSkillPoints();
    void setSkillPoints(int points);

    boolean isRankUpMenuOpen();
    void showRankUpMenu(boolean show);
    int getExperience();
    void setExperience(int experience);
    void updateAbilityButtons();

    default void addExperience(int amount) {
        // TODO: detect level up and award skill points/play sounds accordingly
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
        return (getHeroLevel() + 1) * 100;
    }
}
