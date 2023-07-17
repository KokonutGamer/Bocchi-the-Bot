package me.gabelapingcao.bocchi.util.menus;

import java.util.function.Consumer;

import me.gabelapingcao.bocchi.util.ComponentHandler;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.ActionComponent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.utils.data.DataObject;

/**
 * Adapter class for the ActionComponent class. Adapts the class to adapt its
 * own handler for handling a GenericComponentInteractionCreateEvent that
 * corresponds to the ActionComponent's ID.
 * 
 * @author Gabe Lapingcao
 *
 */
public class MenuComponent implements ItemComponent, ComponentHandler {

	ActionComponent component;
	Consumer<GenericComponentInteractionCreateEvent> action;

	public MenuComponent(ActionComponent component, Consumer<GenericComponentInteractionCreateEvent> action) {
		if (!component.isMessageCompatible()) {
			throw new IllegalArgumentException(
					"Cannot create an instance of a MenuComponent on an ActionComponent that is not compatible with Messages!");
		}
		if(action == null) {
			throw new IllegalArgumentException("Cannot create a MenuComponent with a null action!");
		}
		this.component = component;
		this.action = action;
	}

	@Override
	public void handle(GenericComponentInteractionCreateEvent event) {
		if (event.getComponentId().equals(component.getId())) {
			action.accept(event);
		}
	}

	@Override
	public Type getType() {
		return component.getType();
	}

	@Override
	public DataObject toData() {
		return component.toData();
	}

}
