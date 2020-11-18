package telran.game.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class GameService extends Thread {
	int distance;
	Instant start;
	protected static List<String> lockedParticipants = new ArrayList<>();
	protected static List<String> syncParticipants = new ArrayList<>();

	public static List<String> winners = Collections.synchronizedList(new ArrayList<String>());
	ReentrantLock lock = new ReentrantLock();

	public GameService(int distance, Instant start) {
		this.distance = distance;
		this.start = start;
		winners.clear();
	}

	private static void printList(List<String> list) {
		int i = 1;
		System.out.println("=".repeat(80));
		for (String p : list) {
			System.out.printf("place-%d: %s\n", i++, p);
		}
		System.out.println("=".repeat(80));
	}

	public static void getSync() {
		printList(syncParticipants);
	}

	public static void getLocked() {
		printList(lockedParticipants);
	}

	public static void getFromCollection() {
		printList(winners);
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

		winners.add(threadNumber + ":" + time);

		lock.lock();
		try {
			lockedParticipants.add(threadNumber + ":" + time);
		} finally {
			lock.unlock();
		}

		synchronized (GameService.class) {
			syncParticipants.add(threadNumber + ":" + time);
		}

	}

	public int getRandomInt(int min, int max) {
		return new Random().nextInt(max - min) + min;
	}
}
