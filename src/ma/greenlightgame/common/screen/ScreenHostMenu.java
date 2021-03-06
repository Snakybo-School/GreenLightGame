package ma.greenlightgame.common.screen;

import ma.greenlightgame.client.renderer.Texture;
import ma.greenlightgame.common.screen.components.Button;
import ma.greenlightgame.common.screen.components.Button.ButtonActionHandler;
import ma.greenlightgame.server.Server;

public class ScreenHostMenu implements Screen {
	private static final int BTN_PLAY = new Texture("GUI/Buttons/play.png").getId();
	private static final int BTN_PLAY_HOVER = new Texture("GUI/Buttons/play_hover.png").getId();
	private static final int BTN_PLAY_PRESS = new Texture("GUI/Buttons/play_pushed.png").getId();
	
	private Button[] buttons;
	
	public ScreenHostMenu() {
		buttons = new Button[] {
		// Play
		new Button(100, 200, 100, 100, new ButtonActionHandler() {
			@Override
			public void onCreate(Button button) {
				button.setTexture(BTN_PLAY);
			}
			
			@Override
			public void onHover(Button button, boolean hovering) {
				if(hovering) {
					button.setTexture(BTN_PLAY_HOVER);
				} else {
					button.setTexture(BTN_PLAY);
				}
			}
			
			@Override
			public void onMouse(Button button, boolean down) {
				if(down) {
					button.setTexture(BTN_PLAY_PRESS);
				} else {
					button.setTexture(BTN_PLAY_HOVER);
				}
			}
			
			@Override
			public void onClick(Button button) {
				System.out.println("Pressed button!");
				
				Server.start(0);
			}
		})};
	}
	
	@Override
	public void update() {
		for(Button button : buttons) {
			button.update();
		}
	}
	
	@Override
	public void render() {
		for(Button button : buttons) {
			button.render();
		}
	}
	
	@Override
	public void drawDebug() {
		for(Button button : buttons) {
			button.drawDebug();
		}
	}
	
}
