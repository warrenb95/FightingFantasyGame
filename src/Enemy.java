import java.util.concurrent.ThreadLocalRandom;

public class Enemy {
	
	private int skill;
	private int stamina;
	
	Enemy(int enemySkill, int enemyStamina){
		skill = enemySkill;
		stamina = enemyStamina;
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
}
