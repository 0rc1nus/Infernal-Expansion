/*
 * Copyright 2022 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infernalstudios.infernalexp.mixin.common;

import org.infernalstudios.infernalexp.init.IEBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;

@Mixin(ShearsItem.class)
public class MixinShearsItem {
    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void IE_getDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> ci) {
        if (state.is(IEBlocks.DULLTHORNS.get())) ci.setReturnValue(15.0F);
    }
}
