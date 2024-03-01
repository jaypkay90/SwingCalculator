import java.awt.Font;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class HandleEvents {
	private JButton[] numberBtns;
	private JButton[] operationBtns;
	private JTextField inputField;
	
	// Konstruktor
	public HandleEvents(JButton[] numberBtns, JButton[] operationBtns, JTextField inputField) {
		this.numberBtns = numberBtns;
		this.operationBtns = operationBtns;
		this.inputField = inputField;
	}
	
	private String firstNum = "0", currentNum; // currentNum ist die zuletzt eingegebene Nummer
	private String operation = "", lastOperationCommand;
	private boolean operationCommand = false;
	private boolean equalsCommand = false;
	private boolean easterEggFound = false;
	private String[] calcOperations = {"*", "+", "-", "/"}; // Array ist bereits sortiert
	
	public void handleClick(String command) {
		// Wenn im Display ERROR steht, kann nur noch die "C"-Taste gedrückt werden
		if (inputField.getText().equals("ERROR") && !command.equals("C")) {
			return;
		}
		
		// Funktion des Plus-Minus-Switch-Button implementieren
		if (command.equals("+/-")) {			
			handlePlusMinusCommands();
		}
		
		// Der Command von allen Tasten, abgesehen von der Plus-Minus-Taste hat eine Stringlänge von 1. Daher müssen wir die Charlänge hier nicht abfragen		
		// Wenn Taste mit Nummer gedrückt wurde...
		else if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
			handleNumberCommands(command);
		}
		
		// Komma-Taste implementieren
		else if (command.equals(",")) {
			handleCommaCommands();
		}
		
		// "C"-Button --> Alles zurücksetzen
		else if (command.equals("C")) {
			resetCalculator();
		}
		
		// Wenn Taste eine Rechenoperation ist...
		else if (Arrays.binarySearch(calcOperations, command) >= 0) {
			handleCalcOperationCommands(command);
		}
		
		// Wenn "=" Taste gedrückt wurde
		else if (command.equals("=")) {
			calculate(command);
		}
		
		// Easter Egg
		if (!easterEggFound && inputField.getText().equals("13091985")) {
			// Easter Egg anzeigen und als gefunden markieren
			new EasterEgg();
			easterEggFound = true;
		}
	}
	

	private void handleCalcOperationCommands(String command) {
		// Es darf wieder eine Berechnung durchgeführt werden
		if (equalsCommand) {
			equalsCommand = false;
		}
		
		// Wenn zuvor bereits ein Rechenoperationsbutton gedrückt wurde: Speichere die aktuelle Operation für die nächste Berechenung ab und return
		// Beispiel: 6 + 8 * - 4 --> Die Berechnung 6 + 8 = 14 wird in dem Moment durchgeführt, wo der User * drückt.
		// Da wir danach - drücken, ist die Multiplikation im nächsten Schritt aber nicht gewünscht. Daher ist das - als Operation abzuspeichern.
		// Sobald der User die 4 eingibt und danach = oder einen anderen Rechenoperationsbutton drückt, wird die Rechnung 14 - 4 = 10 ausgeführt
		if (operationCommand) {
			operation = command;
			return;
		}
		
		// lastOperationCommand: Der letzte Rechenoperationsbutton wird abgespeichert --> 6 + 4 - --> Sobald - gedrückt wird, ist lastOperationCommand +
		lastOperationCommand = operation;
		operation = command;				
		operationCommand = true;
		
		// Wenn es einen vorherigen Rechencomand gibt --> führe die Berechnung aus --> 6 + 4 - --> Sobald - gedrückt wird, wird 6 + 4 = 10 berechnet
		if (lastOperationCommand != "") {
			calculate(command);
		}
		
		// Wenn es keinen vorherigen Rechencommand gibt --> 6 + --> Sobald + gedrückt wird --> Speichere 6 als die erste Zahl zur Berechnung ab
		else {
			firstNum = inputField.getText();
			if (firstNum.endsWith(",")) {
				firstNum = firstNum.substring(0, firstNum.length() - 2);
			}				
		}
	}
	
	
	private void resetCalculator() {
		firstNum = "0";
		currentNum = "0";
		inputField.setText("0");
		operation = "";
		equalsCommand = false;
		operationCommand = false;
	}
	
	
	private void handleCommaCommands() {
		// Wenn zuvor eine Berechnung durchgeführt wurde --> schreibe 0. ins Display --> danach dürfen wieder Berechnungen durchgeführt werden
		if (equalsCommand) {
			inputField.setText("0.");
			equalsCommand = false;
		}
		
		// Wenn zuvor ein Rechenoperations-Button gedrückt wurde --> schreibe 0. ins Display --> danach dürfen wieder Rechenoperations-Button gedrückt werden
		else if (operationCommand) {
			inputField.setText("0.");
			operationCommand = false;
		}
		
		// Aktuelle Zahl aus dem Textfeld auslesen
		String input = inputField.getText();
		
		// Wenn die Zahl bereits ein Komma enthält, darf kein zweites Komma eingegeben werden
		if(input.contains(".")) {
			return;
		}
		
		// Wenn noch kein Komma in der Zahl --> Komma zur Zahl hinzufügen und im Display anzeigen
		inputField.setText(input.concat("."));
	}
	
	
	private void handleNumberCommands(String command) {
		// Wenn vor dem Drücken der Zahl eine Berechnung durchgeführt wurde, darf nach dem Drücken der Zahl wieder eine Berechnung durchgeführt werden
		if (equalsCommand) {
			inputField.setText("0");
			equalsCommand = false;
		}
		
		// Wenn nach einem Rechenoperations-Button eine Zahl gedrückt wird, dürfen wieder Rechenoperationen durchgeführt werden
		if (operationCommand) {
			inputField.setText("0");
			operationCommand = false;
		}
		
		// Textfeld auslesen
		String userInput = inputField.getText();
		
		// Fall 1: Eine neue Zahl wird eingeben (im Display steht eine 0 oder eine -0)
		if (userInput.equals("0")) {
			// Neue Zahl im Display anzeigen
			inputField.setText(command);
		}
		else if(userInput.equals("-0")) {
			inputField.setText(String.format("-%s", command));
		}
		
		// Fall 2: Eine bestehende Zahl wird durch eine Ziffer erweitert
		else {
			inputField.setText(userInput.concat(command));
		}
	}
	
	
	private void handlePlusMinusCommands() {
		// Wenn Zahl im Display nicht negativ --> setze ein Minus davor
		if (inputField.getText().charAt(0) != '-') {
			inputField.setText(String.format("-%s", inputField.getText()));
		}
		
		// Wenn Zahl im Display negativ --> entferne das Minuszeichen am Anfang
		else {
			inputField.setText(inputField.getText().substring(1));
		}
	}
	
	
	private void calculate(String command) {
		if (command.equals("=")) {
			// Wenn zuvor ein Rechenoperationsbutton gedrückt wurde --> 6 + = --> Setze die operation auf "" und return
			// Das löst folgenden Bug: 5 * = 2 = --> Sobald das erste = gedrückt wird, wird operation = ""
			// Wenn das zweite = gedrückt wird, findet keine Berechnung statt, weil wir im Switch im default case landen, der die aktuelle Zahl (hier 2) returned.  
			if (operationCommand) {
				operation = "";
				return;
			}
			
			// Wenn zuvor bereits eine Berechnung durchgeführt wurde (dazwischen wurde keine neue Zahl eingegeben) return
			if (equalsCommand) {
				return;
			}
		}
		
		// Wenn wir hier landen, wird der Rest des Codes ausgeführt
		equalsCommand = true;
		currentNum = inputField.getText();
		System.out.println(firstNum + " " + operation + " " + currentNum);
		
		// Speichere die beiden Zahlen für die Berechnung als double
		double num1 = Double.parseDouble(firstNum);
		double num2 = Double.parseDouble(currentNum);
		//double erg = 0;
		
		// Wenn ein = diese Methode aufgerufen hat, muss die "akutellste" Berechnung durchgeführt werden
		// 7 + 3 = --> Sobald = gedrückt wird, wird 7 + 3 berechnet
		String operationToUse;
		if (command.equals("=")) {
			operationToUse = operation;
		}
		
		// Wenn eine Rechenoperationstaste diese Methode aufgerufen hat, muss jetzt die Berechnung aus dem letzten Schritt durchgeführt werden
		// Bspl: 6 + 2 * --> Sobald * gedrückt wird, wird 6 + 2 berechnet
		else {
			operationToUse = lastOperationCommand;
		}
		
		// Ergebnis berechnen
		double erg = calcResult(num1, num2, operationToUse, command);
		
		/*switch (operationToUse) {
		case "/":
			// Teilen durch 0 ist verboten
			if (num2 == 0) {
				inputField.setText("ERROR");
				return;
			}
			erg = num1 / num2;					
			break;
		case "*":
			erg = num1 * num2;
			break;
		case "+":
			erg = num1 + num2;
			break;
		case "-":
			erg = num1 - num2;
			break;
		default:
			if (command.equals("=")) {
				// Wenn vor dem Drücken von = kein Rechenoperationsbutton gedrückt wurde, soll nichts berechnet werden --> die aktuelle Zahl wird zurückgegeben
				erg = num2;
			}
			else {
				return;				
			}
		}*/
		
		// Wenn die Berechnung zu einem Error geführt hat.. return
		if (inputField.getText().equals("ERROR")) {
			return;
		}
		
		currentNum = "0";
		
		if (command.equals("=")) {
			// Die letzte Rechenoperation löschen, denn nach = soll eine komplett neue Berechnung starten
			operation = "";
		}
		else {
			// Wenn ein Rechenoperationsbutton diese Methode aufgerufen hat: Ergebnis der aktuellen Berechnung als erste Zahl für die nächste Berechnung setzen
			firstNum = String.valueOf(erg);			
		}
		
		// Wenn das Ergebnis ein int ist --> im Display als Ganzzahl anzeigen, sonst als double anzeigen
		if (erg % 1 == 0) {
			inputField.setText(String.valueOf((int)erg));
		}
		else {
			inputField.setText(firstNum);
			inputField.setText(String.valueOf(erg));
		}
		
	}
	
	private double calcResult(double num1, double num2, String operator, String command) {
		double erg = 0;
		
		switch (operator) {
		case "/":
			// Teilen durch 0 ist verboten
			if (num2 == 0) {
				inputField.setText("ERROR");
				break;
			}
			erg = num1 / num2;					
			break;
		case "*":
			erg = num1 * num2;
			break;
		case "+":
			erg = num1 + num2;
			break;
		case "-":
			erg = num1 - num2;
			break;
		default:
			if (command.equals("=")) {
				// Wenn vor dem Drücken von = kein Rechenoperationsbutton gedrückt wurde, soll nichts berechnet werden --> die aktuelle Zahl wird zurückgegeben
				erg = num2;
			}
			/*else {
				return;				
			}*/
		}
		
		return erg;
	}
	
	
	public void handleKeyPress(String pressedKey) {
		// Die gedrückte Taste im numberBtns Array suchen, wenn gefunden klicken und return
		for (JButton button : numberBtns) {
			if (button.getActionCommand().equals(pressedKey)) {
				button.doClick();
				return;
			}
		}
		
		// Die gedrückte Taste im operationBtns Array suchen, wenn gefunden klicken und return
		for (JButton button : operationBtns) {
			if (button.getActionCommand().equalsIgnoreCase(pressedKey)) {
				button.doClick();
				return;
			}
		}
		
		// Wenn die gedrückte Taste bis hier nicht gefunden wurde, ist kein Button, der dieser Taste entspricht, auf dem Display --> return
		return;
	}
	
	
}
