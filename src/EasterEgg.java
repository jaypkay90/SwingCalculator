import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class EasterEgg extends JFrame implements WindowListener{
	
	private static final long serialVersionUID = 1L;

	EasterEgg(JFrame hauptframe) {
		JLabel easterEggLabel = createEasterEggLabel();
		
		ImageIcon icon = new ImageIcon("calculator.png");
		this.setIconImage(icon.getImage()); // Calculator Icon als Programm-Icon festlegen
		this.setResizable(false);
		this.addWindowListener(this); // Window Listener hinzufügen
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // beim Schließen: Nur das aktive Fenster schließen, NICHT das ganze Pogramm
		this.setLocationRelativeTo(hauptframe);
		this.getContentPane().add(easterEggLabel); // label zum content hinzufügen
		this.pack();
		this.setVisible(true);
	}
	
	private JLabel createEasterEggLabel() {
		JLabel label = new JLabel();
		
		// HTML für das Label --> enthält Bild und Text
		String htmlFormatText = "<html><div style='width: 100px; display: block; text-align:center;'>" +
                "<img src='file:mario.jpeg' width='400' height='400'><br>" +
                "<div style='font-family: Ink Free; font-size: 20px; color: #21366E; padding: 10px;'>" +
                "Super Mario hat am 13.09.1985 Geburtstag!" +
                "</div></div></html>";
		
		label.setText(htmlFormatText); // HTML-Text zum Label hinzufügen
		
		// Label Hintergrund weiß machen
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		
		return label;
	}

	
	public void playMarioWAV() {
		try {
			// Mario Voice als WAV abspielen
			File marioWAV = new File("Super-Mario.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(marioWAV);
			
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();		
		}
		// Alle möglichen Exceptions catchen
		catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			// Stack Trace drucken, wenn Exceptions aufgetreten sind
			e.printStackTrace();
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// Sobald das Window vollständig geladen hat --> spiele die Audio-Datei
		playMarioWAV();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
