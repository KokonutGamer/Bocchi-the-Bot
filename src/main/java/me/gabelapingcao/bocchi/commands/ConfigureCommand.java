package me.gabelapingcao.bocchi.commands;

import java.util.List;

import me.gabelapingcao.bocchi.util.menus.Menu;
import me.gabelapingcao.bocchi.util.menus.MenuComponent;
import me.gabelapingcao.bocchi.util.menus.Page;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class ConfigureCommand extends GuildCommand {

	@Override
	public String getName() {
		return "configure";
	}

	@Override
	public String getDescription() {
		return "Configure guild settings for Bocchi";
	}

	@Override
	public List<OptionData> getOptions() {
		return null;
	}

	@Override
	public List<String> getUsageInstructions() {
		return null;
	}

	@Override
	public void call(SlashCommandInteractionEvent event) {
		EntitySelectMenu adminChannelSelectMenu = EntitySelectMenu
				.create("admin_channel_selection", SelectTarget.CHANNEL)
				.setChannelTypes(ChannelType.TEXT)
				.setPlaceholder("Set the admin channel")
				.setRequiredRange(1, 1).build();
		MenuComponent adminComponent = new MenuComponent(adminChannelSelectMenu,
				menuEvent -> menuEvent.reply("received!").queue());
		Page page1 = Page.create().addContent("Page 1").addComponents(ActionRow.of(adminComponent)).build();

		EntitySelectMenu announcementsChannelSelectMenu = EntitySelectMenu
				.create("announcement_channel_selection", SelectTarget.CHANNEL)
				.setChannelTypes(ChannelType.TEXT)
				.setPlaceholder("Set the announcements channel")
				.setRequiredRange(1, 1).build();
		MenuComponent announceComponent = new MenuComponent(announcementsChannelSelectMenu,
				menuEvent -> menuEvent.reply("Received!").queue());
		Page page2 = Page.create().addContent("Page 2").addComponents(ActionRow.of(announceComponent)).build();

		EntitySelectMenu welcomeChannelSelectMenu = EntitySelectMenu
				.create("welcome_channel_selection", SelectTarget.CHANNEL)
				.setChannelTypes(ChannelType.TEXT)
				.setPlaceholder("Set the welcome channel")
				.setRequiredRange(1, 1).build();
		MenuComponent welcomeComponent = new MenuComponent(welcomeChannelSelectMenu,
				menuEvent -> menuEvent.reply("Received").queue());
		Page page3 = Page.create().addContent("Page 3").addComponents(ActionRow.of(welcomeComponent)).build();

		Menu testMenu = Menu.create().addPages(page1, page2, page3).build();
		event.reply((MessageCreateData) testMenu.display()).queue();
	}

	@Override
	public DefaultMemberPermissions getPermissions() {
		return DefaultMemberPermissions.DISABLED;
	}

}
