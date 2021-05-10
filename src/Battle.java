import java.util.List;
import java.util.Scanner;

/*
 * This is the battle class, where everything to do with battles is defined
 */
public class Battle {
	
	private void pause() {
		System.out.print("Press enter key to continue...");
		Scanner reader = new Scanner(System.in);
		reader.nextLine();
	}
	
	// Fighting a single enemy or multiple enemies in turn 
	public boolean fightSingle(Player player, List<Enemy> enemies, String battleType) {
		boolean victory = false;
		
		// For each enemy call the default battle method to handle the battle
		for (Enemy enemy : enemies) {
			victory = battleDefault(player, enemy);
		}
		return victory;
	}
	
	// Fight single/multiple enemies in turn with the use of turns or consecutive wins
	public boolean fightSingle(Player player, List<Enemy> enemies, String battleType, int turns) {
		boolean victory = false;
		
		if (battleType.equals("turns")) {
			for (Enemy enemy : enemies) {
				victory = battleTurns(player, enemy, turns);
			}
			
		} else if (battleType.equals("wins")) {
			
			for (Enemy enemy : enemies) {
				victory = battleWins(player, enemy, turns);
			}
		}
		
		return victory;
	}
	
	// If the page requests that the user fight multiple enemies as one
	public boolean fightTogether(Player player, List<Enemy> enemies, String battleType) {
		boolean victory = false;
		int enemySkill = 0;
		int enemyStamina = 0;
		
		if (battleType.equals("default")) {
			
			// Create a super enemy using the stats fed in by the enemies list
			for (Enemy enemy : enemies) {
				enemySkill += enemy.getSkill();
				enemyStamina += enemy.getStamina();
			}
			
			Enemy newEnemy = new Enemy("", enemySkill, enemyStamina);
			
			// Commence the battle
			victory = battleDefault(player, newEnemy);
		}
		return victory;
	}
	
	// If the page requests that the user fight multiple enemies as one
	public boolean fightTogether(Player player, List<Enemy> enemies, String battleType, int turns) {
		boolean victory = false;
		int enemySkill = 0;
		int enemyStamina = 0;
		
		// Create a super enemy using the stats fed in by the enemies list
		for (Enemy enemy : enemies) {
			enemySkill += enemy.getSkill();
			enemyStamina += enemy.getStamina();
		}
		Enemy newEnemy = new Enemy("", enemySkill, enemyStamina);
		
		if (battleType.equals("turns")) {
			// Battle turns
			victory = battleTurns(player, newEnemy, turns);
			
		} else if (battleType.equals("wins")) {
			// Battle consecutive wins
			victory = battleWins(player, newEnemy, turns);
		}
		
		return victory;
	}
	
	public boolean battleDefault(Player player, Enemy enemy) {
		
		Scanner reader = new Scanner(System.in);
		boolean victory = true;
		
		while (player.isAlive() && enemy.isAlive()) {
			
			int enemySkill = enemy.getSkill() + enemy.rollDice() + enemy.rollDice();
			int playerSkill = player.getSkill() + player.rollDice() + player.rollDice();
			
			System.out.printf("\n\n\t\t[Player skill: %s]\t[Player stamina: %s]\n", playerSkill, player.getStamina());
			System.out.printf("\t\t[Enemy skill: %s]\t[Enemy stamina: %s]\n\n", enemySkill, enemy.getStamina());
			
			pause();
			
			if (playerSkill > enemySkill) {
				
				System.out.println("You have won the round!!");
				System.out.print("Use luck to do more damage: y/n ");
				String ans = reader.nextLine();
				
				if (ans.equals("y")) {
					if (player.testLuck()) {
						System.out.println("Lucky, you inflict 2 extra damage!");
						enemy.takeDamage(4);
					}else {
						System.out.println("Unluck, you only inflict 1 damage");
						enemy.takeDamage(1);
					}
				}else {
					enemy.takeDamage(2);
				}
			}else if (playerSkill < enemySkill){
				
				System.out.println("You have lost the round!!");
				System.out.print("Use luck to take less damage: y/n ");
				String ans = reader.nextLine();
				
				if (ans.equals("y")) {
					if (player.testLuck()) {
						System.out.println("Lucky, you only recieve 1 damage");
						player.takeDamage(1);
					}else {
						System.out.println("Unlucky, you recieve 4 damage");
						player.takeDamage(4);
					}
				}else {
					player.takeDamage(2);
				}
			} else {
				System.out.println("You have drawn this round of combat!!");
			}
			
			System.out.println("\n\n\n");
		}
		return victory;
	}
	

