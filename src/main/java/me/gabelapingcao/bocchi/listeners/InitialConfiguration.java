/**
 * 
 */
package me.gabelapingcao.bocchi.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import me.gabelapingcao.bocchi.util.BocchiOperations;
import me.gabelapingcao.bocchi.util.GuildInfoLoader;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * This class is used to first configure Bocchi when Bocchi joins a guild.
 * Bocchi will first introduce itself as a bot developed by Gabe Lapingcao. It
 * will then prompt an admin to set up its initial configurations for the
 * default announcements channel and the default commands channel.
 * 
 * @author Gabe Lapingcao
 */
public class InitialConfiguration {

	private static final BocchiOperations op = BocchiOperations.getInstance();
	private static final GuildInfoLoader info = GuildInfoLoader.getInstance();
	private static final Logger logger = (Logger) LoggerFactory.getLogger(InitialConfiguration.class);

	private ArrayList<MessageCreateData> configMessages;

	// Index 0 - Default Channel
	// Index 1 - Admin Channel
	// Index 2 - Announcements Channel
	// Index 3 - Welcome Channel
	private MessageChannel[] configChannels; // Change to ArrayList in the future
	private Message reactionMessage;
	private Member configAdmin;
	private int configStep;

	@SubscribeEvent
	public void onGuildJoin(GuildJoinEvent event) {
		configChannels = new MessageChannel[4];
		configStep = 0;
		configChannels[0] = event.getGuild().getDefaultChannel().asStandardGuildMessageChannel();
		loadConfigurationMessages();
		configChannels[0].sendMessage(configMessages.get(0)).queue(m -> reactionMessage = m); // Sends step 0 and marks
																								// the reactable message
	}

	@SubscribeEvent
	public void configurationReaction(MessageReactionAddEvent event) {
		if (configStep != 0)
			return;

		if (event.getMessageIdLong() == reactionMessage.getIdLong()) {
			Member respondingMember = event.getMember();
			if (respondingMember.getPermissions().contains(Permission.ADMINISTRATOR)) {
				configAdmin = respondingMember;
				configChannels[0].sendMessage(op.SimpleCreateMessage(
						"Admin " + configAdmin.getAsMention() + " will be setting up the initial configuration."))
						.complete();
				configStep = 1;
				configChannels[0].sendMessage(configMessages.get(configStep)).queueAfter(250, TimeUnit.MILLISECONDS);
			} else {
				configChannels[0]
						.sendMessage(op.SimpleCreateMessage("Please have an administrator intialize the application."))
						.queue();
			}
		}
	}

	@SubscribeEvent
	public void configurationOne(MessageReceivedEvent event) {
		if (configStep != 1)
			return;

		if (event.getMember().getIdLong() != configAdmin.getIdLong())
			return;

		if (event.getChannel().getIdLong() != configChannels[0].getIdLong())
			return;

		List<GuildChannel> channelMentions = event.getMessage().getMentions().getChannels();
		if (channelMentions.isEmpty()) {
			configChannels[0].sendMessage(configMessages.get(5)).queue();
			return;
		}

		if (channelMentions.size() > 1) {
			configChannels[0].sendMessage(configMessages.get(6)).queue();
			return;
		}

		if (!((GuildMessageChannel) channelMentions.get(0)).canTalk()) {
			configChannels[0].sendMessage(configMessages.get(7)).queue();
			return;
		}

		configChannels[configStep] = (MessageChannel) channelMentions.get(0);
		configChannels[0]
				.sendMessage(op.SimpleCreateMessage(
						"Successfully set the Admin Channel as " + configChannels[configStep].getAsMention()))
				.queueAfter(250, TimeUnit.MILLISECONDS, m -> {
					configStep = 2;
					configChannels[1].sendMessage(configMessages.get(configStep)).queue();
				});
	}

	@SubscribeEvent
	public void configurationTwo(MessageReceivedEvent event) {
		if (configStep != 2)
			return;

		if (event.getMember().getIdLong() != configAdmin.getIdLong())
			return;

		if (event.getChannel().getIdLong() != configChannels[1].getIdLong())
			return;

		List<GuildChannel> channelMentions = event.getMessage().getMentions().getChannels();
		if (channelMentions.isEmpty()) {
			configChannels[1].sendMessage(configMessages.get(5)).queue();
			return;
		}

		if (channelMentions.size() > 1) {
			configChannels[1].sendMessage(configMessages.get(6)).queue();
			return;
		}

		if (!((GuildMessageChannel) channelMentions.get(0)).canTalk()) {
			configChannels[1].sendMessage(configMessages.get(7)).queue();
			return;
		}

		configChannels[configStep] = (MessageChannel) channelMentions.get(0);
		configChannels[1]
				.sendMessage(op.SimpleCreateMessage(
						"Successfully set the Announcements Channel as " + configChannels[configStep].getAsMention()))
				.queueAfter(250, TimeUnit.MILLISECONDS, m -> {
					configStep = 3;
					configChannels[1].sendMessage(configMessages.get(configStep)).queue();
				});
	}

	@SubscribeEvent
	public void configurationThree(MessageReceivedEvent event) {
		if (configStep != 3)
			return;

		if (event.getMember().getIdLong() != configAdmin.getIdLong())
			return;

		if (event.getChannel().getIdLong() != configChannels[1].getIdLong())
			return;

		List<GuildChannel> channelMentions = event.getMessage().getMentions().getChannels();
		if (channelMentions.isEmpty()) {
			configChannels[1].sendMessage(configMessages.get(5)).queue();
			return;
		}

		if (channelMentions.size() > 1) {
			configChannels[1].sendMessage(configMessages.get(6)).queue();
			return;
		}

		if (!((GuildMessageChannel) channelMentions.get(0)).canTalk()) {
			configChannels[1].sendMessage(configMessages.get(7)).queue();
			return;
		}

		configChannels[configStep] = (MessageChannel) channelMentions.get(0);
		configChannels[1]
				.sendMessage(op.SimpleCreateMessage(
						"Successfully set the Welcome Channel as " + configChannels[configStep].getAsMention()))
				.queueAfter(250, TimeUnit.MILLISECONDS, m -> {
					info.addGuild(event.getGuild(), Arrays.asList(configChannels));
					configStep = 4;
					configChannels[1].sendMessage(configMessages.get(configStep)).queue();
					event.getJDA().getShardManager().removeEventListener(this);
					logger.info("Initial configuration has been completed.");
				});
	}

	private void loadConfigurationMessages() {
		String line;
		configMessages = new ArrayList<MessageCreateData>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(InitialConfiguration.class.getResourceAsStream("/configuration.csv")))) {
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\\|");
				configMessages.add(Integer.parseInt(tokens[0]), op.SimpleCreateMessage(tokens[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
