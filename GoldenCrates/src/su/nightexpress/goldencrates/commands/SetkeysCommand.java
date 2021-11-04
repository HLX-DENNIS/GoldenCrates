package su.nightexpress.goldencrates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.PlayerUT;
import su.nexmedia.engine.utils.constants.JStrings;
import su.nightexpress.goldencrates.GoldenCrates;
import su.nightexpress.goldencrates.Perms;
import su.nightexpress.goldencrates.manager.key.CrateKey;

import java.util.Arrays;
import java.util.List;

public class SetkeysCommand extends ISubCommand<GoldenCrates> {

	public SetkeysCommand(@NotNull GoldenCrates plugin) {
		super(plugin, new String[] {"setkeys"}, Perms.ADMIN);
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Command_SetKeys_Usage.getMsg();
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Command_SetKeys_Desc.getMsg();
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
				List<String> list = PlayerUT.getPlayerNames();
				return list;
			}
			case 2: {
				return plugin.getKeyManager().getKeyIds();
			}
			case 3: {
				return Arrays.asList("1", "5", "10", "-1", "-5", "-10");
			}
		}
		return super.getTab(player, i, args);
	}

	@Override
	public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (args.length < 4) {
			this.printUsage(sender);
			return;
		}
		
		CrateKey crateKey = plugin.getKeyManager().getKeyById(args[2]);
		if (crateKey == null) {
			plugin.lang().Command_SetKeys_Error_NoKey.send(sender);
			return;
		}
		
		int amount = args.length >= 4 ? this.getNumI(sender, args[3], 1, true) : 0;
		
		String pName = args[1];
		if (pName.equalsIgnoreCase(JStrings.MASK_ANY)) {
			plugin.lang().Error_SetKeys_NoMaskAny.send(sender);
			return;
		}
		else {
			if (!plugin.getKeyManager().setKeys(pName, crateKey, amount)) {
				plugin.lang().Error_NoData.replace("%player%", pName).send(sender);
				return;
			}
		}
		
		plugin.lang().Command_SetKeys_Done
				.replace("%player%", pName)
				.replace("%amount%", amount)
				.replace("%key%", crateKey.getName())
				.send(sender);
	}
}
