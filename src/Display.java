import java.util.List;

public class Display {
	
	public void display(String text) {
		System.out.println("\n\n\t\t##########################################");	
		System.out.print(text);
		System.out.println("\n\t\t##########################################");	
	}
	
	public void display(String text, List<String> options) {
		System.out.println("\n\n\t\t##########################################");	
		System.out.print("\t\t"+text);
		for (String word : options) {
			System.out.print(word + " ");
		}
		System.out.println("\n\t\t##########################################");	
	}
}
