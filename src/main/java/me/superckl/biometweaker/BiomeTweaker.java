package me.superckl.biometweaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lombok.Cleanup;
import lombok.Getter;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.proxy.IProxy;
import me.superckl.biometweaker.script.ScriptCommandManager.ApplicationStage;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLLoadEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION)
public class BiomeTweaker {

	@Instance(ModData.MOD_ID)
	@Getter
	private static BiomeTweaker instance;

	@SidedProxy(clientSide=ModData.CLIENT_PROXY, serverSide=ModData.SERVER_PROXY)
	@Getter
	private static IProxy proxy;

	@EventHandler
	public void onInit(final FMLLoadEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.LOAD);
	}

	@EventHandler
	public void onPreInit(final FMLPreInitializationEvent e){
		BiomeTweaker.proxy.registerHandlers();
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.PRE_INIT);
	}

	@EventHandler
	public void onInit(final FMLInitializationEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.INIT);
	}

	@EventHandler
	public void onPostInit(final FMLPostInitializationEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.POST_INIT);
	}

	@EventHandler
	public void onLoadComplete(final FMLLoadCompleteEvent e) throws IOException{
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.FINISHED_LOAD);
		LogHelper.info("Generating biome status report...");
		final JsonArray array = new JsonArray();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
			if(gen == null)
				continue;
			array.add(BiomeHelper.fillJsonObject(gen));
		}
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final File dir = new File(BiomeTweakerCore.mcLocation, "/config/BiomeTweaker/output/");
		dir.mkdirs();
		for(final File file:dir.listFiles())
			file.delete();
		if(Config.INSTANCE.isOutputSeperateFiles())
			for(final JsonElement element:array){
				final JsonObject obj = (JsonObject) element;
				final File output = new File(dir, ""+obj.get("Name").getAsString()+".json");
				if(output.exists())
					output.delete();
				output.createNewFile();
				@Cleanup
				final
				BufferedWriter writer = new BufferedWriter(new FileWriter(output));
				writer.newLine();
				writer.write(gson.toJson(obj));
			}
		else{
			final File output = new File(dir, "BiomeTweaker - Biome Status Report.json");
			if(output.exists())
				output.delete();
			output.createNewFile();
			@Cleanup
			final
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("//Yeah, it's a doozy.");
			writer.newLine();
			writer.write(gson.toJson(array));
		}
	}

	@EventHandler
	public void onInit(final FMLServerStartingEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.SERVER_STARTING);
	}

	@EventHandler
	public void onInit(final FMLServerStartedEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.SERVER_STARTED);
	}

}
