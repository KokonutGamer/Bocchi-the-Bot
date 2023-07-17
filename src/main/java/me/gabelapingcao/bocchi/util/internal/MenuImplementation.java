package me.gabelapingcao.bocchi.util.internal;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.LoggerFactory;

import me.gabelapingcao.bocchi.util.menus.Menu;
import me.gabelapingcao.bocchi.util.menus.Page;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class MenuImplementation implements Menu {

	private final List<Page> pages;
	private final ListIterator<Page> pageIterator;

	private static final String PREV_BUTTON_ID = "prev_button";
	private static final String NEXT_BUTTON_ID = "next_button";
	private Button prev = Button.primary(PREV_BUTTON_ID, Emoji.fromUnicode("U+25C0"));
	private Button next = Button.primary(NEXT_BUTTON_ID, Emoji.fromUnicode("U+25B6"));

	private Page curr;

	public MenuImplementation(List<Page> pages) {
		this.pages = Collections.unmodifiableList(pages);
		pageIterator = pages.listIterator();
		curr = pages.get(0);
	}

	@Override
	public List<Page> getPages() {
		return pages;
	}

	@Override
	public MessageData display() {
		// Precondition: curr.getMessageData() is of type MessageCreateData
		MessageCreateBuilder builder = MessageCreateBuilder.from((MessageCreateData) curr.getMessageData());
//		if (curr instanceof MessageCreateData) {
//			builder = MessageCreateBuilder.from((MessageCreateData) curr);
//		} else if (curr instanceof MessageEditData) {
//			builder = MessageCreateBuilder.fromEditData((MessageEditData) curr);
//		} else {
//			throw new UnknownError(
//					"The current MessageData passed into this Menu is neither an instance of MessageCreateData nor MessageEditData");
//		}

		prev = prev.withDisabled(!pageIterator.hasPrevious());
		pageIterator.next(); // Set the iterator to the right of the current MessageData
		next = next.withDisabled(!pageIterator.hasNext());
		pageIterator.previous(); // Set the iterator to the left of the current MessageData
		builder.addActionRow(prev, next);

		return builder.build();
	}

	/**
	 * Process the event called by the MenuListener.
	 */
	@Override
	public void process(GenericComponentInteractionCreateEvent event) {
		String componentId = event.getComponentId();

		pages.forEach(page -> page.handle(event));

		if (event.isAcknowledged()) {
			return;
		}

		if (!componentId.equals(PREV_BUTTON_ID) && !componentId.equals(NEXT_BUTTON_ID)) {
			return;
		}

		MessageEditBuilder builder = null;

		if (componentId.equals(PREV_BUTTON_ID)) {
			curr = pageIterator.previous();
			builder = MessageEditBuilder.fromCreateData((MessageCreateData) display());
		} else if (componentId.equals(NEXT_BUTTON_ID)) {
			pageIterator.next(); // Pre-emptive move to in-between curr and next
			curr = pageIterator.next();
			pageIterator.previous(); // Move back to the left side of curr
			builder = MessageEditBuilder.fromCreateData((MessageCreateData) display());
		}

		event.editMessage(builder.build()).queue();
	}
}
