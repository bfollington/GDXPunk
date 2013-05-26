package au.com.voltic.gdx;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Game {
    
    private static OrthographicCamera camera;
    private Rectangle viewport;
    private SpriteBatch batch;
    private static World world;
    
    public static int VIRTUAL_WIDTH = 0;
    public static int VIRTUAL_HEIGHT = 0;
    
    public float ACTUAL_WIDTH;
    public float ACTUAL_HEIGHT;
    public static float UNIT_WIDTH;
    public static float UNIT_HEIGHT;
    
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    
    /**
     * Called when the application is first started.
     * @param width The virtual width of the render window
     * @param height The virtual height of the render window
     */
    public void create(int width, int height) {
        VIRTUAL_WIDTH = width;
        VIRTUAL_HEIGHT = height;
        
        ACTUAL_WIDTH = Gdx.graphics.getWidth();
        ACTUAL_HEIGHT = Gdx.graphics.getHeight();
        
        camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        camera.setToOrtho(true, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        batch = new SpriteBatch();
        
    }
    
    /**
     * Called when the application ends
     */
    public void dispose()
    {
        batch.dispose();
        world.dispose();
    }
    
    /**
     * Called once every frame to draw to the screen
     */
    public void render()
    {
        update();
        
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        
        //Set viewport
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                          (int) viewport.width, (int) viewport.height);
        
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        world.draw(batch);
        
        batch.end();
    }
    
    /**
     * Called once every frame to update logic
     */
    public void update()
    {
        UNIT_WIDTH = ACTUAL_WIDTH / VIRTUAL_WIDTH;
        UNIT_HEIGHT = ACTUAL_HEIGHT / VIRTUAL_HEIGHT;
        
        world.update();
    }
    
    /**
     * Called when the application window is resized
     * @param width The new width
     * @param height The new heigth
     */
    public void resize(int width, int height)
    {
        
        ACTUAL_WIDTH = width;
        ACTUAL_HEIGHT = height;
        
        // calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }

        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
    }
    
    /**
     * Call this to change the current world
     * @param w World to change to
     */
    public static void changeWorld(World w)
    {
        world = w;
        world.camera = camera;
        world.create();
    }

}
