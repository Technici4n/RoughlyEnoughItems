package me.shedaniel.rei.jeicompat.wrap;

import me.shedaniel.rei.api.client.config.ConfigObject;
import mezz.jei.api.runtime.IJeiKeyMapping;
import mezz.jei.api.runtime.IJeiKeyMappings;

public enum JEIJeiKeyMappings implements IJeiKeyMappings {
    INSTANCE;
    
    @Override
    public IJeiKeyMapping getShowRecipe() {
        return new JEIJeiKeyMapping(ConfigObject.getInstance().getRecipeKeybind());
    }
    
    @Override
    public IJeiKeyMapping getShowUses() {
        return new JEIJeiKeyMapping(ConfigObject.getInstance().getUsageKeybind());
    }
}
