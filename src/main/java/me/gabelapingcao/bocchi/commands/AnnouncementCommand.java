/**
 * 
 */
package me.gabelapingcao.bocchi.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.gabelapingcao.bocchi.util.BocchiOperations;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * @author Gabe Lapingcao
 *
 */
public class AnnouncementCommand extends GuildCommand {

	private static final String MODAL_ID = "announcement template";
	private static final BocchiOperations op = BocchiOperations.getInstance();
	private GuildChannelUnion announcementChannel = null;

	@Override
	public void call(SlashCommandInteractionEvent event) {
		// TODO set the fallback to this guild's default announcement channel
		announcementChannel = event.getOption("channel", null, OptionMapping::getAsChannel);

		// Create the modal for announcement submission
		TextInput title = TextInput.create("title", "Title", TextInputStyle.SHORT).setPlaceholder("Title")
				.setMinLength(1).setMaxLength(100).build();
		TextInput content = TextInput.create("content", "Content", TextInputStyle.PARAGRAPH)
				.setPlaceholder("Announcement Description").setMinLength(1).setMaxLength(1000).build();
		Modal announcementTemplate = Modal.create(MODAL_ID, "Announcement Template")
				.addActionRows(ActionRow.of(title), ActionRow.of(content)).build();
		event.replyModal(announcementTemplate).queue();
	}

	@SubscribeEvent
	public void announcementSubmission(ModalInteractionEvent event) {
		if (event.getModalId().equals(MODAL_ID)) {
			List<String> values = event.getValues().stream().map(ModalMapping::getAsString).toList();
			MessageCreateData announcement = op.CreateAnnouncement(event.getUser(), values.get(0), values.get(1));
			announcementChannel.asGuildMessageChannel().sendMessage(announcement).queue();
			event.reply("Announcement sent in channel " + announcementChannel.getJumpUrl()).setEphemeral(true).queue();
			announcementChannel = null;
		}
	}

	@Override
	public String getDescription() {
		return "Allows an administrator to create an announcement and send it to the specified channel";
	}

	@Override
	public String getName() {
		return "announcement";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Collections.singletonList("Sends an announcement message to the announcement channel");
	}

	@Override
	public List<OptionData> getOptions() {
		return Arrays.asList(new OptionData(OptionType.CHANNEL, "channel", "The channel to send the announcement to"));
	}

	@Override
	public DefaultMemberPermissions getPermissions() {
		return DefaultMemberPermissions.DISABLED;
	}

}
