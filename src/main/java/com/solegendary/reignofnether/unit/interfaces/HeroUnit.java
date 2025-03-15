package com.solegendary.reignofnether.unit.interfaces;

public interface HeroUnit {

    int MAX_HERO_LEVEL = 10;

    int getSkillPoints();
    void setSkillPoints(int points);

    boolean isRankUpMenuOpen();
    void showRankUpMenu(boolean show);

    int getHeroLevel();
    void levelUp();
}
