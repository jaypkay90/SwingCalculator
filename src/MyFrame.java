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

public class MyFrame extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	private JButton[] numberBtns, operationBtns;
	private JTextField inputField;
	private HandleEvents handler;
	
	public MyFrame() {
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
		
		handler = new HandleEvents(numberBtns, operationBtns, inputField);
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
	
	
	// AtionListener für Buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		// ActionCommand "bearbeiten"
		handler.handleClick(e.getActionCommand());
		
		return;
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
		
		// Key Press "bearbeiten"
		handler.handleKeyPress(pressedKey);
		
		return;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
