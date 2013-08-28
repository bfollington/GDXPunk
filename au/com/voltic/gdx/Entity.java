package au.com.voltic.gdx;

import java.util.ArrayList;

import au.com.voltic.gdx.ogmo.TileLayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * This is intended to closely match the functionality of the Entity class available in Flashpunk.
 * 
 * It can slice up sprite sheets, define animation sequences and play them.
 * 
 * It also supports basic collision handling
 * 
 * @author Ben Follington
 *
 */
public class Entity {
    
    private Texture spritesheet = null;
    private TextureRegion[] frames;
    private Sprite sprite;
    public float x = 0;
    public float y = 0;
    private String name = "";
    public String type = "";
    /** Using a y-Down co-ordinate system like THE REST OF THE WORLD? */
    public Boolean ydown = true;
    
    //Rendering
    public Entity renderNext = null;
    public Entity renderPrev = null;
    
    //Animation
    private int frameTicker = 0;
    private ObjectMap<String, Animation> animations = new ObjectMap<String, Animation>();
    private String currentAnimation = "";
    private int currentFrame = 0;
    private Boolean animationPlaying = true;
    private Texture providedTexture = null;
    
    public Boolean visible = true;
    //When this is on the Entity declares it's activities to the console
    public Boolean verbose = false;
    
    //Collision
    protected Polygon hitbox = null;
    private int hitboxOffsetX, hitboxOffsetY;
    protected Rectangle collisionZone = new Rectangle(0, 0, 256, 256);
    protected Boolean hitboxRotates = false;
    //Layers to collide with
    private ArrayList<TileLayer> collision = new ArrayList<TileLayer>();
    
    //World
    private int layer = 0;
    public World world;
    
    public Entity() {
        this(0, 0, "");
    }
    
    public Entity(float x, float y)
    {
        this(x, y, "");
    }

    public Entity(float x, float y, String name)
    {
        this(x, y, name, null);
    }
    
    public Entity(float x, float y, String name, String texture)
    {
        this(x, y, name, texture, false);
    }
    
    public Entity(float x, float y, String name, String texture, Boolean debug)
    {
        this.x = x;
        this.y = y;
        this.name = name;
        
        if (texture != null)
        {
            providedTexture = new Texture(Gdx.files.internal(texture));
            animationPlaying = false;
        }
        verbose = debug;
        
        create();
        log(name + " initialised");
    }
    
    /**
     * Called immediately when class instantiated.
     */
    private void create()
    {
        log("Created");
        sprite = new Sprite();
        log("Init Sprite");
        
        if (providedTexture != null)
        {
            log("Provided texture.");
            
            TextureRegion ptr = new TextureRegion(providedTexture);
            if (ydown) ptr.flip(false, true);
            
            sprite.setRegion(ptr);
            sprite.setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
            sprite.setPosition(x, y);
            log("Set provided texture: " + sprite.getWidth() + "   " + sprite.getHeight());
        }
    }
    
    public void setOrigin(float x, float y, Boolean updateCollider)
    {
        getSprite().setOrigin(x, y);
        if (updateCollider) hitbox.setOrigin(x - hitboxOffsetX, y - hitboxOffsetY);
    }
    
    /**
     * Set the size of the collision checking zone surrounding the entity.
     * @param width
     * @param height
     */
    public void setCollisionBox(float width, float height)
    {
        collisionZone.width = width;
        collisionZone.height = height;
    }
    
    /**
     * Check if this Entity collides with a specific other entity
     * @param e
     * @return
     */
    public Boolean collide(Entity e)
    {
        return collide(e, 0, 0);
    }
    
