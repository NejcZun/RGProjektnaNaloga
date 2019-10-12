package renderTest;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args){

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        //Drevo
        ModelData data = OBJFileLoader.loadOBJ("tree");
        TexturedModel staticModel = new TexturedModel(loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices()),new ModelTexture(loader.loadTexture("tree")));

        //Grass
        ModelData data2 = OBJFileLoader.loadOBJ("grassModel");
        TexturedModel grass = new TexturedModel(loader.loadToVAO(data2.getVertices(), data2.getTextureCoords(), data2.getNormals(), data2.getIndices()), new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparentcy(true);
        grass.getTexture().setUseFakeLightning(true);

        //Fern
        ModelData data3 = OBJFileLoader.loadOBJ("fern");
        TexturedModel fern = new TexturedModel(loader.loadToVAO(data3.getVertices(), data3.getTextureCoords(), data3.getNormals(), data3.getIndices()), new ModelTexture(loader.loadTexture("fern")));


        //Low Poly Drevo
        ModelData data4 = OBJFileLoader.loadOBJ("lowPolyTree");
        TexturedModel tree2 = new TexturedModel(loader.loadToVAO(data4.getVertices(), data4.getTextureCoords(), data4.getNormals(), data4.getIndices()), new ModelTexture(loader.loadTexture("lowPolyTree")));

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i < 500; i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,0,0,3));
            entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,0,0,1));
            entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * - 600), 0, 0, 0, 0.6f));
            entities.add(new Entity(tree2, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 0.4f));
        }

        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));

        Terrain terrain = new Terrain(0,0,loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(1,0,loader, new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()){
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity: entities) {
                renderer.processEntity(entity);
            }
            renderer.render(light,camera);
            DisplayManager.updateDisplay();
        }

        loader.cleanUp();
        DisplayManager.closeDisplay();

    }
}
