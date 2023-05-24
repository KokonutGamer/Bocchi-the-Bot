package me.gabelapingcao.bocchi.commands;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class GuildCommand implements BocchiCommand {

	protected static final Logger log = (Logger) LoggerFactory.getLogger(GuildCommand.class);

	@SubscribeEvent
	public void onGuildJoin(GuildJoinEvent event) {
		SlashCommandData command = Commands.slash(getName(), getDescription());
		if (getOptions() != null)
			command.addOptions(getOptions());
		command.setDefaultPermissions(getPermissions());
		event.getGuild().upsertCommand(command).queue();
	}

	@SubscribeEvent
	@Override
	public void onCommand(SlashCommandInteractionEvent event) {
		if (event.getName().equals(getName())) {
			call(event);
		}
	}

	public abstract void call(SlashCommandInteractionEvent event);

	public abstract DefaultMemberPermissions getPermissions();

}
