package me.superckl.api.biometweaker.world.gen.feature;

import java.util.Random;

import lombok.RequiredArgsConstructor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

@RequiredArgsConstructor
public class WorldGeneratorWrapper<K extends WorldGenerator> {

	protected final K generator;

	public void generate(final World world, final Random rand, final BlockPos chunkPos){
		final int x = rand.nextInt(16) + 8;
		final int z = rand.nextInt(16) + 8;
		final int y = world.getHeight(chunkPos.add(x, 0, z)).getY() * 2;
		this.generator.generate(world, rand, chunkPos.add(x, y, z));
	}

}
