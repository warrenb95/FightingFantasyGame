import java.util.concurrent.ThreadLocalRandom;

public class Enemy {
	
	private String name;
	private int skill;
	private int stamina;
	
	Enemy(String enemyName, int enemySkill, int enemyStamina){
		name = enemyName;
		skill = enemySkill;
		stamina = enemyStamina;
	}
	
	public int rollDice() {
		return ThreadLocalRandom.current().nextInt(1, 6 + 1);
	}
	
	public int attack() {
		return skill + rollDice() + rollDice();
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
}
