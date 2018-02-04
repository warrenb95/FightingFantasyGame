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
	
	public void printText(Element page) {
		List<Element> text = page.getChildren("text");
		
		for (Element t : text) {
			System.out.println(t.getText());
			
			System.out.println("###################");
		}
	}
	
	public boolean hasTestLuck(Element page) {
		String hasAttribute = page.getAttributeValue("testLuck", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public String testLuck(Element page, Player player) {
		
		if (player.testLuck()) {
			nextPage =  page.getChild("lucky").getAttributeValue("pageNum");
		} else {
			nextPage =  page.getChild("unlucky").getAttributeValue("pageNum");
		}
		
		return nextPage;
	}
	
	public boolean hasOptions(Element page) {
		String hasAttribute = page.getAttributeValue("options", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public List<String> findOptions(Element page) {
		List<Element> optionElements = page.getChildren("option");
		
		List<String> options = new ArrayList<>();
		
		for(Element option : optionElements) {
			options.add(option.getAttributeValue("pageNum"));
		}
		
		return options;
	}
	
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
	
	public boolean hasTakeDamage(Element page) {
		String hasAttribute = page.getAttributeValue("damage", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public void takeDamage(Element page, Player player) {
		int damage = Integer.parseInt(page.getAttributeValue("damage"));
		
		System.out.println("Damage taken: " + damage);
		
		player.takeDamage(damage);
	}
	
	public boolean hasBattle(Element page) {
		String hasAttribute = page.getAttributeValue("battle", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
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
	
	public boolean battle(Element page, Player player) {
		String battleEnemy = page.getAttributeValue("battle", "single");
		int turns = 0;
		int wins = 0;
		
		boolean victory= false;
		
		List<Enemy> enemies = findEnemies(page);
		
		Battle battle = new Battle();
		
		String battleType = "default";
		
		String hasTurns = page.getChild("battle").getAttributeValue("turns", "none");
		if (!hasTurns.equals("none")) {
			turns = Integer.parseInt(page.getChild("battle").getAttributeValue("turns"));
			battleType = "turns";
		}
		
		String hasConsecutiveWins = page.getChild("battle").getAttributeValue("wins", "none");
		if (!hasConsecutiveWins.equals("none")) {
			wins = Integer.parseInt(page.getChild("battle").getAttributeValue("wins"));
			battleType = "consecutiveWins";
		}
		
		switch (battleType) {
		case "default":
			if (battleEnemy.equals("single") || battleEnemy.equals("seperate")) {
				
				System.out.println("before battle call, victory:  " + victory);
				
				victory = battle.fightSingle(player, enemies, battleType);
				
				System.out.println("after battle call, victory:  " + victory);
				
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
	
	public boolean hasDiceRoll(Element page) {
		String hasAttribute = page.getAttributeValue("diceRoll", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean hasItems(Element page) {
		String hasAttribute = page.getAttributeValue("item", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean hasConstraint(Element page) {
		String hasAttribute = page.getAttributeValue("constraint", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean hasGainStat(Element page) {
		String hasAttribute = page.getAttributeValue("gainStat", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean hasNextPage(Element page) {
		String hasAttribute = page.getChild("nextPage").getAttributeValue("pageNum", "none");
		
		if (!hasAttribute.equals("none")) {
			return true;
		}else {
			return false;
		}
	}
	
	public void findNextPage(Element page) {
		nextPage = page.getChild("nextPage").getAttributeValue("pageNum");
	}
	
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
