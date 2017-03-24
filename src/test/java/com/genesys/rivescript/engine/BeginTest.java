package com.genesys.rivescript;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBegin extends TestBase {

	@Override
	public String replies() {
		return "begin";
	}

	@Test
	public void testNoBeginBlock() {
		this.setUp("no_begin_block.rive");
		this.reply("hello bot", "Hello human.");
	}

	@Test
	public void testSimpleBeginBlock() {
		this.setUp("simple_begin_block.rive");
		this.reply("Hello bot.", "Hello human.");
	}

	@Test
	public void testBlockedBeginBlock() {
		this.setUp("blocked_begin_block.rive");
		this.reply("Hello bot.", "Nope.");
	}

	@Test
	public void testConditionalBeginBlock() {
		this.setUp("conditional_begin_block.rive");

		this.reply("Hello bot.", "Hello human.");
		String expectedMetUservar = rs.getUservar(USER_NAME, "met");
		assertEquals(expectedMetUservar ,"true");

		this.reply("My name is bob", "Hello, Bob.");
		String expectedNameUservar = rs.getUservar(USER_NAME, "name");
		assertEquals(expectedNameUservar, "Bob");

		this.reply("Hello Bot", "Bob: Hello human.");
	}
}
