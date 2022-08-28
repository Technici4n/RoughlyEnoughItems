package me.shedaniel.rei.plugin.client.favorites;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import me.shedaniel.rei.api.client.favorites.FavoriteEntry;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class EntryStackFavoriteEntry extends FavoriteEntry {
    private static final Function<EntryStack<?>, String> CANCEL_FLUID_AMOUNT = s -> null;
    private final EntryStack<?> stack;
    private final long hash;
    
    public EntryStackFavoriteEntry(EntryStack<?> stack) {
        this.stack = stack.normalize();
        this.hash = EntryStacks.hashExact(this.stack);
    }
    
    @Override
    public boolean isInvalid() {
        return this.stack.isEmpty();
    }
    
    @Override
    public Renderer getRenderer(boolean showcase) {
        return this.stack;
    }
    
    @Override
    public boolean doAction(int button) {
        return false;
    }
    
    @Override
    public long hashIgnoreAmount() {
        return hash;
    }
    
    @Override
    public FavoriteEntry copy() {
        return new EntryStackFavoriteEntry(stack.normalize());
    }
    
    @Override
    public ResourceLocation getType() {
        return FavoriteEntryType.ENTRY_STACK;
    }
    
    @Override
    public boolean isSame(FavoriteEntry other) {
        if (!(other instanceof EntryStackFavoriteEntry that)) return false;
        return EntryStacks.equalsExact(stack, that.stack);
    }
    
    public enum Type implements FavoriteEntryType<EntryStackFavoriteEntry> {
        INSTANCE;
        
        private final String key = "data";
        
        @Override
        public DataResult<EntryStackFavoriteEntry> read(CompoundTag object) {
            EntryStack<?> stack;
            try {
                stack = EntryStack.read(object.getCompound(key));
            } catch (Throwable throwable) {
                return DataResult.error(throwable.getMessage());
            }
            return DataResult.success(new EntryStackFavoriteEntry(stack), Lifecycle.stable());
        }
        
        @Override
        public DataResult<EntryStackFavoriteEntry> fromArgs(Object... args) {
            if (args.length == 0) return DataResult.error("Cannot create EntryStackFavoriteEntry from empty args!");
            if (!(args[0] instanceof EntryStack<?> stack))
                return DataResult.error("Creation of EntryStackFavoriteEntry from args expected EntryStack as the first argument!");
            if (!stack.supportSaving())
                return DataResult.error("Creation of EntryStackFavoriteEntry from an unserializable stack!");
            return DataResult.success(new EntryStackFavoriteEntry(stack), Lifecycle.stable());
        }
        
        @Override
        public CompoundTag save(EntryStackFavoriteEntry entry, CompoundTag tag) {
            tag.put(key, entry.stack.saveStack());
            return tag;
        }
    }
}