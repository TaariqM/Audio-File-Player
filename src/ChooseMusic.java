import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.awt.event.ActionEvent;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.*;
import javafx.util.Duration;

//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChooseMusic {

	private JFrame frame;
	private final JFileChooser openChooseMusic;
	private String fileName = null;
	private int count = 0;
	private Duration length;
	Media media;
	MediaPlayer audio;
	private Queue<String> songQueue = new LinkedList<String>(); //this queue is for the songs that are queued next to play
	private Stack<String> prevStack = new Stack<String>(); //this stack is for the songs that were previously queued
	String songName = "";
	int clickCount = 0; //used for rewinding
	
	/**
	 * Create the application.
	 */
	public ChooseMusic() {
		initialize();
		
		openChooseMusic = new JFileChooser();
		openChooseMusic.setCurrentDirectory(new File("C:\\Users"));
		openChooseMusic.setFileFilter(new FileNameExtensionFilter("MP3 file", "mp3", "wav", "mpeg"));
	}
	
	/**
	 * This method returns the frame variable
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Java Audio Player");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton chooseMusicBtn = new JButton("Choose Music");
		chooseMusicBtn.setBounds(150, 30, 137, 25);
		chooseMusicBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = openChooseMusic.showOpenDialog(chooseMusicBtn); 
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						songName = openChooseMusic.getSelectedFile().getName(); //gets the name of the file selected
						fileName = openChooseMusic.getSelectedFile().toURI().toString(); //gets the file and converts it to a string
						songQueue.add(fileName); //add the song to the queue
					}catch (Exception e) {

					}
					
				}
				else {
					;
				}
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(chooseMusicBtn);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (count == 0) { //if count == 0, play  the song from the beginning
					try {
						String song = songQueue.peek(); //retrieves the first song in the queue
						media = new Media(song);
						audio = new MediaPlayer(media); //create a media player for a specific media
						prevStack.push(songQueue.poll()); //removes the song that is played, and adds it to the prevStack						
						audio.play(); //play the audio from the player
					} catch (Exception e) {

					}
				}
				else { //else, continue playing the song where it was paused at
					audio.seek(length); //seeks the audio to the new play back time, specified by 'length'
					audio.play(); //play the audio at the new time
				}
			}
		});
		btnPlay.setBounds(150, 83, 137, 25);
		frame.getContentPane().add(btnPlay);
		
		JButton btnRwnd = new JButton("Rewind");
		btnRwnd.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1) && (clickCount == 0)) { //if the left mouse button is double clicked and the counter is 0
					audio.stop(); //stop the audio that is playing
					prevStack.pop();
					String song = prevStack.pop(); //removes song previously played and places it in 'song' variable
					media = new Media(song);
					audio = new MediaPlayer(media); //create a media player for a specific media
					audio.play(); //play the audio from the player
					clickCount++; //increment counter
				}
				else if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1) && (clickCount > 0)) { //if the left mouse button is double clicked and the counter is greater than 0
					audio.stop(); //stop the audio that is playing
					String song = prevStack.pop(); //removes song previously played and places it in 'song' variable
					media = new Media(song);
					audio = new MediaPlayer(media); //create a media player for a specific media
					audio.play(); //play the audio from the player
				}
				else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) { //if the left mouse button is clicked once
					audio.stop(); //stop the audio being played
					count = 0; //reset the counter
					audio.play();
				}
				else { //else do nothing
					;
				}
				
			}
		});
		
		btnRwnd.setBounds(10, 119, 137, 25);
		frame.getContentPane().add(btnRwnd);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					audio.pause(); //pause the audio being played
					length = audio.getCurrentTime(); //get the time that the song was paused at
					count++; //increment the count
				} catch (Exception a) {

				}
			}
		});
		btnPause.setBounds(150, 118, 137, 25);
		frame.getContentPane().add(btnPause);
		
		JButton btnFFwd = new JButton("Fast Forward");
		JLabel txtFastForward = new JLabel("");
		btnFFwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					length = audio.getTotalDuration(); //get the total duration of the song
					audio.seek(length); //move the audio to the end of the song
					if (!songQueue.isEmpty()) {
						media = new Media(songQueue.peek());
						audio = new MediaPlayer(media); //create a media player for a specific media
						prevStack.push(songQueue.poll()); //removes the song that is played, and adds it to the prevStack
						audio.play(); //play the audio from the player
					}
				} catch (Exception a) {
					
				}
			}
		});
		btnFFwd.setBounds(289, 119, 137, 25);
		frame.getContentPane().add(btnFFwd);
		frame.getContentPane().add(txtFastForward);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					audio.stop(); //stop the audio being played
					count = 0; //reset the counter
				} catch (Exception a) {
					
				}
			}
		});
		btnStop.setBounds(150, 156, 137, 25);
		frame.getContentPane().add(btnStop);
	}
}
