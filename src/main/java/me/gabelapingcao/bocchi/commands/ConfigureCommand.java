package me.gabelapingcao.bocchi.commands;

import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ConfigureCommand extends GuildCommand {

	@Override
	public String getName() {
		return "configure";
	}

	@Override
	public String getDescription() {
		return "Configure guild settings for Bocchi";
	}

	@Override
	public List<OptionData> getOptions() {
		return null;
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void call(SlashCommandInteractionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public DefaultMemberPermissions getPermissions() {
		return DefaultMemberPermissions.DISABLED;
	}

}
