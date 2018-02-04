import org.jdom2.Element;

public class Game {
	
	public void newGame() {
		Player player = new Player();
		Enemy galthazzeth = new Enemy(10, 15);
		Page book = new Page();
		
		String nextPage = "test";
		
			while(player.isAlive() && galthazzeth.isAlive())
			{	
				
				System.out.println("Stamina before: "+ player.getStamina());
				
				// Read the prologue page
				nextPage = book.readPage(nextPage, player, galthazzeth);
				// ---------------------
				
				System.out.println("Stamina after: "+ player.getStamina());
				
				break;
			}
	}
	
}
