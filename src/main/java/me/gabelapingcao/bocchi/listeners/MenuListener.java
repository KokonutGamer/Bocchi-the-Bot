package me.gabelapingcao.bocchi.listeners;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import me.gabelapingcao.bocchi.util.menus.Menu;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class MenuListener {

	List<Menu> registeredMenus;

	private static class SingletonHelper {
		private static final MenuListener INSTANCE = new MenuListener();
	}

	private MenuListener() {
		registeredMenus = new LinkedList<Menu>();
	}

	@SubscribeEvent
	public void onMenuComponentInteraction(GenericComponentInteractionCreateEvent event) {
		event.deferEdit();
		registeredMenus.forEach(menu -> menu.process(event));
	}

	public void register(Menu menu) {
		registeredMenus.add(0, menu);
	}

	public void register(Menu... menus) {
		register(Arrays.asList(menus));
	}

	public void register(Collection<Menu> menus) {
		menus.forEach(menu -> register(menu));
	}

	public void unregister(Menu menu) {
		if (registeredMenus.contains(menu)) {
			registeredMenus.remove(menu);
		}
	}

	public void unregister(Menu... menus) {
		unregister(Arrays.asList(menus));
	}

	public void unregister(Collection<Menu> menus) {
		menus.forEach(menu -> unregister(menu));
	}

	public static MenuListener getInstance() {
		return SingletonHelper.INSTANCE;
	}

}
