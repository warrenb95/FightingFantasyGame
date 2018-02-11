import java.util.HashMap;

public class Game {
	
	public void newGame() {
		Player player = new Player();
		
		HashMap<String, Enemy> enemyHash = new HashMap<String, Enemy>();
		
		enemyHash.put("galthazzeth", new Enemy("galthazzeth", 10, 15));
		
		System.out.println(enemyHash);
		
		Page book = new Page();
		
		String nextPage = "test";
		
			while(player.isAlive())
			{	
				
				System.out.println("Stamina before: "+ player.getStamina());
				
				// Read the prologue page
				nextPage = book.readPage(nextPage, player, enemyHash);
				// ---------------------
				
				System.out.println("Stamina after: "+ player.getStamina());
				
				break;
			}
	}
	
}
