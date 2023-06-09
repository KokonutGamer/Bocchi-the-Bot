package me.gabelapingcao.bocchi;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;
import me.gabelapingcao.bocchi.commands.AnnouncementCommand;
import me.gabelapingcao.bocchi.commands.HelpCommand;
import me.gabelapingcao.bocchi.commands.InfoCommand;
import me.gabelapingcao.bocchi.listeners.GeneralListener;
import me.gabelapingcao.bocchi.listeners.InitialConfiguration;
import me.gabelapingcao.bocchi.util.InheritedAnnotatedEventManager;

public class Bocchi {

	private final Dotenv config;
	private final ShardManager shardManager;

	/**
	 * Loads environment variables and builds the bot shard manager.
	 * 
	 * @throws LoginException when the bot token is invalid.
	 */
	private Bocchi() throws LoginException {
		config = Dotenv.configure().load();
		String token = config.get("TOKEN");
		HelpCommand help = new HelpCommand();
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
				GatewayIntent.MESSAGE_CONTENT);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.playing(" I am dying of burnout"));
		builder.setEventManagerProvider(id -> {
			return new InheritedAnnotatedEventManager();
		});
		builder.addEventListeners(new InitialConfiguration(), new GeneralListener(), help.registerCommand(help),
				help.registerCommand(new InfoCommand()), help.registerCommand(new AnnouncementCommand()));

		shardManager = builder.build();
	}

	/**
	 * Retrieves the Dotenv file.
	 * 
	 * @return the Dotenv file for the bot.
	 */
	public Dotenv getConfig() {
		return config;
	}

	/**
	 * Retrieves the bot shard manager.
	 * 
	 * @return the ShardManager instance for the bot.
	 */
	public ShardManager getShardManager() {
		return shardManager;
	}

	public static void main(String[] args) throws LoginException {
		@SuppressWarnings("unused")
		Bocchi bot = new Bocchi();
	}

}
