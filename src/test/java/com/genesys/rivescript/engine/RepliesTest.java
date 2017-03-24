package com.genesys.rivescript.engine;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RepliesTest extends TestBase {

	@Override
	public String replies() {
		return "replies";
	}

	@Test
	public void testPrevious() {
		this.setUp("previous.rive");

		this.reply("Knock knock", "Who's there?");
		this.reply("Canoe", "Canoe who?");
		this.reply("Canoe help me with my homework?", "Haha! Canoe help me with my homework!");
		this.reply("hello", "I don't know.");
	}

	@Test
	public void testRandom() {
		this.setUp("random.rive");

		this.reply("test random response", new String[]{
			"One.",
			"Two.",
		});
		this.reply("test random tag", new String[]{
			"This sentence has a random word.",
			"This sentence has a random bit.",
		});
	}

	@Test
	public void testContinuations() {
		this.setUp("continuations.rive");

		this.reply("Tell me a poem.", "There once was a man named Tim, "
			+ "who never quite learned how to swim. "
			+ "He fell off a dock, and sank like a rock, "
			+ "and that was the end of him.");
	}

	@Test
	public void testRedirects() {
		this.setUp("redirects.rive");

		this.reply("hello", "Hi there!");
		this.reply("hey", "Hi there!");
		this.reply("hi there", "Hi there!");
	}

	@Test
	public void testConditionals() {
		this.setUp("conditionals.rive");

		String ageQuestion = "What can I do?";
		this.reply(ageQuestion, "I don't know.");

		Map<String, String> ages = new HashMap<>();
		ages.put("16", "Not much of anything.");
		ages.put("18", "Vote.");
		ages.put("20", "Vote.");
		ages.put("22", "Drink.");
		ages.put("24", "Drink.");
		ages.put("25", "Rent a car for cheap.");
		ages.put("27", "Anything you want.");

		for (Object o : ages.entrySet()) {
			Map.Entry pair = (Map.Entry) o;
			this.reply("I am " + pair.getKey() + " years old.", "OK.");
			this.reply(ageQuestion, (String) pair.getValue());
		}

		this.reply("Am I your master?", "No.");
		this.setUservar("master", "true");
		this.reply("Am I your master?", "Yes.");
	}

	@Test
	public void testSetUservars() {
		this.setUp("set-uservars.rive");
		this.setUservar("name", "Aiden");
		this.setUservar("age", "5");

		this.reply("What is my name?", "Your name is Aiden.");
		this.reply("how old am I?", "You are 5.");
	}

	@Test
	public void testQuestionMark() {
		this.setUp("question-mark.rive");

		this.reply("google java", "<a href=\"https://www.google.com/search?q=java\">Results are here</a>");
	}
}