    /**
     * Check for a collision with a specific Entity with an offset.
     * @param e
     * @param x
     * @param y
     * @return
     */
    public Boolean collide(Entity e, float x, float y)
    {
        Polygon p;
        if (x != 0 || y != 0)
        {
            p = new Polygon(hitbox.getTransformedVertices());
            p.setPosition(p.getX() + x, p.getY() + y);
        } else {
            p = hitbox;
        }
        
        if (Intersector.overlapConvexPolygons(e.hitbox, p))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Collide with entities of a type with an offset.
     * @param type
     * @param x
     * @param y
     * @return
     */
    public Entity collide(String type, float x, float y)
    {
        Polygon p;
        if (x != 0 || y != 0)
        {
            p = new Polygon(hitbox.getTransformedVertices());
            p.setPosition(p.getX() + x, p.getY() + y);
        } else {
            p = hitbox;
        }
        
        ArrayList<Entity> ents = world.getEntitiesWithinZone(collisionZone);
        
        for (Entity e : ents)
        {
            if (e.type != type) continue;
            
            if (Intersector.overlapConvexPolygons(p, e.hitbox)){
                return e;
            }
        }
        
        return null;  
    }
    
    //TODO Collide with a list of types
    
    public Entity collide(ArrayList<String> listOfTypes)
    {
        for (String s : listOfTypes)
        {
            collide(s, 0, 0);
        }
        
        return null;
    }
    
    //TODO fix JavaDoc
    /**
     * Perform a collision by name (looking for a specific entity) within a zone. Use zone with caution to prevent excessive checking.
     * @param name The name of the entity to attempt collision with
     * @param x Start X of Zone
     * @param y Start Y of Zone
     * @param width Width of Zone
     * @param height Height of Zone
     * @return Which entity matched first
     */
    public Entity collide(String type)
    {
        return collide(type, 0, 0);
    }
    
    /**
     * Perform a collision by name (looking for a specific entity) within a zone. Use zone with caution to prevent excessive checking.
     * @param name The name of the entity to attempt collision with
     * @param x Offset x
     * @param y Offset y
     * @return All matching entities
     */
    public ArrayList<Entity> collideList(String type, float x, float y)
    {
        Polygon p;
        if (x != 0 || y != 0)
        {
            p = new Polygon(hitbox.getTransformedVertices());
            p.setPosition(p.getX() + x, p.getY() + y);
        } else {
            p = hitbox;
        }
        
        ArrayList<Entity> ents = world.getEntitiesWithinZone(collisionZone);
        ArrayList<Entity> result = new ArrayList<Entity>();
        
        for (Entity e : ents)
        {
            if (e.type != type) continue;
            
            if (Intersector.overlapConvexPolygons(p, e.hitbox)){
                result.add(e);
            }
        }
        
        return result;
    }
    
    /**
     * Collide against a type of entity.
     * @param type
     * @return
     */
    public ArrayList<Entity> collideList(String type)
    {
        return collideList(type, 0, 0);
    }
    
    /**
     * Collide the Entity with a TileLayer
     * @param tl
     * @return
     */
    public Boolean collide(TileLayer tl)
    {     
        return collide(tl, 0, 0);
    }
    
    /**
     * Collide with a TileLayer with a given offset to the hitbox (allows for anticipated collision).
     * @param tl
     * @param xOffset
     * @param yOffset
     * @return
     */
    public Boolean collide(TileLayer tl, float xOffset, float yOffset)
    {
        
        if (hitbox == null)
        {
            System.err.println("Hitbox not defined for entity " + name);
            return false;
        }
        
        Polygon newRect = new Polygon(hitbox.getTransformedVertices());
        newRect.setPosition(xOffset, yOffset);
        
        return tl.collision(newRect);
    }
    
    /**
     * Check all the tile layers in the collision list for a collision.
     * @return
     */
    public Boolean collideTileLayers()
    {
        return collideTileLayers(0, 0);
    }
    
    /**
     * Check all the tile layers in the collision list for a collision.
     * @param xOffset
     * @param yOffset
     * @return
     */
    public Boolean collideTileLayers(float xOffset, float yOffset)
    {
        for (TileLayer tl : collision)
        {
            if (collide(tl, xOffset, yOffset)) return true;
        }
        
        return false;
    }
    
    /**
     * Add a layer to the collision list for an Entity
     * @param tl
     */
    public void addCollisionLayer(TileLayer tl)
    {
        collision.add(tl);
    }
    
    /**
     * Remove a layer from the collision list for an Entity
     * @param tl
     */
    public void removeCollisionlayer(TileLayer tl)
    {
        collision.remove(tl);
    }
    
    /**
     * Called when an Entity is added to the world
     */
    public void added()
    {

    }
    
    /**
     * Update the texture of the Entity
     * @param texture New texture
     */
    protected void setSpriteSheet(String path)
    {
        log("Set Spritesheet");
        spritesheet = new Texture(Gdx.files.internal(path));
        spritesheet.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }
    
    /**
     * Get the Texture of the Sprite (this is the Spritesheet of an animated sprite).
     * @return Texture
     */
    protected Texture getSpriteSheet()
    {
        return spritesheet;
    }
    
    /**
     * Use after calling setSpriteSheet(), specify the dimensions to slice
     * the texture into. Will store frames internally.
     * @param width 
     * @param height
     */
    protected void createFrames(int width, int height)
    {
        sprite.setSize(width, height);
        
        log("Creating frames");
        
        if (getSpriteSheet() == null)
        {
            System.err.println("No SpriteSheet to create frames from.");
            return;
        }
        
        int rowCount = getSpriteSheet().getWidth() / width;
        int colCount = getSpriteSheet().getHeight() / height;
        
        frames = new TextureRegion[rowCount * colCount];
        
        for (int i = 0; i < rowCount*colCount; i++)
        {
            
            int x = i % rowCount;
            int y = i / colCount;
            
            TextureRegion tr = new TextureRegion(getSpriteSheet(), x*width,
                                                              y*height,
                                                              width,
                                                              height);
            
            if (ydown) tr.flip(false, true);
            
            frames[i] = tr;
            
        }
    }
    
    /**
     * Add a new animation definition.
     * @param name Name of animation (Will overwrite old animations named this).
     * @param frames Array of frames indices (new int[]{0, 1, 2}).
     * @param speed How many frames of rendering -> one frame of animation (lower = faster)
     * @param loop Should the animation loop?
     */
    protected void addAnimation(String name, int[] frames, int speed, Boolean loop)
    {
        log("Adding animation: " + name);
        animations.put(name, new Animation(frames, speed, loop));
    }
    
    /**
     * Change the animation of the Entity
     * @param name Animation name
     */
    protected void setAnimation(String name)
    {
        log("Setting animation: " + name);
        if (animations.containsKey(name))
        {
            currentAnimation = name;
            sprite.setRegion(frames[animations.get(currentAnimation).frames[0]]);
        }
        else System.err.println(this.name + ": No such animation: " + name);
    }
    
    /**
     * Called once a frame, override to add logic but ensure to all super.update();
     */
    public void update()
    {
        sprite.setPosition(x, y);
        collisionZone.setX(x - collisionZone.width / 2);
        collisionZone.setY(y - collisionZone.height / 2);
        
        if (hitbox != null)
        { 
            hitbox.setPosition(x + hitboxOffsetX, y + hitboxOffsetY);
        }
        
        if (animationPlaying)
        {
            Animation current = animations.get(currentAnimation);
            frameTicker++;
        
            //Increment Frame
            if (frameTicker > current.speed)
            {
                frameTicker = 0;
                currentFrame++;
            }
            
            //Loop animation or stop playing
            if (currentFrame >= current.frameCount)
            {
                if (current.loops) currentFrame = 0;
                else animationPlaying = false;
            }

            //Change actual image
            try {
                sprite.setRegion(frames[current.frames[currentFrame]]);
            } catch (NullPointerException e) {
                System.err.println(name + ": Frame " + currentFrame + " does not exist!");
            }
        }
    }
    
    /**
     * Draws the sprite to the screen
     * @param batch
     */
    public void draw(SpriteBatch batch)
    {
        sprite.draw(batch);
    }
    
    /**
     * Called when an Entity is removed
     */
    public void removed()
    {
        
    }
    
    /**
     * Specify the dimensions of the hitbox.
     * @param xoffset
     * @param yoffset
     * @param width
     * @param height
     */
    public void setHitbox(int xoffset, int yoffset, int width, int height)
    {
        hitboxOffsetX = xoffset;
        hitboxOffsetY = yoffset;
        hitbox = new Polygon(new float[]{0, 0,
                                         width, 0,
                                         width, height,
                                         0, height});
    }
    
    public void setPolygonCollider(int xoffset, int yoffset, Polygon p)
    {
        hitboxOffsetX = xoffset;
        hitboxOffsetY = yoffset;
        hitbox = p;
    }
    
    public Sprite getSprite()
    {
        return sprite;
    }
    
    protected void log(String str)
    {
        if (verbose) Gdx.app.log(name, str);
    }
    
    public void dispose()
    {
        log("Disposing");
        if (spritesheet != null) spritesheet.dispose();
        if (sprite.getTexture() != null) sprite.getTexture().dispose();
    }
    
    public void setLayer(int layer)
    {
        if (world == null)
        {
            this.layer = layer;
            return;
        }
        
        log("Setting layer to: " + layer + " My world is " + world);
        
        world.removeEntityFromLayer(this, this.layer);
        this.layer = layer;
        
        log("Adjust linked list");
        
        if (renderPrev != null && this.renderNext.renderPrev != null)
            this.renderNext.renderPrev = this.renderPrev;

        if (renderNext != null && this.renderPrev.renderNext != null)
            this.renderPrev.renderNext = this.renderNext;   
        log("Set rendering lists");
        
        renderNext = null;
        renderPrev = null;
        
        world.setUpRendering(this);
    }
    
    public int getLayer()
    {
        return layer;
    }

    
    public void destroy()
    {
        world.destroy(this);
        dispose();
    }
    
    public String toString()
    {
        return name;
    }
    
}
