package com.genesys.rivescript;

import com.rivescript.RiveScript;

import static org.junit.Assert.*;

public class TestBase {

	protected static final String USER_NAME = "localuser";
	private static final String PATH = "src/test/java/com/genesys/rivescript/fixtures/";

	protected RiveScript rs;

	public String replies() {
		return "undefined";
	}

	public void setUp(String file) {
		this.rs = new RiveScript();
		this.rs.loadFile(PATH + this.replies() + "/" + file);
		this.rs.sortReplies();
	}

	public void extend(String file) {
		this.rs.loadFile(PATH + this.replies() + "/" + file);
		this.rs.sortReplies();
	}

	public void setUservar(String key, String value) {
		this.rs.setUservar(USER_NAME, key, value);
	}

	public void reply(String input, String expect) {
		String reply = this.rs.reply(USER_NAME, input);
		assertEquals(expect, reply);
	}

	public void reply(String input, String[] expected) {
		String reply = this.rs.reply(USER_NAME, input);
		for (String expect : expected) {
			if (reply.equals(expect)) {
				assertTrue(true);
				return;
			}
		}

		fail();
	}
}
