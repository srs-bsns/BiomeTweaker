package me.superckl.biometweaker.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import me.superckl.biometweaker.common.reference.ModData;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@SortingIndex(2000)
@MCVersion("1.7.10")
@TransformerExclusions("me.superckl.biometweaker.core")
public class BiomeTweakerCore extends AccessTransformer implements IFMLLoadingPlugin{
	
	public static File mcLocation;
	
	public BiomeTweakerCore() throws IOException {
		super(ModData.MOD_ID.toLowerCase()+"_at.cfg");
	}

	@Override
	public String[] getASMTransformerClass() {
		//TODO
		return null;
	}

	@Override
	public String getModContainerClass() {
		return ModBiomeTweakerCore.class.getName();
	}

	@Override
	public String getSetupClass() {
		return BiomeTweakerCallHook.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data) {
		mcLocation = (File) data.get("mcLocation");
	}

	@Override
	public String getAccessTransformerClass() {
		return this.getClass().getName();
	}

}