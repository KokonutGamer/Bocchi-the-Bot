package me.gabelapingcao.bocchi;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;
import me.gabelapingcao.bocchi.commands.AnnouncementCommand;
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
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
				GatewayIntent.MESSAGE_CONTENT);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL); // Cache all members visible to JDA (within the same
																// server)
		builder.setChunkingFilter(ChunkingFilter.ALL); // Caches all member on startup using lazy loading
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.playing(" I am dying of burnout"));
		builder.setEventManagerProvider(id -> {
			return new InheritedAnnotatedEventManager();
		});
		builder.addEventListeners(new AnnouncementCommand());

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
