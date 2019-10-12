package renderTest;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ObjLongConsumer;

public class Main {
    public static void main(String[] args){

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        // **** TERRAIN **** //

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));

        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));

        TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);

        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("pine", loader), new ModelTexture(loader.loadTexture("pine")));

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));

        grass.getTexture().setHasTransparentcy(true);
        grass.getTexture().setUseFakeLightning(true);
        flower.getTexture().setHasTransparentcy(true);
        flower.getTexture().setUseFakeLightning(true);
        fern.getTexture().setHasTransparentcy(true);
        bobble.getTexture().setHasTransparentcy(true);
        lamp.getTexture().setUseFakeLightning(true);

        Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap, "heightmap");
        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        for(int i=0; i < 400; i++){
            if(i%3==0){
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
                //entities.add(new Entity(grass, new Vector3f(x, y, z), 0,0,0,1.8f));
                //entities.add(new Entity(flower, new Vector3f(x, y, z), 0,0,0,2.3f));
            }
            if(i%2==0){
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bobble,random.nextInt(4), new Vector3f(x,y,z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
            }

        }

        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f,0.4f,0.4f)));
        lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0,0), new Vector3f(1, 0.01f,0.002f)));
        lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2,2), new Vector3f(1, 0.01f,0.002f)));
        lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2,0), new Vector3f(1, 0.01f,0.002f)));

        entities.add(new Entity(lamp, new Vector3f(185, -4.7f, -293), 0,0,0,1));
        entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300), 0,0,0,1));
        entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -305), 0,0,0,1));

        MasterRenderer renderer = new MasterRenderer(loader);

        TexturedModel person = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
        Player player = new Player(person, new Vector3f(153, 5, -274), 0,100,0,0.6f);
        Camera camera = new Camera(player);

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.8f, 0.9f), new Vector2f(0.2f, 0.3f));
        //guis.add(gui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        while(!Display.isCloseRequested()){
            player.move(terrain);
            camera.move();
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            for(Entity entity: entities) {
                renderer.processEntity(entity);
            }
            renderer.render(lights,camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }
}
