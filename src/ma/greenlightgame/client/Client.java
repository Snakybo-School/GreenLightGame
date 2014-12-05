package ma.greenlightgame.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import ma.greenlightgame.client.entity.player.EntityArm;
import ma.greenlightgame.client.entity.player.EntityPlayer;
import ma.greenlightgame.client.entity.player.EntityPlayerControllable;
import ma.greenlightgame.client.entity.wall.EntityWall;
import ma.greenlightgame.client.network.UDPClient;
import ma.greenlightgame.client.network.UDPClientHandler;
import ma.greenlightgame.common.config.Config;
import ma.greenlightgame.common.network.NetworkData;
import ma.greenlightgame.common.network.NetworkData.NetworkMessage;
import ma.greenlightgame.common.screen.Screen;
import ma.greenlightgame.common.screen.ScreenMainMenu;

public class Client {
	private static UDPClientHandler udpClientHandler;
	private static UDPClient udpClient;
	
	private static Screen screen;
	
	private static boolean ingame;
	
	private Level level;
	
	public Client() {
		EntityPlayer.load();
		EntityArm.load();
		Level.load();
		
		udpClientHandler = new UDPClientHandler(this);
		screen = new ScreenMainMenu();
		
		ingame = false;
	}
	
	public void update(float delta) {
		if(ingame) {
			final EntityPlayer[] players = udpClientHandler.getPlayers();
			final EntityWall[] walls = level.getWalls();
			
			for(EntityPlayer player : players) {
				if(player != null) {
					if(player.isAlive()) {
						player.update(delta);
						
						if(player instanceof EntityPlayerControllable) {
							EntityPlayerControllable p = (EntityPlayerControllable)player;
							
							p.checkAttackCollision(players);
							p.checkCollision(walls);
						}
					}
				}
			}
			
			if(level != null)
				level.update(delta);
		} else {
			if(screen != null)
				screen.update();
		}
	}
	
	public void render() {
		if(ingame) {
			final EntityPlayer[] players = udpClientHandler.getPlayers();
			
			if(level != null)
				level.render();
			
			for(EntityPlayer player : players)
				if(player != null)
					player.render();
			
			if(Config.DRAW_DEBUG) {
				if(level != null)
					level.drawDebug();
				
				for(EntityPlayer player : players)
					if(player != null)
						if(player.isAlive())
							player.drawDebug();
			}
		} else {
			if(screen != null) {
				screen.render();
				
				if(Config.DRAW_DEBUG)
					screen.drawDebug();
			}
		}
	}
	
	public void destroy() {
		disconnect();
	}
	
	public void loadLevel(int levelId) {
		level = new Level(levelId);
		ingame = true;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public static void sendUDP(int type, Object... message) {
		String msg = Integer.toString(type) + NetworkData.SEPERATOR;
		
		for(int i = 0; i < message.length; i++)
			msg += (message[i] + NetworkData.SEPERATOR);
		
		try {
			udpClient.send(msg.getBytes("UTF-8"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void connect(InetAddress address, int port) {
		disconnect();
		
		try {
			udpClient = new UDPClient(address, port, udpClientHandler);
			Client.sendUDP(NetworkMessage.CLIENT_REQUEST_CONNECT);
		} catch(SocketException e) {
			e.printStackTrace();
		}
	}
	
	public static void disconnect() {
		if(udpClient != null)
			udpClient.close();
		
		udpClientHandler.disconnect();
	}
	
	public static void setActiveScreen(Screen screen) {
		Client.screen = screen;
	}
	
	public static UDPClientHandler getUDPHandler() {
		return udpClientHandler;
	}
	
	public static boolean isStarted() {
		return ingame;
	}
}