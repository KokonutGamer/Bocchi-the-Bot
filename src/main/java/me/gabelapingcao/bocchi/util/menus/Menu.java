package me.gabelapingcao.bocchi.util.menus;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import me.gabelapingcao.bocchi.listeners.MenuListener;
import me.gabelapingcao.bocchi.util.internal.MenuImplementation;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.utils.messages.MessageData;
import net.dv8tion.jda.internal.utils.Checks;

/**
 * Menus are immutable objects that store Page instances as a doubly linked
 * list. These objects can be seen as iterators in a way where the currently
 * accessed Page is the one that is displayed on as the MessageData through
 * Discord.
 * 
 * @author Gabe Lapingcao
 *
 */
public interface Menu {

	int MAX_COMPONENT_COUNT = 5;
	int MAX_LABEL_LENGHT = 80;
	int MAX_PAGES = 10;

	List<Page> getPages();

	MessageData display();

	void process(GenericComponentInteractionCreateEvent event);

	default Menu.Builder createCopy() {
		return new Builder().addPages(getPages());
	}

	static Menu.Builder create() {
		return new Menu.Builder();
	}

	class Builder {

		private final List<Page> pages = new LinkedList<>();

		Builder() {
		}

		public Builder addPages(Page... messages) {
			return addPages(Arrays.asList(messages));
		}

		public Builder addPages(Collection<? extends Page> messages) {
			Checks.noneNull(messages, "Messages");
			messages.forEach(
					message -> Checks.check(message.getComponents().size() <= MAX_COMPONENT_COUNT - 1, "Message"));
			pages.addAll(messages);
			return this;
		}

		public List<Page> getPages() {
			return pages;
		}

		public Menu build() {
			Checks.check(!pages.isEmpty(), "Cannot make a menu without pages!");
			Checks.check(pages.size() <= MAX_PAGES, "Cannot make a menu with more than 10 pages!");
			MenuImplementation build = new MenuImplementation(pages);
			MenuListener.getInstance().register(build);
			return build;
		}

	}

}
