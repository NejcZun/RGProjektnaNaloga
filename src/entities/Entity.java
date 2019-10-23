package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

import java.util.Random;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	private Vector3f newPosition;
	private boolean isPlayer = false;

	//So entities can jump
	private boolean isInAir = false;
	private float upwardsSpeed = 0;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 18;
	
	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.newPosition = position;
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.textureIndex = index;
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.newPosition = position;
	}
	
	public float getTextureXOffset(){
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public Vector3f getNewPosition() {
		return this.newPosition;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getMovementPosition(Terrain terrain){
		Random random = new Random();
		float x = 400 + random.nextFloat() * 400;
		float z = -400 + random.nextFloat() * 400;
		float y = terrain.getHeightOfTerrain(x, z);
		return new Vector3f(x, y, z);
	}

	public void movement(Terrain terrain){
		Random random = new Random();
		if((this.newPosition == this.position) || (this.position.x >= this.newPosition.x -3 && this.position.x <= this.newPosition.x+3) && (this.position.z <= this.newPosition.z+3 && this.position.z >= this.newPosition.z-3)){
			this.newPosition = getMovementPosition(terrain);
			//change the rotation of the position the fucking bunny is facing
		}else{
			//Movement from position to newPosition
			if(this.position.x >= this.newPosition.x -1 && this.position.x <= this.newPosition.x+1){/*in proximity*/}
			else if(this.newPosition.x <= this.position.x) this.position.x -= 0.4f;
			else this.position.x += 0.4f;
			if(this.position.z <= this.newPosition.z+1 && this.position.z >= this.newPosition.z-1){/*in proximity*/}
			else if(this.newPosition.z <= this.position.z) this.position.z -= 0.4f;
			else this.position.z += 0.4f;

			if(!isInAir){
				if(random.nextInt(100) == 1){
					jump();
				}
			}

			upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
			this.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

			float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
			if (this.getPosition().y < terrainHeight) {
				upwardsSpeed = 0;
				isInAir = false;
				this.getPosition().y = terrainHeight;
			}
			//this.position.y = terrainHeight;
		}

	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public void setPlayer(boolean player) {
		isPlayer = player;
	}
	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
}
