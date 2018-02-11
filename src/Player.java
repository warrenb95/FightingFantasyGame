/*
 * This is the Character class
 */

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Player {
	// Character attributes
	private String name;
	
	private int skill;
	private int stamina;
	private int luck;
	
	private int initialSkill;
	private int initialStamina;
	private int initialLuck;
	
	private List<Item> backpack = new ArrayList<>();
	
	// Construct the character
	Player(){
		initialSkill = skill = rollDice() + 6;
		initialStamina = stamina = rollDice() + rollDice() + 12;
		initialLuck = luck = rollDice() + 6;
		//setName();
	}
	
	public void setName() {
		Scanner reader = new Scanner(System.in);
		
		System.out.print("Enter a name: ");
		name = reader.nextLine();
		boolean incorrect = true;
		
		while (incorrect) {
			System.out.print("Is " + name + " correct? y/n: ");
			String ans = reader.nextLine();
			
			if(ans.equals("y")) {
				incorrect = false;
			} else {
				System.out.print("Enter a new name: ");
				name = reader.nextLine();
			}
		}
		reader.close();
	}
	
	public int rollDice() {
		return ThreadLocalRandom.current().nextInt(1, 6 + 1);
	}
	
	public int getStamina() {
		return stamina;
	}
	
	public int getSkill() {
		return skill;
	}
	
	public void takeDamage(int damage) {
		stamina -= damage;
	}
	
	public boolean isAlive() {
		if(stamina > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean testLuck() {
		int score = rollDice() + rollDice();
		
		if(luck >= score) {
			luck--;
			return true;
		} else {
			luck--;
			return false;
		}
	}
	
	// Consuming potions Cannot surpass initial values
	public void regenStamina(int increase) {
		if ((stamina + increase) > initialStamina) {
			stamina = initialStamina;
		} else {
			stamina += increase;
		}
	}
	
	public void regenLuck(int increase) {
		if ((luck + increase) > initialLuck) {
			luck = initialLuck;
		} else {
			luck += increase;
		}
	}

	public String makeChoice(String[] nextPages) {
		// Print out the options and take in a ans
		
		System.out.print("Pick from the following: ");
		
		for (String option : nextPages) {
			System.out.print(option + "  ");
		}
		
		Scanner reader = new Scanner(System.in);
		
		String ans = reader.nextLine();
		
		// Validate ans
		boolean correct = false;
		
		while (!correct) {
			for (String option : nextPages) {
				if (ans.equals(option)) {
					correct = true;
				}
			}
			
			if (!correct) {
				System.out.print("Please enter a valid page number: ");
				ans = reader.nextLine();
			}
		}
		
		reader.close();
		
		return ans;
	}
	
	public void changeStat(String stat, int num) {
		switch (stat) {
		case "skill":
			skill += num;
			break;
		case "stamina":
			stamina += num;
			break;
		case "luck":
			luck += num;
			break;
		}
			
	}
	
	// Pick up the item and see if it buffs/debuff player stats before adding it to backpack
	public void pickUpItems(List<Item> itemList) {
		for (Item item : itemList) {
			
			Map<String, Integer> itemAttribute = item.getAttribute();
			
			// Check if the item attribute map is empty
			if(itemAttribute.isEmpty()) {
				// Get the key from the map
				String key = (String) itemAttribute.keySet().toArray()[0];
				// Get the value
				int value = itemAttribute.get(key);
				
				// Change the players stats
				changeStat(key, value);
			}
			
			backpack.add(item);
		}
	}
	
}
