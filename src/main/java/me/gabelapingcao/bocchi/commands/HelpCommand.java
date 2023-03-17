package me.gabelapingcao.bocchi.commands;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

/**
 * This implementation of HelpCommand was derived from <a href=
 * "https://github.com/DV8FromTheWorld/Yui/blob/5ef26d486c55e4f7bd4ebceb96345a12dca2adc0/src/main/java/net/dv8tion/discord/commands/HelpCommand.java">
 * DV8FromtheWorld's Yui Bot</a> * 
 *
 */
public class HelpCommand extends Command {
	
	private static final String NO_NAME = "No name provided for this command. Sorry!";
	private static final String NO_DESCRIPTION = "No description has been provided for this command. Sorry!";
	private static final String NO_USAGE = "No usage instructions have been provided for this command. Sorry!";

	private TreeMap<String, Command> commands;

	public HelpCommand() {
		commands = new TreeMap<>();
	}

	public Command registerCommand(Command command) {
		commands.put(command.getAliases().get(0), command);
		return command;
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if (!e.isFromType(ChannelType.PRIVATE)) {
			e.getGuildChannel().sendMessage(
					new MessageCreateBuilder().setContent("Help information was sent as a private message.").build())
					.queue();
		}
		sendPrivate(e.getAuthor().openPrivateChannel().complete(), args);
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(".help", ".commands");
	}

	@Override
	public String getDescription() {
		return "Command that helps use all other commands!";
	}

	@Override
	public String getName() {
		return "Help Command";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Collections.singletonList(".help   **OR**  .help *<command>*\n"
				+ ".help - returns the list of commands along with a simple description of each.\n"
				+ ".help <command> - returns the name, description, aliases and usage information of a command.\n"
				+ "   - This can use the aliases of a command as input as well.\n" + "__Example:__ .help ann");
	}

	private void sendPrivate(PrivateChannel channel, String[] args) {
		if (args.length < 2) {
			StringBuilder s = new StringBuilder();
			for (Command c : commands.values()) {
				String description = c.getDescription();
				description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

				s.append("**").append(c.getAliases().get(0)).append("** - ");
				s.append(description).append("\n");
			}

			channel.sendMessage(new MessageCreateBuilder()
					.setContent("The following commands are supported by the bot\n" + s).build()).queue();
		} else {
			String command = args[1].charAt(0) == '.' ? args[1] : "." + args[1]; // If there is not a preceding .
																					// attached to the command we are
																					// search, then prepend one.
			for (Command c : commands.values()) {
				if (c.getAliases().contains(command)) {
					String name = c.getName();
					String description = c.getDescription();
					List<String> usageInstructions = c.getUsageInstructions();
					name = (name == null || name.isEmpty()) ? NO_NAME : name;
					description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
					usageInstructions = (usageInstructions == null || usageInstructions.isEmpty())
							? Collections.singletonList(NO_USAGE)
							: usageInstructions;

					// TODO: Replace with a PrivateMessage
					channel.sendMessage(new MessageCreateBuilder().addContent("**Name:** " + name + "\n")
							.addContent("**Description:** " + description + "\n")
							.addContent("**Aliases:** " + StringUtils.join(c.getAliases(), ", ") + "\n")
							.addContent("**Usage:** ").addContent(usageInstructions.get(0)).build()).queue();
					for (int i = 1; i < usageInstructions.size(); i++) {
						channel.sendMessage(new MessageCreateBuilder()
								.addContent("__" + name + " Usage Cont. (" + (i + 1) + ")__\n")
								.addContent(usageInstructions.get(i)).build()).queue();
					}
					return;
				}
			}
			channel.sendMessage(new MessageCreateBuilder().setContent(
					"The provided command '**" + args[1] + "**' does not exist. Use .help to list all commands.")
					.build()).queue();
		}
	}
}