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
		log.info("Joined a guild!");
		SlashCommandData command = Commands.slash(getName(), getDescription());
		command.addOptions(getOptions());
		command.setDefaultPermissions(getPermissions());
		event.getGuild().upsertCommand(command).queue();
	}

	@SubscribeEvent
	@Override
	public void onCommand(SlashCommandInteractionEvent event) {
		log.info("detected a slashcommandinteractionevent");
		if (event.getName().equals(getName())) {
			log.info("calling in " + this.getClass().getSimpleName());
			call(event);
		}
	}

	public abstract void call(SlashCommandInteractionEvent event);

	public abstract DefaultMemberPermissions getPermissions();

}
