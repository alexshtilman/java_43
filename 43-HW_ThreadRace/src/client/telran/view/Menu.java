package client.telran.view;

import java.util.*;

public class Menu implements Item {
	String name;
	List<Item> items;

	public Menu(String name, List<Item> items) {
		super();
		this.name = name;
		this.items = items;
	}
	public Menu(String name, Item ...items) {
		this(name,Arrays.asList(items));
	}
	
	@Override
	public String displyName() {
		return name;
	}

	@Override
	public void perform(InputOutput io) {
		while (true) {
			displayMenu(io);
			int selectedItem = io.readInteger("Enter item number", 1, items.size());
			Item item = items.get(selectedItem - 1);
			try {
				item.perform(io);
				if (item.isExit())
					break;
			} catch (Exception e) {
				io.writeLn(e.getMessage());
			}
		}
	}

	private void displayMenu( InputOutput io) {
		io.clrscr();
		io.writeLn("=".repeat(40));
		io.writeLn("\t"+name);
		io.writeLn("=".repeat(40));
		int n = 1;
		for(Item item:items) {
			io.writeLn(n+". "+item.displyName());
			n++;
		}
	}

}
