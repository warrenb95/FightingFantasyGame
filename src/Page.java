import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/*
 * This is the page handler
 */

public class Page {
	
	// Instance variables
	private String nextPage = "";
	private Element page;
	boolean victory = false;
	
	// This reads all of the information on a page
	public String readPage(String nextPage, Player player, Enemy galthazzeth) {
		
		Element page = findPage(nextPage);
		
		printText(page);
		
		if (hasTestLuck(page)) {
			nextPage = testLuck(page, player);
		} else if (hasOptions(page)) {
			nextPage = pickOption(page);
		} else if (hasTakeDamage(page)) {
			takeDamage(page, player);
		} else if (hasBattle(page)) {
			victory = battle(page, player);
			nextPage = battleNextPage(page, victory);
		}
		
		System.out.println("Next page: "+ nextPage);
		
		return nextPage;
	}
	
	// Finds the page to be displayed and returns it
	public Element findPage(String nextPage) {
		SAXBuilder builder = new SAXBuilder();
		
		try {
			// Parses the file supplied into the JDOM document
			Document book = builder.build(new File("src\\test.xml"));
			
			Element root = book.getRootElement();
			
			List<Element> pages = root.getChildren("page");
			
			for (Element place : pages) {
				if (place.getAttributeValue("pageNum").equals(nextPage)) {
					
					page = place;
					break;
				}
			}
		} 
		catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return page;
	}
	
	// Prints the text of the current page
	public void printText(Element page) {
		List<Element> text = page.getChildren("text");
		
		for (Element t : text) {
			System.out.println(t.getText());
		}
	}
	
