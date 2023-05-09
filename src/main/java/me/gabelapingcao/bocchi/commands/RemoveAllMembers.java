package me.gabelapingcao.bocchi.commands;

import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import me.gabelapingcao.bocchi.util.BocchiOperations;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RemoveAllMembers extends Command {

	private static final BocchiOperations op = BocchiOperations.getInstance();
	private static final Logger log = (Logger) LoggerFactory.getLogger(RemoveAllMembers.class);
	
	@Override
	public void onCommand(MessageReceivedEvent event, String[] args) {
		if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			MessageCreateData message = op.SimpleCreateMessage("You can't do that!!!");
			event.getChannel().sendMessage(message);
			return;
		}
		List<Member> members = event.getGuild().getMembers();
		log.debug(members.toString());
		for(Member member : members) {
			if(member.hasPermission(Permission.ADMINISTRATOR) || member.getUser().isBot()) {
				continue;
			}
			event.getGuild().kick(member).queue();
		}
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(".kickAll");
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getName() {
		return "Kick All Members";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
