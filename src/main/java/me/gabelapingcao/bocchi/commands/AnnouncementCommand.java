/**
 * 
 */
package me.gabelapingcao.bocchi.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.gabelapingcao.bocchi.util.BocchiOperations;
import me.gabelapingcao.bocchi.util.GuildInfoLoader;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * @author School
 *
 */
public class AnnouncementCommand extends Command {

	private static final BocchiOperations op = BocchiOperations.getInstance();
	private static final GuildInfoLoader info = GuildInfoLoader.getInstance();

	@SubscribeEvent
	public void onCommandReceived(MessageReceivedEvent event) {
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			return;
		}
		super.onCommandReceived(event);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, String[] args) {
		if (args.length < 2) {
			event.getChannel()
					.sendMessage(op.SimpleCreateMessage(
							"**Error**: please specify a title and/or description to add to this announcement."))
					.queue();
			return;
		}

		StringBuilder sbTitle = new StringBuilder();
		StringBuilder sbText = new StringBuilder();
		boolean isTitle = false;
		boolean isDesc = false;
		for (int i = 1; i < args.length; i++) {
			
			if (isDesc) {
				sbText.append(" " + args[i]);
			}
			
			if (isTitle) {
				if (args[i].startsWith("-")) {
					sbText.append(args[i].substring(1));
					isDesc = true;
					isTitle = false;
				} else {
					sbTitle.append(" " + args[i]);					
				}
			}
			
			if (args[i].startsWith("+") && !isTitle && !isDesc) {
				sbTitle.append(args[i].substring(1));
				isTitle = true;
			}
		}
		String title = (sbTitle.isEmpty()) ? null : sbTitle.toString();
		String text = (sbText.isEmpty()) ? null : sbText.toString();
		MessageChannel announcements = info.getGuildChannels(event.getGuild()).get(2);
		MessageCreateData message = op.CreateAnnouncement(event.getAuthor(), title, text);
		announcements.sendMessage(message).queue();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(".announce", "..a");
	}

	@Override
	public String getDescription() {
		return "Allows an administrator to create an announcement and send it to the guild's specified announcement channel";
	}

	@Override
	public String getName() {
		return "Announcement command";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Collections.singletonList(".announce - Sends an announcement message to the announcement channel.\n"
				+ "To insert the title, prepend a \"+\" symbol to the front of the title text.\n"
				+ "To insert the description, prepend a \"-\" symbol to the front of the description text.");
	}

}
