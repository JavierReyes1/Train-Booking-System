package com.trainbooking;

import com.trainbooking.db.DatabaseManager;
import com.trainbooking.gui.MainFrame;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {

		// set up db
		DatabaseManager db = DatabaseManager.getInstance();
		try {
			db.connect();
			db.createTables();
			db.seedSampleData();
			System.out.println("Database ready.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Failed to start database:\n" + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		}

		// launch gui
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame();
				frame.setVisible(true);
			}
		});
	}
}
