package me.gabelapingcao.bocchi.util.internal;

import java.util.ArrayList;
import java.util.List;

import me.gabelapingcao.bocchi.util.menus.MenuComponent;
import me.gabelapingcao.bocchi.util.menus.Page;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageData;

public class PageImplementation implements Page {

	private final MessageCreateData data;

	public PageImplementation(String content, List<MessageEmbed> embeds, List<FileUpload> files,
			List<LayoutComponent> components, boolean tts) {
		this.data = new MessageCreateBuilder().setContent(content).setEmbeds(embeds).setFiles(files)
				.setComponents(components).setTTS(tts).build();
	}

	@Override
	public void handle(GenericComponentInteractionCreateEvent event) {
		for (LayoutComponent layout : data.getComponents()) {
			layout.getComponents().forEach(item -> ((MenuComponent) item).handle(event));
		}
	}

	@Override
	public MessageData getMessageData() {
		return data;
	}

	@Override
	public List<MenuComponent> getComponents() {
		List<MenuComponent> menuComponents = new ArrayList<>();
		for (LayoutComponent layout : data.getComponents()) {
			menuComponents.addAll(layout.getComponents().stream().map(item -> (MenuComponent) item).toList());
		}
		return menuComponents;
	}

}
