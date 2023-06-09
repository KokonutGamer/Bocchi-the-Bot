/**
 * 
 */
package me.gabelapingcao.bocchi.commands;

import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * @author Gabe Lapingcao
 *
 */
public abstract class Command {

	public abstract void onCommand(MessageReceivedEvent event, String[] args);

	public abstract List<String> getAliases();

	public abstract String getDescription();

	public abstract String getName();

	public abstract List<String> getUsageInstructions();

	@SubscribeEvent
	public void onCommandReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() && !respondToBots())
			return;
		if (containsCommand(event.getMessage())) {
			onCommand(event, commandArgs(event.getMessage()));
		}
	}

	protected boolean containsCommand(Message message) {
		return getAliases().contains(commandArgs(message)[0]);
	}

	protected String[] commandArgs(Message message) {
		return commandArgs(message.getContentDisplay());
	}

	// The regular expression \s+ (\\ is used as an escape character for \) removes
	// based on all whitespace characters
	protected String[] commandArgs(String string) {
		return string.split("\\s+");
	}

	protected Message sendMessage(MessageReceivedEvent e, MessageCreateData message) {
		return e.getChannel().sendMessage(message).complete();
	}

	protected Message sendMessage(MessageReceivedEvent event, String message) {
		return sendMessage(event, new MessageCreateBuilder().setContent(message).build());
	}

	protected boolean respondToBots() {
		return false;
	}

}
