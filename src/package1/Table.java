package package1;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Table {
	InetAddress source;
	InetAddress destination;
	InetAddress next;
	int cost;

	List<InetAddress> destinations = new ArrayList<>();
	List<InetAddress> nexts = new ArrayList<>();
	List<Integer> costs = new ArrayList<>();

	List<Entry> entries = new ArrayList<>();

	public Table(InetAddress source) {
		this.source = source;
	}

	public void addNewEntry(InetAddress destination, InetAddress next, int cost) {
		this.destination = destination;
		this.next = next;
		this.cost = cost;
		this.destinations.add(destination);
		this.nexts.add(next);
		this.costs.add(cost);

		entries.add(new Entry(destination, next, cost));
	}

	public class Entry {
		InetAddress destination;
		InetAddress next;
		int cost;

		public Entry(InetAddress destination, InetAddress next, int cost) {
			this.destination = destination;
			this.next = next;
			this.cost = cost;
		}
	}

	public void displayTable() {
		System.out.println("\n");

		System.out.println("===========================================================");
		System.out.println("  DESTINATION \t          NEXT \t \t    COST ");
		System.out.println("===========================================================");
		for (Entry entry : entries) {
			System.out.println(entry.destination + "    " + entry.next + "    " + entry.cost);
		}
		System.out.println("\n");
	}

	public static void main(String args[]) throws Exception {

	}

}
