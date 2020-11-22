package telran.game.controller;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import client.telran.view.*;
import telran.game.service.GameService;

class Sortbyplace implements Comparator<GameService> {
	// Used for sorting in ascending order of
	// roll name
	public int compare(GameService a, GameService b) {
		if(a.finishPlace - b.finishPlace == 0) {
			return (int) (a.finishTime - b.finishTime) ;
		}
		return a.finishPlace - b.finishPlace;
	}
}

public class GameClientAppl {

	private static final int LINES = 50;

	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		Menu menu = new Menu("Menu", Item.of("Start a new race", GameClientAppl::startRace), Item.exit());
		menu.perform(io);
	}

	public static void startRace(InputOutput io) {

		int numOfThreads = io.readInteger("number of threads", 3, 10000);
		int distance = io.readInteger("number of threads", 100, 10000);
		var start = Instant.now();

		List<GameService> rides = Stream.generate(() -> new GameService(distance, start)).limit(numOfThreads)
				.collect(Collectors.toList());

		rides.forEach(GameService::start);

		rides.forEach(r -> {
			try {
				r.join();
			} catch (InterruptedException e) {
				// we wait each thread...
			}
		});

		System.out.println("ConcurrentLinkedDeque");
		GameService.getFromCollection();

		System.out.println("ReentrantLock");
		GameService.getLocked();

		System.out.println("ReentrantLock");
		GameService.getSync();

		System.out.println("AtomicInteger");
		System.out.println("=".repeat(LINES));

		Collections.sort(rides, new Sortbyplace());

		for (GameService ride : rides) {
			System.out.println(ride.getResults());
		}
	}
}
