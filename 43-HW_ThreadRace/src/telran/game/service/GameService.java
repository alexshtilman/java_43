package telran.game.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

public class GameService extends Thread {
	int distance;
	Instant start;
	static int place;
	public static ArrayList <String> participants = new ArrayList<>();

	public GameService(int distance, Instant start) {
		this.distance = distance;
		this.start = start;
		place = 0;
		participants.clear();
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
		synchronized (participants) {
			participants.add(String.format("%s finished with place %d after %s Msec", threadNumber, place, time));
			place++;
			try {
				sleep(getRandomInt(2, 5));
			} catch (InterruptedException e) {
				// no action at interruption required
			}
		}
		
	}

	public int getRandomInt(int min, int max) {
		return new Random().nextInt(max - min) + min;
	}
}
