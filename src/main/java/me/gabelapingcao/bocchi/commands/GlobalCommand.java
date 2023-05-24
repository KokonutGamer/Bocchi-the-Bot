package me.gabelapingcao.bocchi.commands;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class GlobalCommand implements BocchiCommand {

	@SubscribeEvent
	public void onStartup(ReadyEvent event) {
		SlashCommandData command = Commands.slash(getName(), getDescription());
		event.getJDA().upsertCommand(command).queue();
	}

}
