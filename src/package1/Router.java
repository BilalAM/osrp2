package package1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router {

	public String name;
	public static Table routerTable;
	private static List<Socket> historyOfRouterConnections = new ArrayList<>();
	private ServerSocket selfServer;
	private int PORT;
	private static Scanner scanner = new Scanner(System.in);

	public Router(String name, int port) {
		this.PORT = port;
		try {
			selfServer = new ServerSocket(PORT);
			this.name = name;
			routerTable = new Table(selfServer.getInetAddress());
			System.out.println("Empty Router created by name.... " + name);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WARNING*** , Router " + name + " cannot be created at Port " + port);
		}
	}

	/**
	 * listens for active connection from other router and add that entry into table
	 * , also 'saves' the connections in a list
	 */
	public void initialize() {
		Socket otherRouter = null;
		try {
			while (true) {
				System.out.println("Waiting For Connection....");
				otherRouter = selfServer.accept();
				if (checkExistingConnection(otherRouter)) {
					System.out.println("Connection already exists....");
					continue;
				} else {
					System.out.println("Router connection found...");
					System.out.println("Details  :- ");
					System.out.println("-Address : " + otherRouter.getInetAddress());
					System.out.println("-Port    :" + otherRouter.getPort());
					System.out.println("adding new entry in table....");
					routerTable.addNewEntry(otherRouter.getInetAddress(), otherRouter.getInetAddress(), 1);
					historyOfRouterConnections.add(otherRouter);
					routerTable.displayTable();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				otherRouter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param the
	 *            new socket to check for prior existance
	 * @return whether the socket already exists or not
	 */
	private static boolean checkExistingConnection(Socket toCheck) {
		for (Socket socket : historyOfRouterConnections) {
			if (socket.getInetAddress().equals(toCheck.getInetAddress())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * tries to connect to a seperate router
	 */
	public void connectToRouter() {
		System.out.println("Enter IP Address Of Router To Connect");
		String ip = scanner.nextLine();
		Socket socket = null;
		try {

			if (checkExistingConnection(socket)) {
				System.out.println("Connection already exists....");
			} else {
				socket = new Socket(InetAddress.getByName(ip), PORT);
				System.out.println("Connected to a router " + ip);
				// add this to the history
				historyOfRouterConnections.add(socket);
				routerTable.addNewEntry(socket.getInetAddress(), socket.getInetAddress(), 1);
			}
		} catch (Exception e) {
			System.out.println("cannot connect ");
			e.printStackTrace();
		}
	}

	// attempt..
	public void initiateProcess() {
		Callable<Void> process1 = (() -> {
			initialize();
			return null;
		});

		ExecutorService service = Executors.newFixedThreadPool(5);
		try {
			service.submit(process1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * RouterA routerA = new RouterA("bilal router"); routerA.initialize();
	 * routerA.connectTo(routerB); routerA.connectTo(routerC);
	 * 
	 * RouterB routerB = new RouterB("sara router"); routerB.initialize();
	 * routerB.connectTo(routerA); routerB.connectTo(routerC);
	 * 
	 * RouterC routerC = new RouterC("asad router"); routerC.initialize();
	 * routerC.connectTo(routerA); routerC.connectTo(routerB);
	 * 
	 * 
	 * NetworkGrid gr7idA = new NetworkGrid(routerA); gridA.learnFrom(routerB)
	 * //send B table to A gridA.learnFrom(routerC); // send C table to A
	 * gridA.showTable();
	 * 
	 * NetworkGrid gridB = new Networkrid(routerB); gridB.learnFrom(routerC);
	 * gridB.showTable();
	 * 
	 * Host bahria = new Host(); bahria.connectAndSend(routerA,"my destination
	 * network"); bahria.showPath();
	 * 
	 * 
	 */

	public static void main(String args[]) {
		Router router = new Router("bilal", 1999);
		router.initiateProcess();
		router.connectToRouter();
	}

}
