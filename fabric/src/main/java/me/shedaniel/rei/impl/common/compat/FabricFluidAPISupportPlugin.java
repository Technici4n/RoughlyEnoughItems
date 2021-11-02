/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021 shedaniel
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

package me.shedaniel.rei.impl.common.compat;

import me.shedaniel.architectury.event.CompoundEventResult;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.fluid.FluidSupportProvider;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FabricFluidAPISupportPlugin implements REIServerPlugin {
    @Override
    public void registerFluidSupport(FluidSupportProvider support) {
        support.register(entry -> {
            ItemStack stack = entry.getValue().copy();
            Storage<FluidVariant> storage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
            if (storage != null) {
                List<EntryStack<FluidStack>> result;
                try (Transaction transaction = Transaction.openOuter()) {
                    result = StreamSupport.stream(storage.iterable(transaction).spliterator(), false)
                        .filter(view -> !view.isResourceBlank() && !view.getResource().isBlank())
                        .map(view -> EntryStacks.of(FluidStack.create(view.getResource().getFluid(), Fraction.of(view.getAmount(), FluidConstants.BUCKET), view.getResource().getNbt())))
                            .collect(Collectors.toList());
                }
                if (!result.isEmpty()) {
                    return CompoundEventResult.interruptTrue(result.stream());
                }
            }
            return CompoundEventResult.pass();
        });
    }
}