package telran.game.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameService extends Thread {
	int distance;
	Instant start;
	static AtomicInteger place = new AtomicInteger(0);
	static int placeOld;
	public static ArrayList<String> participants = new ArrayList<>();
	public static ArrayList<String> synchParticipants = new ArrayList<>();
	public static List<String> winners = Collections.synchronizedList(new ArrayList<String>());
	
	
	public GameService(int distance, Instant start) {
		this.distance = distance;
		this.start = start;
		place.getAndSet(0);
		placeOld = 1;
		participants.clear();
		winners.clear();
		synchParticipants.clear();
	}

	@Override
	public void run() {
		String threadNumber = Thread.currentThread().getName();
		for (int i = 0; i < distance; i++) {
			System.out.println(threadNumber);
			try {
				sleep(getRandomInt(2, 5));
			} catch (InterruptedException e) {
				// no action at interruption required
			}
		}
		long time = ChronoUnit.MILLIS.between(start, Instant.now());
		participants.add(String.format("%s finished with place %d after %s Msec", threadNumber, place.addAndGet(1), time));
		try {
			sleep(5);
		} catch (InterruptedException e) {
			// no action at interruption required
		}
		winners.add(threadNumber+":"+time);
		try {
			sleep(5);
		} catch (InterruptedException e) {
			// no action at interruption required
		}
		synchronized (GameService.class) {
			synchParticipants.add(String.format("%s finished with place %d after %s Msec", threadNumber, placeOld, time));
			placeOld++;
			
			//This code break all
			try {
				sleep(5);
			} catch (InterruptedException e) {
				// no action at interruption required
			}
		}
	}

	public int getRandomInt(int min, int max) {
		return new Random().nextInt(max - min) + min;
	}
}
