package me.gabelapingcao.bocchi.util.menus;

import net.dv8tion.jda.api.utils.messages.MessageEditData;

/**
 * Pages are the canonical name used for the nodes of a Menu which implement a
 * doubly linked list to hold each page. The content of each page can be set
 * using {@code setContent()}. Each page must include either a previous button
 * to direct the user to the previous page, and/or a next button to direct the
 * user to the next page.
 * 
 * @author Gabe Lapingcao
 *
 */
public class Page {

	MessageEditData content;
	Page next, previous;

	public Page(MessageEditData content) {
		this.content = content;
		next = null;
		previous = null;
	}

}
