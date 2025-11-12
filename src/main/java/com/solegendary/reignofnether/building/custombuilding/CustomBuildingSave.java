package com.solegendary.reignofnether.building.custombuilding;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;

// registry saves only - placements are saved as a regular BuildingSaves
public class CustomBuildingSave {

    public CompoundTag structureNbt;
    public String buildingName;
    public Vec3i structureSize;
    public String portraitBlockRegistryKey;
    public boolean capturable;
    public boolean invulnerable;

    public CustomBuildingSave(CompoundTag structureNbt, String buildingName, Vec3i structureSize, String portraitBlockRegistryKey,
                              boolean capturable, boolean invulnerable) {
        this.structureNbt = structureNbt;
        this.buildingName = buildingName;
        this.structureSize = structureSize;
        this.portraitBlockRegistryKey = portraitBlockRegistryKey;
        this.capturable = capturable;
        this.invulnerable = invulnerable;
    }
}
