package telran.game.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class GameService extends Thread {
	private static final int LINES = 50;
	int distance;
	Instant start;
	protected static List<String> lockedParticipants = new ArrayList<>();
	protected static List<String> syncParticipants = new ArrayList<>();
	static AtomicInteger curretPlace = new AtomicInteger(0)	 ;
	public long finishTime;
	public int finishPlace;
	String threadNumber;
	public static ConcurrentLinkedDeque<String> winners = new ConcurrentLinkedDeque( new ArrayList<String>());
	ReentrantLock lock = new ReentrantLock();

	public GameService(int distance, Instant start) {
		this.distance = distance;
		this.start = start;
		lockedParticipants.clear();
		syncParticipants.clear();
		winners.clear();
		finishTime = 0;
		finishPlace = 0;
		curretPlace.getAndSet(0);
	}

	private static void printList(List<String> list) {
		int i = 1;
		System.out.println("=".repeat(LINES));
		for (String p : list) {
			System.out.printf("%s at %d place\n", p, i++);
		}
		System.out.println("=".repeat(LINES));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distance;
		result = prime * result + finishPlace;
		result = prime * result + (int) (finishTime ^ (finishTime >>> 32));
		result = prime * result + ((lock == null) ? 0 : lock.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((threadNumber == null) ? 0 : threadNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameService other = (GameService) obj;
		if (distance != other.distance)
			return false;
		if (finishPlace != other.finishPlace)
			return false;
		if (finishTime != other.finishTime)
			return false;
		return true;
	}

	public static void getSync() {
		printList(syncParticipants);
	}

	public static void getLocked() {
		printList(lockedParticipants);
	}

	public static void getFromCollection() {
		
		
		System.out.println("=".repeat(LINES));
		int i =1;
		while (winners.size()>0) {
			System.out.printf("%s at %d place\n", winners.pollFirst(), i++);
		}
		System.out.println("=".repeat(LINES));
	}

	@Override
	public void run() {
		this.threadNumber = Thread.currentThread().getName();
		for (int i = 0; i < distance; i++) {
			System.out.println(threadNumber);
			try {
				sleep(getRandomInt(2, 5));
			} catch (InterruptedException e) {
				// no action at interruption required
			}
		}
		long time = ChronoUnit.MILLIS.between(start, Instant.now());
		finishPlace = curretPlace.addAndGet(1);
		finishTime = time;
		
		winners.add(threadNumber + " finished with time " + time);

		lock.lock();
		try {
			lockedParticipants.add(threadNumber + " finished with time " + time);
		} finally {
			lock.unlock();
		}

		synchronized (GameService.class) {
			syncParticipants.add(threadNumber + " finished with time " + time);
		}

	}
	

	
	public int getRandomInt(int min, int max) {
		return new Random().nextInt(max - min) + min;
	}
	public String getResults() {
		return String.format("%s finished with time %s at %d place",threadNumber,finishTime,finishPlace);
	}
}
