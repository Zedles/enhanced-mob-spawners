package com.branders.spawnermod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.branders.spawnermod.event.EventHandler;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
/**
 * 	Redstone event for Spawner.
 * 
 * 	Found from following PistonBlock: {@code neighborUpdate()} function.
 * 
 * 	@author Anders <Branders> Blomqvist
 */
@Mixin(AbstractBlock.class)
public class UpdateNeighborMixin {

    @Inject(at = @At("HEAD"), method = "neighborUpdate", cancellable = true)
    private void onNeighborUpdate(
        BlockState state,
        World world,
        BlockPos pos,
        Block sourceBlock,
        WireOrientation orientation,  // <— parâmetro correto
        boolean notify,
        CallbackInfo ci
    ) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for(Direction dir : Direction.values()) {
            mutable.set(pos, dir);
            if(world.getBlockState(mutable).getBlock() instanceof SpawnerBlock) {
                EventHandler.updateNeighbor(mutable, (World)world);
            }
        }
    }
}
