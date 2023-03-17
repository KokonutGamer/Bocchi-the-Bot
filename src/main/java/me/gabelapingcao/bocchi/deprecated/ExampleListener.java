package me.gabelapingcao.bocchi.deprecated;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ExampleListener extends ListenerAdapter {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(ExampleListener.class);
	
	/**
	 * Returns a message specifying if a user reacted to a message
	 */
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		User user = event.getUser();
		String emoji = event.getReaction().getEmoji().getAsReactionCode();
		String channel = event.getChannel().getAsMention();
		
		String message = user.getAsTag() + " reacted to a message with " + emoji + " in the " + channel + " channel!";
		event.getChannel().sendMessage(message).queue();
	}

	/**
	 * @param event fires when a new member joins a guild
	 * Warning: must have the "Guild Members" gateway intent enabled
	 */
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		User user = event.getUser();
		logger.info("A user has joined");
		logger.debug("Channel connected: " + event.getGuild().getDefaultChannel().getId());
		event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(user.getAsTag() + " has joined!").queue();
	}
	
}
