/**
 * 
 */
package me.gabelapingcao.bocchi.util;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

/**
 * The OperationsAbstractFactory provides a template for operation-based classes
 * working with the Java Discord API (JDA) in order to create universal operations
 * such as uniform message formats and uniform command prompts.
 * 
 * @author Gabe Lapingcao
 * @see net.dv8tion.jda.api.JDA JDA
 */
public abstract class OperationsAbstractFactory {

	protected static final GuildInfoLoader info = GuildInfoLoader.getInstance();
	protected Color color;
	protected TextChannel adminChannel;
	protected TextChannel announcementsChannel;
	protected TextChannel welcomeChannel;

	public abstract MessageCreateData SimpleCreateMessage(String text);

	public abstract void SimpleEditMessage(Message message, String text);

	public abstract MessageCreateData SimpleCreateEmbedMessage(User author, String text);

	public abstract MessageCreateData CreateReactableMessage(User author, String text, Button... buttons);

	public abstract MessageCreateData CreateAnnouncement(User author, String title, String text);

	public abstract MessageCreateData CreateWelcomeMessage(User member);

	public abstract void EditMessage(Message message, MessageEditData data);

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public MessageChannel getAdminChannel(Guild guild) {
		List<MessageChannel> channels = info.getGuildChannels(guild);
		return channels.get(1);
	}

	public void setAdminChannel(TextChannel adminChannel) {
		this.adminChannel = adminChannel;
	}

	public MessageChannel getAnnouncementsChannel(Guild guild) {
		List<MessageChannel> channels = info.getGuildChannels(guild);
		return channels.get(2);
	}

	public void setAnnouncementsChannel(TextChannel announcementsChannel) {
		this.announcementsChannel = announcementsChannel;
	}

	public MessageChannel getWelcomeChannel(Guild guild) {
		List<MessageChannel> channels = info.getGuildChannels(guild);
		return channels.get(3);
	}

	public void setWelcomeChannel(TextChannel welcomeChannel) {
		this.welcomeChannel = welcomeChannel;
	}

}
