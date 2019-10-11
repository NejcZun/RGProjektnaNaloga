package renderTest;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

public class Main {
    public static void main(String[] args){

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
        ModelTexture texture = staticModel.getTexture();

        Entity entity = new Entity(staticModel, new Vector3f(0,0,-25), 0, 0, 0,1);
        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));

        Terrain terrain = new Terrain(0,0,loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(1,0,loader, new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()){
            entity.increaseRotation(0,1,0);
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);

            renderer.processEntity(entity);
            renderer.render(light,camera);
            DisplayManager.updateDisplay();
        }

        loader.cleanUp();
        DisplayManager.closeDisplay();

    }
}
