import java.util.HashMap;
import java.util.Map;

public class Item {
	private String name;
	private int goldValue;
	private Map<String, Integer> attribute = new HashMap<String, Integer>();
	
	Item(String itemName, int itmeGoldValue){
		name = itemName;
		goldValue = itmeGoldValue;
	}
	
	Item(String itemName, int itmeGoldValue, Map itemAttribute){
		name = itemName;
		goldValue = itmeGoldValue;
		attribute = itemAttribute;
	}
	
	public Map getAttribute() {
		return attribute;
	}
}
