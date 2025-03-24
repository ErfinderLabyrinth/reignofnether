package com.solegendary.reignofnether.ability;

import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Abilities {
    Map<Ability, Keybinding> abilities = new HashMap<>();

    public Abilities() {

    }

    public void add(Ability ability, Keybinding keybind) {
        abilities.put(ability, keybind);
    }

    public List<AbilityButton> getButtons(BuildingPlacement placement) {
        List<AbilityButton> buttons = new ArrayList<>();
        //TODO Remove need for Minecraft
        if (FMLEnvironment.dist == Dist.CLIENT) {
            for (Map.Entry<Ability, Keybinding> ability : abilities.entrySet()) {
                buttons.add(ability.getKey().getButton(ability.getValue(), placement));
            }
        }
        return buttons;
    }

    public List<AbilityButton> getButtons(Unit unit) {
        List<AbilityButton> buttons = new ArrayList<>();
        //TODO Remove need for I18n
        if (FMLEnvironment.dist == Dist.CLIENT) {
            for (Map.Entry<Ability, Keybinding> ability : abilities.entrySet()) {
                buttons.add(ability.getKey().getButton(ability.getValue(), unit));
            }
        }
        return buttons;
    }

    public List<Ability> get() {
        return new ArrayList<>(abilities.keySet());
    }
}
