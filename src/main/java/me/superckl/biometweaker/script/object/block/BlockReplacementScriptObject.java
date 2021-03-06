package me.superckl.biometweaker.script.object.block;

import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.object.ScriptObject;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.world.gen.ReplacementConstraints;

public class BlockReplacementScriptObject extends ScriptObject{

	private final ReplacementConstraints constraints = new ReplacementConstraints();

	@Override
	public String[] modifyArguments(final String[] args, final ScriptHandler handler) {
		final String name = CollectionHelper.reverseLookup(handler.getObjects(), this);
		final String[] newArgs = new String[args.length+1];
		newArgs[0] = name;
		System.arraycopy(args, 0, newArgs, 1, args.length);
		return newArgs;
	}

	@Override
	public void addCommand(final ScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

	public ReplacementConstraints getConstraints() {
		return this.constraints;
	}

}
