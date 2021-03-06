package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class QuitCommand extends PBCommand {
	
	public QuitCommand() {
		super ("team-quit", "/pb team quit", "Quit your current team.", new String[] {"quit", "q", "leave", "l"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();
		Team team = PBMethods.getPlayerTeam(uuid);
		if (team == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotInTeam);
			return;
		}
		if (team.isOwner(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.CantBootFromOwnTeam);
			return;
		}
		team.removePlayer(uuid);
		sender.sendMessage(PBMethods.Prefix + PBMethods.YouHaveQuit.replace("%team", team.getName()));
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (PBMethods.getPlayerTeam(player.getUniqueId()) == null) continue;
			if (PBMethods.getPlayerTeam(player.getUniqueId()).equals(team.getName())) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerHasQuit.replace("%team", team.getName()).replace("%player", sender.getName()));
			}
		}
		return;
	}
}
