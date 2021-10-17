import java.awt.EventQueue;

import javafx.embed.swing.JFXPanel;

public class Main {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new JFXPanel(); //added this
				try {
					ChooseMusic window = new ChooseMusic();
					//window.frame.setVisible(true);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
