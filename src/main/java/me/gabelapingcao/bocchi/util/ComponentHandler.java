package me.gabelapingcao.bocchi.util;

import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

/**
 * Represents an operation that handles the input of a
 * GenericComponentInteractionCreateEvent.
 * 
 * <p>
 * This is a functional interface whose functional method is
 * {@link #handle(GenericComponentInteractionCreateEvent)}.
 * 
 * @author Gabe Lapingcao
 *
 */

@FunctionalInterface
public interface ComponentHandler {

	/**
	 * Handles this event by performing the defined operation.
	 * 
	 * @param event
	 */
	void handle(GenericComponentInteractionCreateEvent event);

}
