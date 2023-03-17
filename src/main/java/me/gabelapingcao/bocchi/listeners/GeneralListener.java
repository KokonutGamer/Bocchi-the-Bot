package me.gabelapingcao.bocchi.listeners;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import me.gabelapingcao.bocchi.util.BocchiOperations;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class GeneralListener {
	
	private static final BocchiOperations op = BocchiOperations.getInstance();
	private static final Logger logger = (Logger) LoggerFactory.getLogger(GeneralListener.class);
	
	@SubscribeEvent
	public void onMemberJoin(GuildMemberJoinEvent event) {
		op.getWelcomeChannel(event.getGuild()).sendMessage(op.CreateWelcomeMessage(event.getUser())).queue();
		logger.info("User " + event.getUser().getName() + " joined the guild \"" + event.getGuild().getName() + "\"");
	}
	
}
