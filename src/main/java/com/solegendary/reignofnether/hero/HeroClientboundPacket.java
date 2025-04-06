package com.solegendary.reignofnether.hero;

import com.solegendary.reignofnether.registrars.PacketHandler;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class HeroClientboundPacket {

    HeroAction action;
    int unitId;
    int value;

    public static void setExperience(int unitId, int value) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new HeroClientboundPacket(HeroAction.SET_EXPERIENCE, unitId, value));
    }

    public static void setSkillPoints(int unitId, int value) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new HeroClientboundPacket(HeroAction.SET_SKILL_POINTS, unitId, value));
    }

    public HeroClientboundPacket(HeroAction action, int unitId, int value) {
        this.action = action;
        this.unitId = unitId;
        this.value = value;
    }

    public HeroClientboundPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(HeroAction.class);
        this.unitId = buffer.readInt();
        this.value = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.action);
        buffer.writeInt(this.unitId);
        buffer.writeInt(this.value);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> {
                    HeroUnit hero = null;
                    for (LivingEntity entity : UnitClientEvents.getAllUnits()) {
                        if (entity.getId() == this.unitId && entity instanceof HeroUnit) {
                            hero = (HeroUnit) entity;
                        }
                    }
                    if (hero != null) {
                        switch (action) {
                            case SET_EXPERIENCE -> hero.setExperience(value);
                            case SET_SKILL_POINTS -> hero.setSkillPoints(value);
                        }
                    }
                    success.set(true);
                });
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
