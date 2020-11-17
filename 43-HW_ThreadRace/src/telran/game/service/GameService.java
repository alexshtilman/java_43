package telran.game.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class GameService extends Thread {
	int distance;
	Instant start;
	static Integer place;
	String[] participants;

	public GameService(int distance, String[] participants, Instant start) {
		this.distance = distance;
		this.participants = participants;
		this.start = start;
		place = 0;
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
		synchronized (GameService.class) {
			participants[place++] = String.format("%s finished with place %d after %s Msec", threadNumber, place, time);
		}
		
	}

	public int getRandomInt(int min, int max) {
		return new Random().nextInt(max - min) + min;
	}

	public String[] getParticipants() {
		return participants;
	}
}
