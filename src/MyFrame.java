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
	private String input;
	private boolean operationCommand = false;
	private boolean equalsCommand = false;

	@Override
	public void actionPerformed(ActionEvent e) {
		// Action Command bekommen
		command = e.getActionCommand();
		input = inputField.getText();
		
		// Wenn Error im Display steht, werden alle Tasten außer die "C"-Taste disabled
		if (input.equals("ERROR") && !command.equals("C")) {
			return;
		}
		
		// Wenn einmal gleich gedrückt wurde, darf es danach nicht unmittelbar nochmal gedrückt werden
		if (equalsCommand) {
			if (command.equals("=")) {
				return;				
			}
			else {
				equalsCommand = false;
			}
		}
		
		// Plus-Minus-Switch-Taste konfigurieren
		if (command.equals("+/-")) {
			if (input.charAt(0) != '-') {
				currentNum = String.format("-%s", input);
				inputField.setText(currentNum);
			}
			else {
				currentNum = inputField.getText().substring(1);
				inputField.setText(currentNum);
			}
		}
		
		// Der Command von allen Tasten, abgesehen von der plusMinus-Taste hat eine Stringlänge von 1.		
		// Wenn Taste mit Nummer gedrückt wurde...
		else if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
			// Steht im Display eine 0?
			if (inputField.getText().equals("0") || operationCommand == true) {
				operationCommand = false;
				currentNum = command;
				inputField.setText(currentNum);
			}
			else {
				currentNum = inputField.getText().concat(command);
				inputField.setText(currentNum);
			}
		}
		
		else if (command.equals(",")) {
			if (operationCommand) {
				inputField.setText("0.");
				operationCommand = false;
			}
			
			currentNum = input;
			if(currentNum.contains(".")) {
				return;
			}
			inputField.setText(currentNum.concat("."));
		}
		
		else if (command.equals("C")) {
			firstNum = "0";
			currentNum = "0";
			inputField.setText("0");
		}
		
		// Tasten mit Rechenoperationen
		else if (command.equals("/")) {
			operationCommand = true;
			operation = "/";
			firstNum = input;
			if (firstNum.endsWith(",")) {
				firstNum = firstNum.substring(0, firstNum.length() - 2);
			}
			//currentNum = "0";
			//inputField.setText(currentNum);			
		}
		
		else if (command.equals("+")) {
			operationCommand = true;
			operation = "+";
			firstNum = input;
			if (firstNum.endsWith(",")) {
				firstNum = firstNum.substring(0, firstNum.length() - 2);
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
			firstNum = input;
			if (firstNum.endsWith(",")) {
				firstNum = firstNum.substring(0, firstNum.length() - 2);
			}
			//currentNum = "0";
			//inputField.setText(currentNum);
		}
		
		else if (command.equals("=")) {
			if (operation == "") {
				return;
			}
			equalsCommand = true;	
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
