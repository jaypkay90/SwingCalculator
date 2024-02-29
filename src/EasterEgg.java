
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class EasterEgg extends JFrame implements WindowListener{
	//JFrame frame = new JFrame();
	JLabel label = new JLabel("Super Mario hat am 13.09.1985 Geburtstag!");
	
	EasterEgg() {
		//ImageIcon marioImg = new ImageIcon("mario.jpeg");
		//marioImg = new ImageIcon(marioImg.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));
		
		//label.setLayout(new BorderLayout());
		/*label.setFont(new Font("Ink Free", Font.BOLD, 30));
		label.setForeground(new Color(0x21366E));
		label.setBackground(Color.WHITE); // bg color setzen
		label.setOpaque(true); // bg color anzeigen
		label.setIconTextGap(10);*/
		//label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		String htmlFormatText = "<html><div style='width: 100px; display: block; text-align:center;'>" +
                "<img src='file:mario.jpeg' width='400' height='400'><br>" +
                "<div style='font-family: Ink Free; font-size: 20px; color: #21366E; padding: 10px;'>" +
                "Super Mario hat am 13.09.1985 Geburtstag!" +
                "</div></div></html>";
		
		label.setText(htmlFormatText);
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		//label.setHorizontalAlignment(JLabel.CENTER);
		/*label.setIcon(marioImg);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);*/
		this.add(label);
		
		ImageIcon icon = new ImageIcon("calculator.png");
		this.addWindowListener(this);
		this.setIconImage(icon.getImage());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frame.setSize(400,400);
		//frame.setLayout(null);
		this.getContentPane().add(label);
		this.pack();
		this.setVisible(true);
	}
	
	public void playMarioWAV() {
		try {
			File marioWAV = new File("Super-Mario.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(marioWAV);
			
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
			
			// Den Clip zu Ende spielen lassen
			Thread.sleep(clip.getMicrosecondLength() / 1000);			
		}
		catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
			// Stack Trace drucken, auch wenn die Exception aufgefangen wurde
			e.printStackTrace();
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
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
