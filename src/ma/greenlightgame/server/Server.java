package ma.greenlightgame.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ma.greenlightgame.client.Client;
import ma.greenlightgame.common.config.Config;
import ma.greenlightgame.common.network.NetworkData;
import ma.greenlightgame.server.network.UDPServer;
import ma.greenlightgame.server.network.UDPServerHandler;

public class Server {
	private static UDPServerHandler udpServerHandler;
	private static UDPServer udpServer;
	
	private static boolean started;
	
	public Server() {
		try {
			udpServerHandler = new UDPServerHandler();
			udpServer = new UDPServer(Config.getInt(Config.LAST_SERVER_PORT), udpServerHandler);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		started = false;
		
		try {
			Client.connect(InetAddress.getLocalHost(), Config.getInt(Config.LAST_SERVER_PORT));
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void update(float delta) {
<<<<<<< Updated upstream
		
=======
		if(!started && Input.isKeyDown(KeyCode.P) || !started && UserInterface.play)
			start(0);
>>>>>>> Stashed changes
	}
	
	public void destroy() {
		udpServerHandler.destroy();
	}
	
	public static void sendUDP(InetAddress address, int port, int type, Object... message) {
		String msg = Integer.toString(type) + NetworkData.SEPERATOR;
		
		for(int i = 0; i < message.length; i++)
			msg += (message[i] + NetworkData.SEPERATOR);
		
		try {
			udpServer.send(address, port, msg.getBytes("UTF-8"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void start(int levelId) {
		started = true;
		System.out.println("Silent");
		udpServerHandler.startGame(levelId);
	}
	
	public static boolean isStarted() {
		return started;
	}
}