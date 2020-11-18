package telran.game.controller;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import client.telran.view.*;
import telran.game.service.GameService;

public class GameClientAppl {

	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		Menu menu = new Menu("Menu", Item.of("Start a new race", GameClientAppl::startRace), Item.exit());
		menu.perform(io);
	}

	public static void startRace(InputOutput io) {
		
		int numOfThreads = io.readInteger("number of threads",3,10000);
		int distance = io.readInteger("number of threads",100,10000);	
		var start = Instant.now();
		List<GameService> rides = Stream.generate(()->new GameService(distance,start))
				.limit(numOfThreads)
				.collect(Collectors.toList());
		
		rides.forEach(GameService::start);
		
		rides.forEach(r->{
			try {
				r.join();
			} catch (InterruptedException e) {
				// we wait each thread...
			}
		});
		System.out.println("=".repeat(80));
		System.out.println("Collections.synchronizedList");
		System.out.println("=".repeat(80));
		for(String p:GameService.winners) {
			System.out.println(p);
		}
		System.out.println("=".repeat(80));
		System.out.println("AtomicInteger");
		System.out.println("=".repeat(80));
		for(String p:GameService.participants) {
			System.out.println(p);
		}
		System.out.println("=".repeat(80));
		System.out.println("synchronized (GameService.class)");
		System.out.println("=".repeat(80));
		for(String p:GameService.synchParticipants) {
			System.out.println(p);
		}
		
	}
}
