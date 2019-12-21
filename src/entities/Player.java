package entities;

import font.FontType;
import font.GUIText;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
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
	public static int SCORE = 0;
	private GUIText score = null;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain terrain, List<Entity> entities, List<Entity> normalEntities, List<Entity> movingEntities, FontType font, GUIText score) {
		checkInputs();
		if(this.score==null) this.score = score;
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();

		/* IF PLAYER STEPS IN WATER HE IS SLOWED */
		if(getPosition().y <= -2) distance = distance / 2;

		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

		if(getPosition().z > -798 && getPosition().z < -1 && getPosition().x > 1 && getPosition().x < 798) {

			boolean collision = false;
			for(Entity entity : entities){
				if(!entity.isPlayer() && !entity.isWalkthrough() &&(entity.getPosition().x >= this.getPosition().x-2 && entity.getPosition().x <= this.getPosition().x+2) && (entity.getPosition().z <= this.getPosition().z+2 && entity.getPosition().z >= this.getPosition().z-2)) {
					if(getPosition().z-2 <= entity.getPosition().z) getPosition().z -= 0.9f;
					else if(getPosition().z+2 >= entity.getPosition().z) getPosition().z += 0.9f;
					else if(getPosition().x-2 <= entity.getPosition().x) getPosition().x -= 0.9f;
					else if(getPosition().x+2 >= entity.getPosition().x) getPosition().x += 0.9f;
					collision = true;
				}
			}
			for(Entity entity : normalEntities){
				if(!entity.isPlayer() &&(entity.getPosition().x >= this.getPosition().x-5 && entity.getPosition().x <= this.getPosition().x+5) && (entity.getPosition().z <= this.getPosition().z+5 && entity.getPosition().z >= this.getPosition().z-5)) {
					if(getPosition().z-5 <= entity.getPosition().z) getPosition().z -= 0.9f;
					else if(getPosition().z+5 >= entity.getPosition().z) getPosition().z += 0.9f;
					else if(getPosition().x-5 <= entity.getPosition().x) getPosition().x -= 0.9f;
					else if(getPosition().x+5 >= entity.getPosition().x) getPosition().x += 0.9f;
					collision = true;
				}
			}
			if(!collision) super.increasePosition(dx, 0, dz);

			// PLAYER GETS SCORE BY STEPPING ON A BUNNY
			for(Entity entity : movingEntities){
				if(!entity.isPlayer() && (entity.getPosition().x >= this.getPosition().x-2 && entity.getPosition().x <= this.getPosition().x+2) && (entity.getPosition().z <= this.getPosition().z+2 && entity.getPosition().z >= this.getPosition().z-2)) {
					SCORE ++;
					setScore(font);
					respawnRabbit(entity, terrain);
				}
			}

		}else{ // THE WALL
			if(getPosition().z < -798) super.getPosition().z = -797.9f;
			if(getPosition().z > -1) super.getPosition().z = -1.1f;
			if(getPosition().x < 1) super.getPosition().x = 1.1f;
			if(getPosition().x > 798) super.getPosition().x = 797.9f;
		}
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	private void respawnRabbit(Entity entity, Terrain terrain){
		entity.setPosition(entity.getMovementPosition(terrain));
		entity.getNewPosition();
	}

	private void setScore(FontType font){
		this.score.remove();
		this.score = new GUIText("Score: " + SCORE, 1.2f, font, new Vector2f(0.47f, 0.05f), 0.9f, true);
		this.score.setColour(1,1,1);
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
