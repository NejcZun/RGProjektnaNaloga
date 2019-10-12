package textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;

	private boolean hasTransparentcy = false;
	private boolean useFakeLightning = false;

	private int numberOfRows = 1;


	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfROws) {
		this.numberOfRows = numberOfROws;
	}

	public boolean isUseFakeLightning() {
		return useFakeLightning;
	}

	public void setUseFakeLightning(boolean useFakeLightning) {
		this.useFakeLightning = useFakeLightning;
	}


	public ModelTexture(int texture){
		this.textureID = texture;
	}

	public boolean isHasTransparentcy() {
		return hasTransparentcy;
	}

	public void setHasTransparentcy(boolean hasTransparentcy) {
		this.hasTransparentcy = hasTransparentcy;
	}

	public int getID(){
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	

}
