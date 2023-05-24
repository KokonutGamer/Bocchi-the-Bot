/**
 * 
 */
package me.gabelapingcao.bocchi.util;

import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

/**
 * @author Gabe Lapingcao
 *
 */
public class BocchiOperations extends OperationsAbstractFactory {

	private final MessageCreateBuilder message = new MessageCreateBuilder();
	private final MessageEditBuilder editor = new MessageEditBuilder();
	private final EmbedBuilder embed = new EmbedBuilder();

	private BocchiOperations() {
	}

	// Instantiate the BocchiOperations Singleton using the Bill Pugh Singleton
	// implementation
	private static class SingletonHelper {
		private static final BocchiOperations INSTANCE = new BocchiOperations();
	}

	// The implementation is called within the getInstance() method to ensure that
	// the SingletonHelper class is only loaded into memory when needed
	public static BocchiOperations getInstance() {
		return SingletonHelper.INSTANCE;
	}

	@Override
	public MessageCreateData SimpleCreateMessage(String text) {
		message.clear();
		message.setContent(text);
		return message.build();
	}

	@Override
	public void SimpleEditMessage(Message message, String text) {
		editor.clear();
		editor.setContent(text);
		message.editMessage(this.editor.build());
	}

	@Override
	public MessageCreateData SimpleCreateEmbedMessage(User author, String text) {
		resetEmbed();
		embed.setAuthor(author.getName(), null, author.getAvatarUrl());
		embed.setDescription(text);

		return message.setEmbeds(embed.build()).build();
	}

	@Override
	public MessageCreateData CreateReactableMessage(User author, String text, Button... buttons) {
		resetEmbed();
		embed.setAuthor(author.getName(), null, author.getAvatarUrl());
		embed.setDescription(text);

		return message.setEmbeds(embed.build()).addActionRow(buttons).build();
	}

	@Override
	public MessageCreateData CreateAnnouncement(User author, String title, String text) {
		resetEmbed();
		embed.setAuthor(author.getName(), null, author.getAvatarUrl());
		embed.setTitle(title);
		embed.setDescription(text);
		embed.setTimestamp(Instant.now());

		return message.setEmbeds(embed.build()).build();
	}

	@Override
	public MessageCreateData CreateWelcomeMessage(User member) {
		resetEmbed();
		embed.setDescription("Welcome " + member.getAsMention() + " to the server!");
		embed.setFooter("This is an automated message from Bocchi the bot");

		return message.setEmbeds(embed.build()).build();
	}

	@Override
	public void EditMessage(Message message, MessageEditData data) {
		message.editMessage(data);
	}

	private void resetEmbed() {
		embed.clear();
		embed.setColor(getColor());
		message.clear();
	}
}
