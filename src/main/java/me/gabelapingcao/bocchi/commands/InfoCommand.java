package me.gabelapingcao.bocchi.commands;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.Arrays;
import java.util.List;

public class InfoCommand extends Command {
	
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		MessageCreateBuilder builder = new MessageCreateBuilder();
		builder.addContent("__Bocchi Information__\n").addContent("    **Version**: 1.0.1\n")
				.addContent("    **ID**: " + e.getJDA().getSelfUser().getId() + "\n").addContent("__Creator__\n")
				.addContent("    **Name**: Gabe Lapingcao\n").addContent("    **ID**: Not Available\n")
				.addContent("    **Github**: Not Available\n").addContent("__Development__\n")
				.addContent("    **Language**: Java 8\n")
				.addContent("    **Library**: JDA - v" + JDAInfo.VERSION + "\n")
				.addContent("    **Source**: Not Available");
		sendMessage(e, builder.build());
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(".info");
	}

	@Override
	public String getDescription() {
		return "Provides information about Bocchi.";
	}

	@Override
	public String getName() {
		return "Bocchi Information";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Arrays.asList(".info - Prints all information pertaining to the current instance of Bocchi.");
	}
	
}