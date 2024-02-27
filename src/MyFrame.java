import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
// Test
public class MyFrame extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	private JButton[] numberBtns, operationBtns;
	private JTextField inputField;
	
	MyFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(300,400));
		this.setResizable(false);
		this.add(createInputPanel(), BorderLayout.NORTH);
		this.add(createOperationPanel(), BorderLayout.EAST);
		this.add(createNumberPanel(), BorderLayout.CENTER);
		
		// Taschenrechner-Bild als Icon hinzufügen
		ImageIcon icon = new ImageIcon("calculator.png");
		this.setIconImage(icon.getImage());
		
		this.pack(); // Adjust the frame size, so that all components fit in
		this.setVisible(true);
	}
	
	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setPreferredSize(new Dimension(100,100));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		inputField = new JTextField();
		inputField.setEditable(false);
		inputField.setFocusable(true);
		inputField.addKeyListener(this);
		inputField.setText("0");
		inputField.setHorizontalAlignment(JTextField.RIGHT);
		
		inputPanel.add(inputField, BorderLayout.CENTER);
		return inputPanel;
	}
	
	private JPanel createNumberPanel() {
		JPanel numberPanel = new JPanel();
		numberPanel.setLayout(new GridLayout(4,3,5,5));
		numberPanel.setPreferredSize(new Dimension(300,400));
		numberPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,0));
		addNumberBtns(numberPanel);
		
		return numberPanel;
	}
	
	private void addNumberBtns(JPanel numberPanel) {
		numberBtns = new JButton[12];

		for (int i = numberBtns.length - 3; i >= 0 ; i--) {
			numberBtns[i] = new JButton();
			if (i == 0) {
				numberBtns[i].setText(String.format("%d", i));
			}
			else if (i % 3 == 0) {
				numberBtns[i].setText(String.format("%d", i - 2));				
			}
			else if (i % 3 == 2) {
				numberBtns[i].setText(String.format("%d", i));
			}
			else {
				numberBtns[i].setText(String.format("%d", i + 2));
			}
			
			numberBtns[i].addActionListener(this);
			numberBtns[i].setFocusable(false);
			numberPanel.add(numberBtns[i]);
		}
		
		// Komma-Button und Plus-Minus-Switch-Button hinzufügen
		numberBtns[10] = new JButton(",");
		numberBtns[11] = new JButton("+/-");
		
		for (int i = 10; i < 12; i++) {
			numberBtns[i].addActionListener(this);
			numberBtns[i].setFocusable(false);
			numberPanel.add(numberBtns[i]);
		}
		
	}
	
	private JPanel createOperationPanel() {
		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new GridLayout(6,1,10,5));
		operationPanel.setPreferredSize(new Dimension(100, 400));
		operationPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		addOperationBtns(operationPanel);
		
		return operationPanel;
	}
	
	private void addOperationBtns(JPanel operationPanel) {
		operationBtns = new JButton[6];
		for (int i = 0; i < operationBtns.length; i++) {
			operationBtns[i] = new JButton();
			operationBtns[i].addActionListener(this);
			operationBtns[i].setFocusable(false);
		}
		
		operationBtns[0].setText("C");
		operationBtns[1].setText("/");
		operationBtns[2].setText("*");
		operationBtns[3].setText("-");
		operationBtns[4].setText("+");
		operationBtns[5].setText("=");
		
		for (int i = 0; i < operationBtns.length; i++) {
			operationPanel.add(operationBtns[i]);
		}
	}
	
	private String firstNum = "0", currentNum;
	private String operation = "", lastOperationCommand;
	private String command;
	private boolean operationCommand = false;
	private boolean equalsCommand = false;
	private String[] calcOperations = {"*", "+", "-", "/"};
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// ActionCommand bekommen
		command = e.getActionCommand();
		
		// Wenn im Display ERROR steht, kann nur noch die "C"-Taste gedrückt werden
		if (inputField.getText().equals("ERROR") && !command.equals("C")) {
			return;
		}
		
		// Funktion des Plus-Minus-Switch-Button implementieren
		if (command.equals("+/-")) {
			// Wenn Zahl im Display nicht negativ --> setze ein Minus davor
			if (inputField.getText().charAt(0) != '-') {
				inputField.setText(String.format("-%s", inputField.getText()));
			}
			
			// Wenn Zahl im Display negativ --> entferne das Minuszeichen am Anfang
			else {
				inputField.setText(inputField.getText().substring(1));
			}
		}
		
		// Der Command von allen Tasten, abgesehen von der Plus-Minus-Taste hat eine Stringlänge von 1. Daher müssen wir die Charlänge hier nicht abfragen		
		// Wenn Taste mit Nummer gedrückt wurde...
		else if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
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
			
			// Fall 1: Eine neue Zahl wird eingeben (im Display steht eine 0)
			if (userInput.equals("0")) {
				// Neue Zahl im Display anzeigen
				inputField.setText(command);
			}
			
			// Fall 2: Eine bestehende Zahl wird durch eine Ziffer erweitert
			else {
				inputField.setText(userInput.concat(command));
			}
		}
		
		// Komma-Taste implementieren
		else if (command.equals(",")) {
			// Wenn zuvor eine Berechnung durchgeführt wurde --> schreibe 0. ins Display
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
		
		
		// "C"-Button --> Alles zurücksetzen
		else if (command.equals("C")) {
			firstNum = "0";
			currentNum = "0";
			inputField.setText("0");
			operation = "";
			equalsCommand = false;
			operationCommand = false;
		}
		
		// Wenn Taste eine Rechenoperation ist...
		else if (Arrays.binarySearch(calcOperations, command) >= 0) {
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
				berechne();
			}
			
			// Wenn es keinen vorherigen Rechencommand gibt --> 6 + --> Sobald + gedrückt wird --> Speichere 6 als die erste Zahl zur Berechnung ab
			else {
				firstNum = inputField.getText();
				if (firstNum.endsWith(",")) {
					firstNum = firstNum.substring(0, firstNum.length() - 2);
				}				
			}
		}
		
		// Wenn "=" Taste gedrückt wurde
		else if (command.equals("=")) {
			berechne();
		}
			// Wenn zuvor ein Rechenoperationsbutton gedrückt wurde --> 6 + = --> Setze die operation auf "" und return
			// Das löst folgenden Bug: 5 * = 2 = --> Sobald das erste = gedrückt wird, wird operation = ""
			// Wenn das zweite = gedrückt wird, findet keine Berechnung statt, weil wir im Switch im default case landen, der die aktuelle Zahl (hier 2) returned.  
			/*if (operationCommand) {
				operation = "";
				return;
			}
			
			// Wenn zuvor bereits eine Berechnung durchgeführt wurde (dazwischen wurde keine neue Zahl eingegeben) return
			if (equalsCommand) {
				return;
			}
				
			// Wenn wir hier landen, wird der Rest des Codes ausgeführt
			equalsCommand = true;
			currentNum = inputField.getText();
			System.out.println(firstNum + " " + operation + " " + currentNum);
			
			// Speichere die beiden Zahlen für die Berechnung als double
			double num1 = Double.parseDouble(firstNum);
			double num2 = Double.parseDouble(currentNum);
			System.out.println(num1 + " " + num2);
			double erg = 0;
			
			// Führe die Berechnung aus
			switch (operation) {
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
			// Wenn kein Rechenoperationsbutton gedrückt wurde --> gib die aktuelle Zahl zurück
			default:
				erg = num2;
			}
			
			// currentNum zurück auf 0 setzen
			// operation leeren, damit wir eine komplett neue Berechnung starten können
			currentNum = "0";
			operation = "";
			
			// Wenn das Ergebnis ein int ist --> im Display als Ganzzahl anzeigen, sonst als double anzeigen
			if (erg % 1 == 0) {
				inputField.setText(String.valueOf((int)erg));
			}
			else {
				inputField.setText(firstNum);
				inputField.setText(String.valueOf(erg));
			}
			
		}*/
	}
	
		
	/*private void berechne() {
		equalsCommand = true;
		currentNum = inputField.getText();
		System.out.println(firstNum + " " + currentNum);
		 
		double num1 = Double.parseDouble(firstNum);
		double num2 = Double.parseDouble(currentNum);
		double erg = 0;
		
		
		switch (lastOperationCommand) {
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
			return;
		}
		
		// Ergebnis der aktuellen Berechnung als erste Zahl für die nächste Berechnung setzen
		firstNum = String.valueOf(erg);
		currentNum = "0";
		
		// Wenn das Ergebnis ein int ist --> im Display als Ganzzahl anzeigen, sonst als double anzeigen
		if (erg % 1 == 0) {
			inputField.setText(String.valueOf((int)erg));
		}
		else {
			inputField.setText(firstNum);
			inputField.setText(String.valueOf(erg));
		}
		
	}*/
	
	private void berechne() {
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
		double erg = 0;
		
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
		
		switch (operationToUse) {
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

	
	//KeyListener implementieren	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Gedrückte Taste abspeichern
		String pressedKey = String.valueOf(e.getKeyChar());
		
		// Enter-Key fungiert wie =, . fungiert wie Komma
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			pressedKey = "=";
		}
		else if (pressedKey.equals(".")) {
			pressedKey = ",";
		}
		
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
