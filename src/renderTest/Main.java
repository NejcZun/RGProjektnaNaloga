package renderTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class Main {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        //PLAYER
        RawModel person = OBJLoader.loadObjModel("person", loader);
        TexturedModel personModel = new TexturedModel(person, new ModelTexture(loader.loadTexture("playerTexture")));




        Player player = new Player(personModel, new Vector3f(300, 5, -400), 0, 100, 0, 0.6f);
        Camera camera = new Camera(player);
        MasterRenderer renderer = new MasterRenderer(loader, camera);

        //TERRAIN

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grassy3"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);
        // *****************************************
        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader), fernTextureAtlas);
        fern.getTexture().setHasTransparency(true);

        TexturedModel pineModel = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader), new ModelTexture(loader.loadTexture("pine")));
        pineModel.getTexture().setHasTransparency(true);


        TexturedModel cherry = new TexturedModel(OBJFileLoader.loadOBJ("cherry", loader), new ModelTexture(loader.loadTexture("cherry")));
        pineModel.getTexture().setHasTransparency(true);

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lantern", loader), new ModelTexture(loader.loadTexture("lantern")));
        lamp.getTexture().setUseFakeLighting(true);

        TexturedModel bunny = new TexturedModel(OBJLoader.loadObjModel("bunny", loader), new ModelTexture(loader.loadTexture("white")));

        List<Entity> entities = new ArrayList<>();
        List<Entity> movingEntities = new ArrayList<>();
        List<Entity> normalMapEntities = new ArrayList<>();

        //******************NORMAL MAP MODELS************************

        TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
                new ModelTexture(loader.loadTexture("crate")));
        crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
        crateModel.getTexture().setShineDamper(10);
        crateModel.getTexture().setReflectivity(0.5f);

        TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
                new ModelTexture(loader.loadTexture("boulder")));
        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);


        //ENTITETE
        Random random = new Random(5666778);
        for (int i = 0; i < 320; i++) {
            if (i % 3 == 0) {
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -800;
                float y = terrain.getHeightOfTerrain(x, z);
                if(y > 0) entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
            }
            if (i % 5 == 0) {

                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -800;
                float y = terrain.getHeightOfTerrain(x, z);
                if(y > 0){
                    entities.add(new Entity(cherry, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 2f + 2f));
                }

            }
            float x = random.nextFloat() * 800;
            float z = random.nextFloat() * -800;
            float y = terrain.getHeightOfTerrain(x, z);
            if(y > 0){
                entities.add(new Entity(pineModel, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 1f + 1f));

            }

        }
        for(int i=0; i < 30; i++){
            float x = 400 + random.nextFloat() * 400;
            float z = -400 + random.nextFloat() * 400;
            float y = terrain.getHeightOfTerrain(x, z);
            normalMapEntities.add(new Entity(boulderModel, new Vector3f(x, y,z), 180, random.nextFloat() * 360, 0, 0.5f + random.nextFloat()));
        }

        for(int i=0; i < 100;i++){
            float x = 400 + random.nextFloat() * 600;
            float z = -400 + random.nextFloat() * 600;
            float y = terrain.getHeightOfTerrain(x, z);
            if(i%2 == 0 && y>0) {
                entities.add(new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0, 1));
            }
        }
        for(int i=0; i<10; i++){
            float x = 400 + random.nextFloat() * 400;
            float z = -400 + random.nextFloat() * 400;
            float y = terrain.getHeightOfTerrain(x, z);
            movingEntities.add(new Entity(bunny, new Vector3f(x, y, z), 0, 0, 0, 0.5f));
        }

        //THE WALL
        for(int i=0;i<9;i++){
            entities.add(new Entity(crateModel, new Vector3f(i*100, 0, 48), 0, 0, 0, 0.5f));
            entities.add(new Entity(crateModel, new Vector3f(i*100, 0, -848), 0, 0, 0, 0.5f));
        }
        for(int i=0;i>-9;i--){
            entities.add(new Entity(crateModel, new Vector3f(-48, 0, i*100), 0, 0, 0, 0.5f));
            entities.add(new Entity(crateModel, new Vector3f(848, 0, i*100), 0, 0, 0, 0.5f));
        }

        List<Light> lights = new ArrayList<>();
        Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);
        player.setPlayer(true);
        entities.add(player);

        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
        GuiRenderer guiRenderer = new GuiRenderer(loader);


        //ZA IZRIS SHADOW MAPA - mid building

        //GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
        //guiTextures.add(shadowMap);

        //WATER

        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<>();
        for(int i=1; i < 5;i++){
            for(int j=1;j<5;j++) waters.add(new WaterTile(i * 160, -j *160, -1));
        }

        //LOOP

        while (!Display.isCloseRequested()) {
            player.move(terrain, entities, normalMapEntities, movingEntities);
            camera.move();

            renderer.renderShadowMap(entities, sun);
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, normalMapEntities, movingEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -waters.get(0).getHeight()+1));
            camera.getPosition().y += distance;
            camera.invertPitch();

            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, movingEntities, terrains, lights, camera, new Vector4f(0, -1, 0, waters.get(0).getHeight() + 0.2f));

            //make the bunnies move
            for(Entity entity : movingEntities){
                float terrainHeight = terrain.getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z);
                entity.getPosition().y = terrainHeight;
                entity.movement(terrain);
            }
            //render to screen
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            buffers.unbindCurrentFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, movingEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);
            guiRenderer.render(guiTextures);

            DisplayManager.updateDisplay();
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }


}