	// Query page for test luck attribute
	public boolean hasTestLuck(Element page) {
		String hasAttribute = page.getAttributeValue("testLuck", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Handles the test luck on the page
	public String testLuck(Element page, Player player) {
		
		if (player.testLuck()) {
			nextPage =  page.getChild("lucky").getAttributeValue("pageNum");
		} else {
			nextPage =  page.getChild("unlucky").getAttributeValue("pageNum");
		}
		
		return nextPage;
	}
	
	// Query page for options attribute
	public boolean hasOptions(Element page) {
		String hasAttribute = page.getAttributeValue("options", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Finds all the options on the page and adds them to a list
	public List<String> findOptions(Element page) {
		List<Element> optionElements = page.getChildren("option");
		
		List<String> options = new ArrayList<>();
		
		for(Element option : optionElements) {
			options.add(option.getAttributeValue("pageNum"));
		}
		
		return options;
	}
	
	// Get the user to pick an option from the list
	public String pickOption(Element page) {
		
		List<String> options = findOptions(page);
		
		String choice;
		
		System.out.print("Pick an option from the following: ");
		for (String option : options) {
			System.out.print(option + " ");
		}
		
		Scanner reader = new Scanner(System.in);
		
		System.out.println();
		System.out.print("Choice: ");
		choice = reader.nextLine();
		
		boolean incorrect = true;
		
		// Validate the answer
		while(incorrect) {
			for (String option : options) {
				if(choice.equals(option)) {
					
					return choice;
				}
			}
			
			System.out.print("Please enter a correct choice: ");
			choice = reader.nextLine();
		}
		
		return choice;
	}
	
	// Query page for damage attribute
	public boolean hasTakeDamage(Element page) {
		String hasAttribute = page.getAttributeValue("damage", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Player takes the damage
	public void takeDamage(Element page, Player player) {
		int damage = Integer.parseInt(page.getAttributeValue("damage"));
		
		System.out.println("Damage taken: " + damage);
		
		player.takeDamage(damage);
	}
	
	// Query page for battle attribute
	public boolean hasBattle(Element page) {
		String hasAttribute = page.getAttributeValue("battle", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Finds the enemies and return a list of Enemy objects
	public List<Enemy> findEnemies(Element page){
		
		List<Element> enemyElements =  page.getChild("battle").getChildren("enemy");
		
		List<Enemy> enemies = new ArrayList<>(); 
		
		for (Element element : enemyElements) {
			int skill = Integer.parseInt(element.getAttributeValue("skill"));
			int stamina = Integer.parseInt(element.getAttributeValue("stamina"));
			
			Enemy enemy = new Enemy(skill,stamina);
			
			enemies.add(enemy);
		}
		
		return enemies;
		
	}
	
	// Handles the battle on the page and calls the battle class to deal with it 
	public boolean battle(Element page, Player player) {
		String battleEnemy = page.getAttributeValue("battle", "single");
		int turns = 0;
		int wins = 0;
		
		boolean victory= false;
		
		List<Enemy> enemies = findEnemies(page);
		
		Battle battle = new Battle();
		
		// Set the default battleType to default
		String battleType = "default";
		
		// Find and set the battleType
		String hasTurns = page.getChild("battle").getAttributeValue("turns", "none");
		if (!hasTurns.equals("none")) {
			turns = Integer.parseInt(page.getChild("battle").getAttributeValue("turns"));
			battleType = "turns";
		}
		
		String hasConsecutiveWins = page.getChild("battle").getAttributeValue("wins", "none");
		if (!hasConsecutiveWins.equals("none")) {
			wins = Integer.parseInt(page.getChild("battle").getAttributeValue("wins"));
			battleType = "wins";
		}
		
		// Calls Battle class depending on battleType
		switch (battleType) {
		case "default":
			if (battleEnemy.equals("single") || battleEnemy.equals("seperate")) {
				
				victory = battle.fightSingle(player, enemies, battleType);
				
			} else if (battleEnemy.equals("together")) {
				
				victory = battle.fightTogether(player, enemies, battleType);
			}
			break;
		case "turns":
			if (battleEnemy.equals("single") || battleEnemy.equals("seperate")) {
				
				victory = battle.fightSingle(player, enemies, battleType, turns);
			} else if (battleEnemy.equals("together")) {
				
				victory = battle.fightTogether(player, enemies, battleType, turns);
			}
			break;
		case "wins":
			if (battleEnemy.equals("single") || battleEnemy.equals("seperate")) {
				
				victory = battle.fightSingle(player, enemies, battleType, wins);
			} else if (battleEnemy.equals("together")) {
				
				victory = battle.fightTogether(player, enemies, battleType, wins);
			}
			break;
		}
		return victory;
	}
	
	// Query page for dice roll attribute
	public boolean hasDiceRoll(Element page) {
		String hasAttribute = page.getAttributeValue("diceRoll", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// This handles the dice roll of the page
	public void diceRoll(Element page) {
		int diceAmount = Integer.parseInt(page.getAttributeValue("diceRoll"));
		int rolls = 0;
		
		// Stopped here I need to comment all this code ASAP before I forget.
	}
	
	// Query page for items attribute
	public boolean hasItems(Element page) {
		String hasAttribute = page.getAttributeValue("item", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Query page for constraint attribute
	public boolean hasConstraint(Element page) {
		String hasAttribute = page.getAttributeValue("constraint", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Query page for gain attribute
	public boolean hasGainStat(Element page) {
		String hasAttribute = page.getAttributeValue("gainStat", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Query page for next page attribute
	public boolean hasNextPage(Element page) {
		String hasAttribute = page.getChild("nextPage").getAttributeValue("pageNum", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	// Finds the next page if there is nothing to do on the page
	public void findNextPage(Element page) {
		nextPage = page.getChild("nextPage").getAttributeValue("pageNum");
	}
	
	// Finds the next page if there has been a battle on the page
	public String battleNextPage(Element page, boolean victory) {
		String nextPage;
		if (victory) {
			nextPage = page.getChild("battle").getChild("win").getAttributeValue("pageNum");
		} else {
			nextPage = page.getChild("battle").getChild("lose").getAttributeValue("pageNum");
		}
		return nextPage;
	}
	
}
