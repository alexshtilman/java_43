package client.telran.view;

import java.util.function.Consumer;

public interface Item {
	String displyName();

	void perform(InputOutput io);

	default boolean isExit() {
		return false;
	}

	public static Item of(String name, Consumer<InputOutput> action, boolean isExit) {

		return new Item() {
			@Override
			public void perform(InputOutput io) {
				action.accept(io);
			}

			@Override
			public String displyName() {

				return name;
			}

			@Override
			public boolean isExit() {
				return isExit;
			}
		};
	}

	public static Item of(String name, Consumer<InputOutput> action) {
		return of(name, action, false);
	}

	public static Item exit() {
		return of("exit", a -> {
		}, true);
	}
}