	// This version takes in the turn @param as a constraint of the fight
	public boolean battleTurns(Player player, Enemy enemy, int turns) {
		
		Scanner reader = new Scanner(System.in);
		int battleTurns = 0;
		
		boolean victory = true;
		
		while (player.isAlive() && enemy.isAlive() && battleTurns < turns) {
			
			int enemySkill = enemy.getSkill() + enemy.rollDice() + enemy.rollDice();
			int playerSkill = player.getSkill() + player.rollDice() + player.rollDice();
			
			System.out.printf("\n\n\t\t[Player skill: %s]\t[Player stamina: %s]\n", playerSkill, player.getStamina());
			System.out.printf("\t\t[Enemy skill: %s]\t[Enemy stamina: %s]\n\n", enemySkill, enemy.getStamina());
			
			pause();
			
			if (playerSkill > enemySkill) {
				System.out.println("You have won the round!!");
				System.out.print("Use luck to do more damage: y/n ");
				String ans = reader.nextLine();
				
				if (ans.equals("y")) {
					if (player.testLuck()) {
						System.out.println("Lucky, you inflict 2 extra damage!");
						enemy.takeDamage(4);
					}else {
						System.out.println("Unluck, you only inflict 1 damage");
						enemy.takeDamage(1);
					}
				}else {
					enemy.takeDamage(2);
				}
			}else if (playerSkill < enemySkill){
				System.out.println("You have lost the round!!");
				System.out.print("Use luck to take less damage: y/n ");
				String ans = reader.nextLine();
				
				if (ans.equals("y")) {
					if (player.testLuck()) {
						System.out.println("Lucky, you only recieve 1 damage");
						player.takeDamage(1);
					}else {
						System.out.println("Unlucky, you recieve 4 damage");
						player.takeDamage(4);
					}
				}else {
					player.takeDamage(2);
				}
			} else {
				System.out.println("You have drawn this round of combat!!");
			}
			
			battleTurns++;
		}
		return victory;
	}
	
	// This version takes in the turn @param as a constraint of the fight
	public boolean battleWins(Player player, Enemy enemy, int wins) {
		
		Scanner reader = new Scanner(System.in);
		int playerWins = 0;
		int enemyWins = 0;
		boolean victory = false;
		
		while (player.isAlive() && enemy.isAlive() && playerWins > wins && playerWins <= wins) {
			
			int enemySkill = enemy.getSkill() + enemy.rollDice() + enemy.rollDice();
			int playerSkill = player.getSkill() + player.rollDice() + player.rollDice();
			
			System.out.printf("\n\n\t\t[Player skill: %s]\t[Player stamina: %s]\n", playerSkill, player.getStamina());
			System.out.printf("\t\t[Enemy skill: %s]\t[Enemy stamina: %s]\n\n", enemySkill, enemy.getStamina());
			
			pause();
			
			if (playerSkill > enemySkill) {
				playerWins++;
				System.out.println("You have won the round!!");
				
				System.out.print("Use luck to do more damage: y/n ");
				String ans = reader.nextLine();
				
				if (ans.equals("y")) {
					if (player.testLuck()) {
						System.out.println("Lucky, you inflict 2 extra damage!");
						enemy.takeDamage(4);
					}else {
						System.out.println("Unluck, you only inflict 1 damage");
						enemy.takeDamage(1);
					}
				}else {
					enemy.takeDamage(2);
				}
			}else if (playerSkill < enemySkill){
				System.out.println("You have lost the round!!");
				System.out.print("Use luck to take less damage: y/n ");
				String ans = reader.nextLine();
				enemyWins++;
				
				if (ans.equals("y")) {
					if (player.testLuck()) {
						System.out.println("Lucky, you only recieve 1 damage");
						player.takeDamage(1);
					}else {
						System.out.println("Unlucky, you recieve 4 damage");
						player.takeDamage(4);
					}
				}else {
					player.takeDamage(2);
				}
			} else {
				System.out.println("You have drawn this round of combat!!");
			}
			
		}
		
		if (playerWins == 2) {
			victory = true;
		}
		
		return victory;
	}
	
}
