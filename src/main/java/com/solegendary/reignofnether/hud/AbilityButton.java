package com.solegendary.reignofnether.hud;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.util.LanguageUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AbilityButton extends Button {

    // can be null for stuff like production buttons (handled separately)
    // or simple abilities with no cooldown and the logic can be handled entirely in onLeftClick()
    public Ability ability;

    public AbilityButton(String name, ResourceLocation rl, Keybinding hotkey, Supplier<Boolean> isSelected,
                         Supplier<Boolean> isHidden, Supplier<Boolean> isEnabled, Runnable onLeftClick, Runnable onRightClick,
                         List<FormattedCharSequence> tooltipLines, @Nullable Ability ability) {

        // generate x/y based on given position (starting at 0 which is bottom left 1 row above generic action buttons)
        super(name, Button.itemIconSize, rl, hotkey, isSelected, isHidden, isEnabled, onLeftClick, onRightClick, tooltipLines);

        this.ability = ability;

        Runnable originalOnLeftClick = this.onLeftClick;
        this.onLeftClick = () -> {
            if (this.ability != null && (this.ability.getCooldown() > 0 && !this.ability.canBypassCooldown()))
                HudClientEvents.showTemporaryMessage(LanguageUtil.getTranslation("hud.buttons.reignofnether.on_cooldown"));
            else if (originalOnLeftClick != null)
                originalOnLeftClick.run();
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
        if (this.ability != null && ability.cooldownMax > 0)
            this.greyPercent = 1.0f - ((float) ability.getCooldown() / (float) ability.cooldownMax);
        super.render(guiGraphics, x, y, mouseX, mouseY);
    }
}
