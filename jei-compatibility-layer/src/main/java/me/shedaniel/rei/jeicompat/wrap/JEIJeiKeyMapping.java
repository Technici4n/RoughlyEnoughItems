/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
