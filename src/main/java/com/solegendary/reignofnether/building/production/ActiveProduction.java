package com.solegendary.reignofnether.building.production;

public class ActiveProduction {
    public boolean completed;
    public float ticksLeft;
    public ProductionItem item;
    public ActiveProduction(ProductionItem item) {
        this.item = item;
        this.ticksLeft = item.cost.ticks;
    }
}
