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
public class MyFrame extends JFrame implements ActionListener {

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
		inputField.addKeyListener(listener);
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
	
	private String firstNum = "0";
	private String operation = "";
	private String command;
	private String currentNum;
	private boolean operationCommand = false;
	private boolean equalsCommand = false;
	private boolean firstNumSet = false;
	private String[] calcOperations = {"*", "+", "-", "/"};
	private String currentOperationCommand = "", lastOperationCommand;
	
	
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
			}
			
			// Wenn zuvor ein Rechenoperations-Button gedrückt wurde --> schreibe 0. ins Display --> danach dürfen wieder Rechenoperations-Button gedrückt werden
			else if (operationCommand) {
				inputField.setText("0.");
				operationCommand = false;
			}
			
			// Aktuelle Zahl aus dem Textfeld auslesen
			//currentNum = inputField.getText();
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
			if (equalsCommand) {
				equalsCommand = false;
			}
			
			if (operationCommand) {
				operation = command;
				return;
			}
			
			lastOperationCommand = operation;
			operation = command;				
			operationCommand = true;
			if (lastOperationCommand != "") {
				berechne();
			}
			else {
				firstNum = inputField.getText();
				if (firstNum.endsWith(",")) {
					firstNum = firstNum.substring(0, firstNum.length() - 2);
				}				
			}
		}
		
		// Wenn "=" Taste gedrückt wurde
		else if (command.equals("=")) {
			if (operationCommand) {
				operation = "";
			}
			if (equalsCommand || operationCommand) {
				return;
			}
				
				
			equalsCommand = true;
			currentNum = inputField.getText();
			System.out.println(firstNum + " " + currentNum);
			 
			double num1 = Double.parseDouble(firstNum);
			double num2 = Double.parseDouble(currentNum);
			System.out.println(num1 + " " + num2);
			double erg = 0;
			
			
			switch (operation) {
			case "/":
				if (currentNum.equals("0") || currentNum.equals("0.")) {
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
				erg = num2;
				//return;
			}
			
			// Ergebnis der aktuellen Berechnung als erste Zahl für die nächste Berechnung setzen
			//firstNum = String.valueOf(erg);
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
			
		}
}
	
		
	private void berechne() {
		equalsCommand = true;
		currentNum = inputField.getText();
		System.out.println(firstNum + " " + currentNum);
		 
		double num1 = Double.parseDouble(firstNum);
		double num2 = Double.parseDouble(currentNum);
		double erg = 0;
		
		
		switch (lastOperationCommand) {
		case "/":
			if (currentNum.equals("0") || currentNum.equals("0.")) {
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
		
	}

	
KeyListener listener = new KeyListener() {	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		String pressedKey = String.valueOf(e.getKeyChar());
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			pressedKey = "=";
		}
		else if (pressedKey.equals(".")) {
			pressedKey = ",";
		}
		
		for (JButton button : numberBtns) {
			if (button.getActionCommand().equals(pressedKey)) {
				button.doClick();
				return;
			}
		}
		
		for (JButton button : operationBtns) {
			if (button.getActionCommand().equalsIgnoreCase(pressedKey)) {
				button.doClick();
				return;
			}
		}
		
		return;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
};

	
}
