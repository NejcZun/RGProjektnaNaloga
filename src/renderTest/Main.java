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

        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));

        grass.getTexture().setHasTransparentcy(true);
        grass.getTexture().setUseFakeLightning(true);
        flower.getTexture().setHasTransparentcy(true);
        flower.getTexture().setUseFakeLightning(true);
        fern.getTexture().setHasTransparentcy(true);

        Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap, "heightmap");
        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        for(int i=0; i < 400; i++){
            if(i%2==0){
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
                //entities.add(new Entity(grass, new Vector3f(x, y, z), 0,0,0,1.8f));
                //entities.add(new Entity(flower, new Vector3f(x, y, z), 0,0,0,2.3f));
            }
            if(i%5==0){
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bobble, new Vector3f(x,y,z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
                x = random.nextFloat() * 800;
                z = random.nextFloat()* -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() *1 + 4));

            }

        }

        Light light = new Light(new Vector3f(0, 20000, 20000), new Vector3f(1,1,1));


        MasterRenderer renderer = new MasterRenderer();

        TexturedModel person = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
        Player player = new Player(person, new Vector3f(100, 5, -150), 0,180,0,0.6f);
        Camera camera = new Camera(player);

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.8f, 0.9f), new Vector2f(0.2f, 0.3f));
        guis.add(gui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        while(!Display.isCloseRequested()){
            player.move(terrain);
            camera.move();
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            for(Entity entity: entities) {
                renderer.processEntity(entity);
            }
            renderer.render(light,camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }
}
