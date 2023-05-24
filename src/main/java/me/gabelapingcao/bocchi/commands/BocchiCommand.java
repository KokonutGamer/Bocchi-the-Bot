/**
 * 
 */
package me.gabelapingcao.bocchi.commands;

import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Gabe Lapingcao
 *
 */
public interface BocchiCommand {

	void onCommand(SlashCommandInteractionEvent event);

	String getName();

	String getDescription();

	List<OptionData> getOptions();

	List<String> getUsageInstructions();

}
