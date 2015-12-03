package me.superckl.biometweaker.common.handler;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEventHandler {

	@Getter
	@Setter
	private static int globalPackSize = -1;
	@Getter
	private static TIntObjectMap<TObjectIntMap<String>> packSizes = new TIntObjectHashMap<TObjectIntMap<String>>();

	@SubscribeEvent
	public void onGetMaxPackSize(final LivingPackSizeEvent e){
		if(EntityEventHandler.globalPackSize > -1){
			e.setResult(Result.ALLOW);
			e.maxPackSize = EntityEventHandler.globalPackSize;
			return;
		}
		if(EntityEventHandler.packSizes.isEmpty())
			return;
		final BiomeGenBase biome = e.entityLiving.worldObj.getBiomeGenForCoords(e.entityLiving.getPosition());
		final String clazz = e.entityLiving.getClass().getName();
		if(EntityEventHandler.packSizes.containsKey(biome.biomeID)){
			final TObjectIntMap<String> sizes = EntityEventHandler.packSizes.get(biome.biomeID);
			if(sizes.containsKey(clazz)){
				final int size = sizes.get(clazz);
				if(size > -1){
					e.setResult(Result.ALLOW);
					e.maxPackSize = size;
				}
			}
		}
	}

}
