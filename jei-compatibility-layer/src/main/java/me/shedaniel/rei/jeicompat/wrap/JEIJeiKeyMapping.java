package me.shedaniel.rei.jeicompat.wrap;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import mezz.jei.api.runtime.IJeiKeyMapping;
import net.minecraft.network.chat.Component;

public class JEIJeiKeyMapping implements IJeiKeyMapping {
    private final ModifierKeyCode keyCode;
    
    public JEIJeiKeyMapping(ModifierKeyCode keyCode) {
        this.keyCode = keyCode;
    }
    
    @Override
    public boolean isActiveAndMatches(InputConstants.Key key) {
        return key.getType() == InputConstants.Type.KEYSYM ? keyCode.matchesKey(key.getValue(), InputConstants.UNKNOWN.getValue()) :
                key.getType() == InputConstants.Type.MOUSE ? keyCode.matchesMouse(key.getValue()) :
                        key.getType() == InputConstants.Type.SCANCODE && keyCode.matchesKey(InputConstants.UNKNOWN.getValue(), key.getValue());
    }
    
    @Override
    public boolean isUnbound() {
        return keyCode.isUnknown();
    }
    
    @Override
    public Component getTranslatedKeyMessage() {
        return keyCode.getLocalizedName();
    }
}
