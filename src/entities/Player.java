package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;

import java.util.List;

public class Player extends Entity {

	private static final float RUN_SPEED = 70;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 18;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain terrain, List<Entity> entities, List<Entity> normalEntities, List<Entity> movingEntities) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

		if(getPosition().z > -798 && getPosition().z < -1 && getPosition().x > 1 && getPosition().x < 798) {

			// Add the collision detection within the wall

			/*for(Entity entity : entities){
			if(Math.round(getPosition().x) == Math.round(entity.getPosition().x) && Math.round(getPosition().z) == Math.round(entity.getPosition().z)) trigger = true;
			}
			for(Entity entity : normalEntities){
				if(Math.round(getPosition().x) == Math.round(entity.getPosition().x) && Math.round(getPosition().z) == Math.round(entity.getPosition().z)) trigger = true;
			}*/
			super.increasePosition(dx, 0, dz);
		}else{ // THE WALL
			if(getPosition().z < -798) super.getPosition().z = -797.9f;
			if(getPosition().z > -1) super.getPosition().z = -1.1f;
			if(getPosition().x < 1) super.getPosition().x = 1.1f;
			if(getPosition().x > 798) super.getPosition().x = 797.9f;
		}
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

		//System.out.println(getPosition().x + " " + getPosition().z);

		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
