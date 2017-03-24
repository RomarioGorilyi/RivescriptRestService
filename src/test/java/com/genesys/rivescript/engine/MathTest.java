package com.genesys.rivescript.engine;

import org.junit.Test;

public class MathTest extends TestBase {

	@Override
	public String replies() {
		return "math";
	}

	@Test
	public void testMath() {
		this.setUp("math.rive");

		this.reply("test counter", "counter set");
		this.reply("show", "counter = 0");

		this.reply("set counter 5", "counter set 5");
		this.reply("show", "counter = 5");

		this.reply("add 2", "adding");
		this.reply("show", "counter = 7");

		this.reply("sub 3", "subbing");
		this.reply("show", "counter = 4");

		this.reply("div 4", "divving");
		this.reply("show", "counter = 1");

		this.reply("mult 10", "multing");
		this.reply("show", "counter = 10");
	}
}
