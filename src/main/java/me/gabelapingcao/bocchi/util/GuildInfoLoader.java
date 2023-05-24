package me.gabelapingcao.bocchi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

/**
 * The <b>GuildInfoLoader</b> class assists Bocchi with configuring any saved
 * guilds and adding new guilds to be stored on a system (usually my computer).
 * 
 * @author Gabe Lapingcao
 */
public class GuildInfoLoader {

	// TODO instead of creating a custom loader, use GSON to load Guild
	// Configuration Information

	private static final Logger logger = (Logger) LoggerFactory.getLogger(GuildInfoLoader.class);
	private File info;
	private Map<Long, List<Long>> guilds;

	/**
	 * The constructor for GuildInfoLoader loads the guild-info.csv file and sets up
	 * any methods to allow reading and writing to this file outside of the class
	 * itself.
	 */
	private GuildInfoLoader() {
		info = new File("/Users/School/eclipse-workspace/STEM-bot/src/main/resources/guild-info.csv");

		// Read all of the current information on the file and save it to a Map
		loadGuildInfo();
	}

	private static class SingletonHelper {
		private static final GuildInfoLoader INSTANCE = new GuildInfoLoader();
	}

	public static GuildInfoLoader getInstance() {
		return SingletonHelper.INSTANCE;
	}

	private void loadGuildInfo() {
		String line;
		guilds = new ConcurrentHashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(info)))) {
			while ((line = br.readLine()) != null) {
				String[] tokensAsStrings = line.split("\\|");
				long[] tokens = new long[tokensAsStrings.length];
				for (int i = 0; i < tokens.length; i++) {
					tokens[i] = Long.parseLong(tokensAsStrings[i]);
				}
				if (!guilds.containsKey(tokens[0])) {
					List<Long> channels = Arrays.stream(Arrays.copyOfRange(tokens, 1, tokens.length)).boxed().toList();
					guilds.put(tokens[0], channels);
				}
			}
			logger.info("All guilds loaded successfully!");
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e + ": an error occured when loading the guild-info file.");
		}
	}

	public void addGuild(Guild guild) {
		addGuild(guild, null);
	}

	public void addGuild(Guild guild, List<MessageChannel> channels) {
		List<Long> channelIds = channels.stream().map(ch -> ch.getIdLong()).collect(Collectors.toList());
		if (!guilds.containsKey(guild.getIdLong())) {
			guilds.put(guild.getIdLong(), channelIds);
			logger.info("Successfully added the guild \"" + guild.getName() + "\"");
			writeGuildInfo(guild.getIdLong(), ArrayUtils.toPrimitive(channelIds.toArray(new Long[0])));
		}
	}

	private void writeGuildInfo(long guild, long... channels) {
		String line;
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(info)))) {
			line = Long.toString(guild);
			for (long channel : channels) {
				line += "|" + channel;
			}
			out.println(line);
		} catch (FileNotFoundException e) {
			logger.error(e + ": could not find the guild-info file on the system");
		} catch (IOException e) {
			logger.error(e + ": an error occured when writing to the guild-info file");
		}
	}

	public List<MessageChannel> getGuildChannels(Guild guild) {
		if (!guilds.containsKey(guild.getIdLong())) {
			return null;
		}
		return guilds.get(guild.getIdLong()).stream().map(id -> guild.getChannelById(MessageChannel.class, id))
				.collect(Collectors.toList());
	}

}
