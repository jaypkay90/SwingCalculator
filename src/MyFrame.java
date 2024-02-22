import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
	
	private String firstNum;
	private String operation = "";
	private String command;
	private String currentNum;
	private boolean operationCommand = false;
	private boolean equalsCommand = false;
	private boolean firstNumSet = false;
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// ActionCommand bekommen
		command = e.getActionCommand();
		//currentNum = inputField.getText();
		
		// Wenn im Display ERROR steht, kann nur die "C"-Taste gedrückt werden
		if (inputField.getText().equals("ERROR") && !command.equals("C")) {
			return;
		}
		
		
		if (equalsCommand) {
			if (command.equals("=")) {
				return;				
			}
			else {
				equalsCommand = false;
			}
		}
		
		// Funktion des Plus-Minus-Switch-Button implementieren
		if (command.equals("+/-")) {
			// Wenn Zahl im Display nicht negativ --> setze ein Minus davor
			if (inputField.getText().charAt(0) != '-') {
				currentNum = String.format("-%s", inputField.getText());
				inputField.setText(currentNum);
			}
			
			// Wenn Zahl im Display negativ --> entferne das Minuszeichen am Anfang
			else {
				currentNum = inputField.getText().substring(1);
				inputField.setText(currentNum);
			}
		}
		
		// Der Command von allen Tasten, abgesehen von der plusMinus-Taste hat eine Stringlänge von 1.		
		// Wenn Taste mit Nummer gedrückt wurde...
		else if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
			// Eine neue Zahl wird eingeben (im Display steht eine 0)
			if (operationCommand) {
				inputField.setText("0");
				operationCommand = false;
			}
			if (inputField.getText().equals("0")) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! || operationCommand == false
				// Es darf wieder ein Rechenoperationszeichen ausgewählt werden
				//operationCommand = false;
		
				// Neue Zahl "anlegen"
				currentNum = command;
				inputField.setText(currentNum);
			}
			
			// Eine bestehende Zahl wird durch eine Ziffer erweitert
			else {
				currentNum = inputField.getText().concat(command);
				inputField.setText(currentNum);
			}
		}
		
		// Komma-Taste implementieren
		else if (command.equals(",")) {
			
			if (operationCommand) {
				inputField.setText("0.");
				operationCommand = false;
			}
			
			// Aktuelle Zahl aus dem Textfeld auslesen
			currentNum = inputField.getText();
			
			// Wenn die Zahl bereits ein Komma enthält, darf kein zweites Komma eingegeben werden
			if(currentNum.contains(".")) {
				return;
			}
			
			// Wenn noch kein Komma in der Zahl --> Komma zur Zahl hinzufügen und im Display anzeigen
			inputField.setText(currentNum.concat("."));
		}
		
		
		// "C"-Button --> Alles zurücksetzen
		else if (command.equals("C")) {
			firstNum = "0";
			currentNum = "0";
			inputField.setText("0");
		}
		
		// Tasten mit Rechenoperationen
		else if (command.equals("/")) {
			operationCommand = true;
			operation = "/";
			firstNum = inputField.getText();
			if (firstNum.endsWith(",")) {
				firstNum = firstNum.substring(0, firstNum.length() - 2);
			}
			//currentNum = "0";
			//inputField.setText(currentNum);			
		}
		
		else if (command.equals("+")) {
			operationCommand = true;
			operation = "+";
			
			// Wenn firstNum noch nicht gesetzt
			if (!firstNumSet) {
				firstNum = inputField.getText();
				firstNumSet = true;
				if (firstNum.endsWith(",")) {
					firstNum = firstNum.substring(0, firstNum.length() - 2);
				}
			}
			else {
				berechne();
				firstNumSet = false;
			}
			
			//currentNum = "0";
			//inputField.setText(currentNum);
		}
		
		
		
		
		
		
		
		
		else if (command.equals("-")) {
			operationCommand = true;
			operation = "-";
			firstNum = inputField.getText();
			if (firstNum.endsWith(",")) {
				firstNum = firstNum.substring(0, firstNum.length() - 2);
			}
			//currentNum = "0";
			//inputField.setText(currentNum);
		}
		
		else if (command.equals("*")) {
			operationCommand = true;
			operation = "*";
			firstNum = inputField.getText();
			if (firstNum.endsWith(",")) {
				firstNum = firstNum.substring(0, firstNum.length() - 2);
			}
			//currentNum = "0";
			//inputField.setText(currentNum);
		}
		
		else if (command.equals("=")) {
			equalsCommand = true;
			System.out.println(firstNum + " " + currentNum);
			 
			double num1 = Double.parseDouble(firstNum);
			double num2 = Double.parseDouble(currentNum);
			double erg = 0;
			
			
			switch (operation) {
			case "/":
				if (currentNum.equalsIgnoreCase("0")) {
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
			
			// Wenn das Ergebnis ein int ist --> im Display als Ganzzahl anzeigen, sonst als double anzeigen
			if (erg % 1 == 0) {
				inputField.setText(String.valueOf((int)erg));
			}
			else {
				inputField.setText(firstNum);				
			}
			
		}
		
	}

	private void berechne() {
		if (operation == "") {
			return;
		}
		//equalsCommand = true;	
		System.out.println(firstNum + " " + currentNum);
		double erg = 0;
		switch (operation) {
		case "/":
			if (currentNum.equalsIgnoreCase("0")) {
				inputField.setText("ERROR");
				return;
			}
			erg = Double.parseDouble(firstNum) / Double.parseDouble(currentNum);					
			break;
		case "*":
			erg = Double.parseDouble(firstNum) * Double.parseDouble(currentNum);
			break;
		case "+":
			erg = Double.parseDouble(firstNum) + Double.parseDouble(currentNum);
			break;
		case "-":
			erg = Double.parseDouble(firstNum) - Double.parseDouble(currentNum);
			break;
		default:
			erg = 0;
		}
		
		if (erg % 1 == 0) {
			inputField.setText(String.valueOf((int)erg));
		}
		else {
			inputField.setText(String.valueOf(erg));				
		}
		firstNum = String.valueOf(erg);
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
		
		if (pressedKey.equals(".")) {
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
		
		/*if (commaBtn.getActionCommand().equals(pressedKey)) {
			commaBtn.doClick();
			return;
		}
		
		if (plusMinusBtn.getActionCommand().equals(pressedKey)) {
			plusMinusBtn.doClick();
			return;
		}*/
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
};

	
}
