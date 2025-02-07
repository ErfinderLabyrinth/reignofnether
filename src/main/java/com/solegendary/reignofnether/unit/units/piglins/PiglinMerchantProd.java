package com.solegendary.reignofnether.unit.units.piglins;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.BuildingServerboundPacket;
import com.solegendary.reignofnether.building.ProductionBuilding;
import com.solegendary.reignofnether.building.ProductionItem;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.sandbox.SandboxAction;
import com.solegendary.reignofnether.sandbox.SandboxClientEvents;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;

import java.util.List;

public class PiglinMerchantProd extends ProductionItem {

    public final static String itemName = "piglin_merchant";
    public final static ResourceCost cost = ResourceCosts.PIGLIN_MERCHANT;

    public PiglinMerchantProd(ProductionBuilding building) {
        super(building, cost.ticks);
        this.onComplete = (Level level) -> {
            if (!level.isClientSide())
                building.produceUnit((ServerLevel) level, EntityRegistrar.PIGLIN_MERCHANT_UNIT.get(), building.ownerName, true);
        };
        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popCost = cost.population;
    }

    public String getItemName() {
        return PiglinMerchantProd.itemName;
    }

    public static AbilityButton getPlaceButton() {
        return new AbilityButton(
                itemName,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/piglin_merchant.png"),
                null,
                () -> SandboxClientEvents.spawnUnitName.equals(itemName),
                () -> false,
                () -> false,
                () -> {
                    CursorClientEvents.setLeftClickSandboxAction(SandboxAction.SPAWN_UNIT);
                    SandboxClientEvents.spawnUnitName = itemName;
                },
                null,
                List.of(
                        FormattedCharSequence.forward(
                                I18n.get("units.piglins.reignofnether.piglin_merchant") +
                                        " (" + I18n.get("hud.units.reignofnether.hero") + ")",
                                Style.EMPTY.withBold(true)),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.piglin_merchant.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.piglin_merchant.tooltip2"), Style.EMPTY)
                ),
                null
        );
    }

    public static Button getStartButton(ProductionBuilding prodBuilding, Keybinding hotkey) {
        return new Button(
                PiglinMerchantProd.itemName,
                14,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/piglin_merchant.png"),
                hotkey,
                () -> false,
                () -> false,
                () -> true,
                () -> BuildingServerboundPacket.startProduction(prodBuilding.originPos, itemName),
                null,
                List.of(
                        FormattedCharSequence.forward(
                                I18n.get("units.piglins.reignofnether.piglin_merchant") +
                                        " (" + I18n.get("hud.units.reignofnether.hero") + ")",
                                Style.EMPTY.withBold(true)),
                        ResourceCosts.getFormattedCost(cost),
                        ResourceCosts.getFormattedPopAndTime(cost),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.piglin_merchant.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.piglin_merchant.tooltip2"), Style.EMPTY)
                )
        );
    }

    public Button getCancelButton(ProductionBuilding prodBuilding, boolean first) {
        return new Button(
                PiglinMerchantProd.itemName,
                14,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/piglin_merchant.png"),
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                () -> BuildingServerboundPacket.cancelProduction(prodBuilding.originPos, itemName, first),
                null,
                null
        );
    }
}
