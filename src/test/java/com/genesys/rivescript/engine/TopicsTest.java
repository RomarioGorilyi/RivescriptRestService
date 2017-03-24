package com.genesys.rivescript.engine;

import org.junit.Test;

public class TopicsTest extends TestBase {

	@Override
	public String replies() {
		return "topics";
	}

	@Test
	public void testPunishmentTopic() {
		this.setUp("punishment.rive");

		this.reply("hello", "Hi there!");
		this.reply("How are you?", "Catch-all.");
		this.reply("Swear word!", "How rude! Apologize or I won't talk to you again.");
		this.reply("hello", "Say you're sorry!");
		this.reply("How are you?", "Say you're sorry!");
		this.reply("Sorry!", "It's ok!");
		this.reply("hello", "Hi there!");
		this.reply("How are you?", "Catch-all.");
	}

	@Test
	public void testInheritance() {
		this.setUp("inheritance.rive");

		String RS_ERR_MATCH = "ERR: No Reply Matched";

		this.setUservar("topic", "colors");
		this.reply("What color is the sky?", "Blue.");
		this.reply("What color is the sun?", "Yellow.");
		this.reply("What color is grass?", RS_ERR_MATCH);
		this.reply("Name a Red Hat distro.", RS_ERR_MATCH);
		this.reply("Name a Debian distro.", RS_ERR_MATCH);
		this.reply("Say stuff.", RS_ERR_MATCH);

		this.setUservar("topic", "linux");
		this.reply("What color is the sky?", RS_ERR_MATCH);
		this.reply("What color is the sun?", RS_ERR_MATCH);
		this.reply("What color is grass?", RS_ERR_MATCH);
		this.reply("Name a Red Hat distro.", "Fedora.");
		this.reply("Name a Debian distro.", "Ubuntu.");
		this.reply("Say stuff.", RS_ERR_MATCH);

		this.setUservar("topic", "stuff");
		this.reply("What color is the sky?", "Blue.");
		this.reply("What color is the sun?", "Yellow.");
		this.reply("What color is grass?", RS_ERR_MATCH);
		this.reply("Name a Red Hat distro.", "Fedora.");
		this.reply("Name a Debian distro.", "Ubuntu.");
		this.reply("Say stuff.", "\"Stuff.\"");

		this.setUservar("topic", "override");
		this.reply("What color is the sky?", "Blue.");
		this.reply("What color is the sun?", "Purple.");
		this.reply("What color is grass?", RS_ERR_MATCH);
		this.reply("Name a Red Hat distro.", RS_ERR_MATCH);
		this.reply("Name a Debian distro.", RS_ERR_MATCH);
		this.reply("Say stuff.", RS_ERR_MATCH);

		this.setUservar("topic", "morecolors");
		this.reply("What color is the sky?", "Blue.");
		this.reply("What color is the sun?", "Yellow.");
		this.reply("What color is grass?", "Green.");
		this.reply("Name a Red Hat distro.", RS_ERR_MATCH);
		this.reply("Name a Debian distro.", RS_ERR_MATCH);
		this.reply("Say stuff.", RS_ERR_MATCH);

		this.setUservar("topic", "evenmore");
		this.reply("What color is the sky?", "Blue.");
		this.reply("What color is the sun?", "Yellow.");
		this.reply("What color is grass?", "Blue, sometimes.");
		this.reply("Name a Red Hat distro.", RS_ERR_MATCH);
		this.reply("Name a Debian distro.", RS_ERR_MATCH);
		this.reply("Say stuff.", RS_ERR_MATCH);
	}
}
