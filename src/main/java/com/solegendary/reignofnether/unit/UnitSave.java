package com.solegendary.reignofnether.unit;

import net.minecraft.core.BlockPos;

public class UnitSave {

    public String name;
    public String ownerName;
    public String uuid; // regular integer ID isn't consistent between server restarts
    public BlockPos anchorPos;
    // held resources are covered by ResourcesSaveData

    public UnitSave(String name, String ownerName, String uuid, BlockPos anchorPos) {
        this.name = name;
        this.ownerName = ownerName;
        this.uuid = uuid;
        this.anchorPos = anchorPos;
    }
}
