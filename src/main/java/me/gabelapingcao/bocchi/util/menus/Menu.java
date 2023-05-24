package me.gabelapingcao.bocchi.util.menus;

import net.dv8tion.jda.api.utils.messages.MessageEditData;

/**
 * Menu implements Page objects as nodes stored as a doubly linked list.
 * 
 * @author Gabe Lapingcao
 *
 */
public class Menu {

	Page head, tail;
	int size;

	Menu() {
		head = null;
		tail = null;
		size = 0;
	}

	public boolean hasNext() {
		// TODO implement this
		return false;
	}

	public boolean hasPrevious() {
		// TODO implement this
		return false;
	}

	public MessageEditData next() {
		// TODO implement editing a message based on this
		// add "next" and "previous" buttons after getting the next Page
		// "next" should be disabled if next is null
		// "previous" should be disabled if previous is null
		return null;
	}

	public MessageEditData previous() {
		// TODO implement editing a message based on this
		// add "next" and "previous" buttons after getting the previous Page
		// "next" should be disabled if next is null
		// "previous" should be disabled if previous is null
		return null;
	}

}
