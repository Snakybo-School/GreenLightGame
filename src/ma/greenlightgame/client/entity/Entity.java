package ma.greenlightgame.client.entity;

import java.awt.Color;

import ma.greenlightgame.client.utils.DebugDraw;

public abstract class Entity implements IEntity {
	protected float rotation;
	
	protected int x;
	protected int y;
	
	protected boolean colliding;
	
	public Entity() {
		x = 0;
		y = 0;
		
		colliding = false;
	}
	
	@Override
	public void drawDebug() {
		Color color = new Color(0, 1, 0);
		
		if(colliding) {
			color = new Color(1, 0, 0);
		}
		
		DebugDraw.drawBounds(getBounds(), color);
		DebugDraw.drawPoint(x, y, new Color(0, 0, 1));
	}
	
	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public float getRotation() {
		return rotation;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public boolean isColliding() {
		return colliding;
	}
}
