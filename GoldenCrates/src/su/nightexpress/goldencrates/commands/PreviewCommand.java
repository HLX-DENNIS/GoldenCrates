package su.nightexpress.goldencrates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.constants.JStrings;
import su.nightexpress.goldencrates.GoldenCrates;
import su.nightexpress.goldencrates.Perms;
import su.nightexpress.goldencrates.manager.crate.Crate;

import java.util.List;

public class PreviewCommand extends ISubCommand<GoldenCrates> {

	public PreviewCommand(@NotNull GoldenCrates plugin) {
		super(plugin, new String[] {"preview"}, Perms.PREVIEW);
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Command_Preview_Usage.getMsg();
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Command_Preview_Desc.getMsg();
	}
	
	@Override
	public boolean playersOnly() {
		return false;
	}

	@Override
	@NotNull
	public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
		switch (i) {
			case 1: {
				return plugin.getCrateManager().getCrateIds(false);
			}
		}
		return super.getTab(player, i, args);
	}
	
	@Override
	public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (args.length < 2) {
			this.printUsage(sender);
			return;
		}
		
		String pName = sender.getName();
		String crateId = args[1];
		
		Crate crate = plugin.getCrateManager().getCrateById(crateId);
		if (crate == null) {
			plugin.lang().Crate_Error_Invalid.replace("%crate%", crateId).send(sender);
			return;
		}
		
		if (pName.equalsIgnoreCase(JStrings.MASK_ANY)) {
			this.printUsage(sender);
			return;
		}
		else {
			Player player = plugin.getServer().getPlayer(pName);
			if (player == null) {
				this.errPlayer(sender);
				return;
			}
			if(!crate.openPreview(player))
				return;
		}

		// we don't really need to notify about previews.
		//plugin.lang().Command_Preview_Done
		//	.replace("%crate%", crate.getName())
		//	.send(sender);
	}
}
