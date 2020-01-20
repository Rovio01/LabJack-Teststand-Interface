package co.jensrud.sac.teststandclient;

import com.labjack.LJM;
import com.labjack.LJMException;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {

	private static GUI gui;
	static int handle;

	public static void main(String[] args) {
		try {
			//UI stuff
			SwingUtilities.invokeLater(Main::createAndShowGUI);

			//LabJack stuff
			IntByReference handleRef = new IntByReference(0);
			LJM.openS("ANY", "ANY", "ANY", handleRef);
			handle = handleRef.getValue();
			print("LabJack connected");
			String name = "SERIAL_NUMBER";
			DoubleByReference valueRef = new DoubleByReference(0);
			LJM.eReadName(handle, name, valueRef);
			print("Serial Number: " + new DecimalFormat("#.#").format( valueRef.getValue() ));

			gui.setConnectionStatus("Connected");
/*
			int iterations = 0;

			int[] aAddresses = {0,2,4};
			int[] aTypes = {3,3,3};
			double[] aValues = {0,0,0};
			int numFrames = aAddresses.length;
			IntByReference errAddr = new IntByReference(0);

			long startingTime = System.currentTimeMillis();
			while (iterations<10000) {
				LJM.eReadAddresses(handle, numFrames, aAddresses,aTypes,aValues,errAddr);
				print(Arrays.toString(aValues));
				iterations++;
			}
			long endingTime = System.currentTimeMillis();
			print("Exiting");
			double rate = ((double)iterations) * 1000 / (endingTime-startingTime);
			print("Rate: " + rate);
			LJM.close(handle);
*/
		}
		catch (LJMException le) {
			le.printStackTrace();
			LJM.closeAll();
		}
	}

	private static void createAndShowGUI() {

		gui = new GUI("GT SAC DAQ Interface");
		gui.setVisible(true);
		print("GUI Initialized");
	}

	public static void setFiringPin(boolean state) {
		LJM.eWriteAddress(handle,2015,0,state?1:0);
	}

	private static void print(String text) {
		System.out.println(text);
	}
}
