import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.border.LineBorder;

import java.awt.Font;

public class MyFrame extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	// JButton-Arrays für die Nummerntasten und die Rechenoperationstasten
	private JButton[] numberBtns, operationBtns;
	private JTextField inputField;
	
	// Das Bearbeiten der Klick-Events wird von einer anderen Klasse durchgeführt
	private HandleEvents handler;
	private Font inkTree = new Font("Ink Free", Font.BOLD, 25);
	
	public MyFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(300,400)); // Fenstergröße: 300x400
		this.setResizable(false); // Größe des Frames nicht veränderbar
		
		// Panel für "Display" und Tasten kreiieren und zum Frame hinzufügen
		this.add(createInputPanel(), BorderLayout.NORTH);
		this.add(createOperationPanel(), BorderLayout.EAST);
		this.add(createNumberPanel(), BorderLayout.CENTER);
		
		// Taschenrechner-Bild als Icon hinzufügen
		ImageIcon icon = new ImageIcon("calculator.png");
		this.setIconImage(icon.getImage());
		
		// Frame-Größe so anpassen, dass alle Komponenten hineinpassen und frame sichtbar machen
		this.pack(); 
		this.setVisible(true);
		
		// Instanz der Event-Handler Klasse initialisieren --> In dieser Klasse steht der Code für die logischen Berechnungen drin
		handler = new HandleEvents(numberBtns, operationBtns, inputField, this);
	}
	
	private JPanel createInputPanel() {
		// Input Panel ("Taschenrechner-Display") erstellen
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		
		// Größe: 100x100 --> Die Breite wird hierdurch nicht verändert, weil der Frame BorderLayout hat und das Panel nach NORTH kommt
		inputPanel.setPreferredSize(new Dimension(100,100));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		inputField = new JTextField();
		inputField.setEditable(false);
		
		// Das inputField ist das einzige fokussierbare Objekt in der GUI, so funktioniert der KeyListener am einfachsten
		inputField.setFocusable(true);
		inputField.addKeyListener(this);
		inputField.setBackground(Color.WHITE);
		
		// Abgerundeten Border hinzufügen
		inputField.setBorder(new LineBorder(Color.BLACK, 2, true));
		inputField.setText("0");
		inputField.setFont(inkTree);
		
		// Text im Display rechts anzeigen
		inputField.setHorizontalAlignment(JTextField.RIGHT);
		
		// Das Eingabefeld in der Mitte des Panels platzieren
		inputPanel.add(inputField, BorderLayout.CENTER);
		return inputPanel;
	}
	
	private JPanel createNumberPanel() {
		// Panel mit den Nummerntasten erstellen
		JPanel numberPanel = new JPanel();
		
		// Grid-Layout mit 4 Reihen a 3 Tasten
		numberPanel.setLayout(new GridLayout(4,3,5,5));
		numberPanel.setPreferredSize(new Dimension(300,400));
		
		// Etwas Platz zu den anderen Paneln schaffen
		numberPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,0));
		
		// JButtons (Tasten) hinzufügen
		addNumberBtns(numberPanel);
		
		return numberPanel;
	}
	
	private void addNumberBtns(JPanel numberPanel) {
		// Nummerntasten erstellen --> 10 Zahlen sowie Komma- und Plus-Minus-Switch-Button
		numberBtns = new JButton[12];
		
		// Die Buttons "0" bis "9" erstellen und in der richtigen Reihenfolge im Array speichern
		for (int i = numberBtns.length - 3; i >= 0 ; i--) {
			numberBtns[i] = new JButton();
			
			// Letzte Stelle im Array: JButton mit Text "0"
			if (i == 0) {
				numberBtns[i].setText(String.format("%d", i));
			}
			// Beispiel: Erste Stelle im Array: i = 9 --> 9 % 3 = 0 --> Auf den Button kommt der Text 7
			else if (i % 3 == 0) {
				numberBtns[i].setText(String.format("%d", i - 2));				
			}
			// Beispiel: Zweite Stelle im Array: i = 8 --> 8 % 3 = 2 --> Auf den Button kommt der Text 8
			else if (i % 3 == 2) {
				numberBtns[i].setText(String.format("%d", i));
			}
			// Beispiel: Dritte Stelle im Array: i = 7 --> 8 % 3 = 1 --> Auf den Button kommt der Text 9
			// In der ersten Reihe in der GUI werden also die Zahlen 7, 8, 9 von links nach rechts angezeigt
			else {
				numberBtns[i].setText(String.format("%d", i + 2));
			}
			
			// Aktuellen Button konfigurieren und zum Panel hinzufügen
			configureButton(numberBtns[i]);
			numberPanel.add(numberBtns[i]);
		}
		
		// Komma-Button und Plus-Minus-Switch-Button erstellen, konfigurieren und zum Panel hinzufügen
		numberBtns[10] = new JButton(",");
		numberBtns[11] = new JButton("+/-");
		
		for (int i = 10; i < 12; i++) {
			configureButton(numberBtns[i]);
			numberPanel.add(numberBtns[i]);
		}
		
	}
	
	private JPanel createOperationPanel() {
		// Panel für Rechenoperationstasten und C-Taste
		JPanel operationPanel = new JPanel();
		
		// Grid-Layout mit 6 Button in einer Spalte
		operationPanel.setLayout(new GridLayout(6,1,10,5));
		operationPanel.setPreferredSize(new Dimension(100, 400));
		operationPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		
		// Rechenoperationstasten zum Panel hinzufügen
		addOperationBtns(operationPanel);
		
		return operationPanel;
	}
	
	private void addOperationBtns(JPanel operationPanel) {
		// Wir brauchen 6 Rechenoperationstasten: + - * / = C
		operationBtns = new JButton[6];
		
		// Die 6 Button erstellen und konfigurieren
		for (int i = 0; i < operationBtns.length; i++) {
			operationBtns[i] = new JButton();
			configureButton(operationBtns[i]);
		}
		
		// Den Text der Buttons festlegen
		operationBtns[0].setText("C");
		operationBtns[1].setText("/");
		operationBtns[2].setText("*");
		operationBtns[3].setText("-");
		operationBtns[4].setText("+");
		operationBtns[5].setText("=");
		
		// Zum Schluss: Die Buttons zum Panel hinzufügen
		for (int i = 0; i < operationBtns.length; i++) {
			operationPanel.add(operationBtns[i]);
		}
	}
	
	
	private void configureButton(JButton button) {
		// Alle Button haben einen ActionListener und sind nicht fokussierbar. Font ist inkTree
		button.addActionListener(this);
		button.setFocusable(false);
		button.setFont(inkTree);
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
		
		// Enter-Key fungiert wie = und . fungiert wie Komma
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
