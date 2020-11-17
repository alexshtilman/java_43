package client.telran.view;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleInputOutput implements InputOutput {

	Scanner scanner = new Scanner(System.in);

	@Override
	public String readString(String promt) {

		writeLn(promt + " >");
		return scanner.nextLine();
	}

	@Override
	public void writeObject(Object obj) {
		System.out.print(obj);
	}
	
	@Override
	public  void clrscr() {
		// Clears Screen in java
		try {
			if (System.getProperty("os.name").contains("Windows"))
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				Runtime.getRuntime().exec("clear");
		} catch (IOException | InterruptedException ex) {
		}

	}

	@Override
	public void writeObject(Object obj, boolean color) {
		System.err.print(obj);
	}
}
