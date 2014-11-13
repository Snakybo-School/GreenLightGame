package ma.greenlightgame.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

/** @author Kevin Krol
 * @since Nov 12, 2014 */
public class Config {	
	public static final String NAME = "GreenLight Game";
	
	// Keys
	public static final String DISPLAY_WIDTH = "displayWidth";
	public static final String DISPLAY_HEIGHT = "displayHeight";
	public static final String FULLSCREEN = "fullscreen";
	public static final String VSYNC = "vsync";
	
	public static final int RENDER_WIDTH = 1920;
	public static final int RENDER_HEIGHT = 1080;
	public static final int FRAMERATE = 60;
	
	public static final boolean RENDER_BOUNDS = true;
	
	private static Map<String, Object> config;
	
	private static final String CONFIG_SCRIPT = "./res/lua/config.lua";
	
	public static void load() {
		config = new HashMap<String, Object>();
		
		if(!new File(CONFIG_SCRIPT).exists()) {
			createConfig();
			System.out.println("Created config file");
		}
		
		loadConfig();
	}
	
	public static void flush() {
		createConfig();
		System.out.println("Flushed config file");
	}
	
	private static void createConfig() {
		String[] configFile = {
			"config = {",
			"    [\"displayWidth\"] = " + (config.isEmpty() ? "1280" : getInt(DISPLAY_WIDTH)) + ",",
			"    [\"displayHeight\"] = " + (config.isEmpty() ? "720" : getInt(DISPLAY_HEIGHT)) + ",",
			"",
			"    [\"fullscreen\"] = " + (config.isEmpty() ? "false" : getBool(FULLSCREEN)) + ",",
			"    [\"vsync\"] = " + (config.isEmpty() ? "false" : getBool(VSYNC)) + "",
			"}"
		};
		
		BufferedWriter writer = null;
		
		try {
			File luaFolder = new File("./res/lua/");
			if(!luaFolder.exists())
				luaFolder.mkdir();
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CONFIG_SCRIPT), "UTF-8"));
			
			int l = configFile.length;
			for(int i = 0; i < l; i++)
				writer.write(configFile[i] + "\n");
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void loadConfig() {
		Globals globals = JsePlatform.standardGlobals();
		globals.loadfile(CONFIG_SCRIPT).call(LuaValue.valueOf(CONFIG_SCRIPT));
		
		setValues((LuaTable)globals.get("config"));
		
		System.out.println("Loaded config file");
	}
	
	private static void setValues(LuaTable config) {
		// Display size
		setInt(DISPLAY_WIDTH, 		config.get(DISPLAY_WIDTH).toint());
		setInt(DISPLAY_HEIGHT, 		config.get(DISPLAY_HEIGHT).toint());
		
		// Fullscreen
		setBool(FULLSCREEN, 		config.get(FULLSCREEN).toboolean());
		setBool(VSYNC,				config.get(VSYNC).toboolean());
	}
	
	public static void setInt(String key, Integer value) {
		config.put(key, value);
	}
	
	public static void setBool(String key, Boolean value) {
		config.put(key, value);
	}
	
	public static int getInt(String key) {
		return (int)config.get(key);
	}
	
	public static boolean getBool(String key) {
		return (boolean)config.get(key);
	}
}