package au.com.voltic.gdx;

import java.util.ArrayList;
import java.util.List;


import au.com.voltic.gdx.ogmo.TileLayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Container {

    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private ArrayList<TileLayer> tileLayers = new ArrayList<TileLayer>();
    private ArrayList<Container> containers = new ArrayList<Container>();
    
    protected Container parent = null;
    
    private ArrayList<Polygon> debugPolygons;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    
    public Boolean drawDebug = false;
    
    public Camera camera;
    
    private int maxLayer = 0;
    private Boolean fixedMaxLayer = false;
    
    private String name = "DEFAULT_NAME";
    
    public Container(String name) {
        this.name = name;
    }
    
    /**
     * Add an Entity
     * @param e The Entity
     */
    public void add(Entity e)
    {
        entities.add(e);
        e.added();
        e.container = this;
        e.world = getParent();
        if (e.layer > maxLayer) maxLayer = e.layer;
    }
    
    /**
     * Add a list of Entities
     * @param list
     */
    public void add(List<Entity> list)
    {
        for (Entity e : list)
        {
            if (e != null) add(e);
        }
    }
    
    /**
     * Retrieve the highest level parent of the container.
     * @return Highest level parent (a.k.a. world)
     */
    public Container getParent()
    {
        if (parent == null)
        {
            return this;
        }
        else return parent.getParent();
    }
    
    /**
     * Add a TileLayer
     * @param t The TileLayer
     */
    public void add(TileLayer t)
    {
        tileLayers.add(t);
        if (t.layer > maxLayer) maxLayer = t.layer;
    }
    
    /**
     * Add a container to this container. This is purely a superficial grouping, positions, rotations etc. are not applied recursively.
     * @param c The Container
     */
    public void add(Container c)
    {
        containers.add(c);
        c.parent = this;
        c.camera = camera;
        c.create();
        c.added();
        if (c.maxLayer > maxLayer) maxLayer = c.maxLayer;
        
    }
    
    /**
     * Set the layer of EVERY ELEMENT RECURSIVELY in the container.
     * 
     * Use with caution.
     * @param layer Layer to set to
     */
    public void setLayer(int layer)
    {
        for (Entity e : entities)
        {
            e.layer = layer;
        }
        
        for (Container c : containers)
        {
            c.setLayer(layer);
        }
        
        for (Entity t : entities)
        {
            t.layer = layer;
        }
    }
    
    /**
     * Called when a container is added to a container
     */
    public void added()
    {
        for (Entity e : entities)
        {
            e.world = getParent();
        }
    }
    
    /**
     * Remove an Entity from the container, if you want to destroy it use destroy() instead.
     * @param e The Entity
     */
    public void remove(Entity e)
    {
        entities.remove(e);
        e.world = null;
        computeMaxLayer();
    }
    
    /**
     * Completely remove an Entity from memory.
     * 
     * @param e The Entity
     */
    public void destroy(Entity e)
    {
        entities.remove(e);
        e.destroy();
        e = null;
    }
    
    /**
     * Remove a TileLayer from the container
     * @param t The TileLayer
     */
    public void remove(TileLayer t)
    {
        tileLayers.remove(t);
        computeMaxLayer();
    }
    
    /**
     * Remove a container from the container
     * @param c The Container
     */
    public void remove(Container c)
    {
        containers.remove(c);
        c.parent = null;
        computeMaxLayer();
    }
    
    /**
     * Compute the highest layer index in every child element of everything
     */
    private void computeMaxLayer()
    {
        if (!fixedMaxLayer)
        {
            maxLayer = 0;
            
            for (Entity e : entities)
            {
                if (e.layer > maxLayer) maxLayer = e.layer;
            }
            
            for (Container c : containers)
            {
                if (c.maxLayer > maxLayer) maxLayer = c.maxLayer;
            }
            
            for (Entity t : entities)
            {
                if (t.layer > maxLayer) maxLayer = t.layer;
            }
        }
    }
    
    /**
     * Called when the world is created, override this.
     */
    public void create()
    {
        
    }

    /**
     * Called once a frame, updates all children. Override this if you want.
     */
    public void update()
    {
        for (Entity e : entities)
        {
            e.update();
        }
        
        for (Container c : containers)
        {
            c.update();
        }
        
        if (drawDebug) debugPolygons = getPolygons();
    }
    
    /**
     * Draw children on a specified layer.
     * @param batch
     * @param layer
     */
    protected void drawLayer(SpriteBatch batch, int layer)
    {
        for (TileLayer t : tileLayers)
        {
            if (t.layer == layer) t.draw(batch);
        }
        
        for (Entity e : entities)
        {
            if (e.layer == layer) e.draw(batch);
        }
    }
    
    /**
     * Draw this Container and all Sub-Containers and Children
     * @param batch
     */
    public void draw(SpriteBatch batch)
    {
        for (int i = maxLayer; i >= 0; i--)
        {
            drawLayer(batch, i);
            for (Container c : containers)
            {
                c.drawLayer(batch, i);
            }
        }
        
        if (drawDebug)
        {
            batch.end();
            
            Gdx.gl.glEnable(GL10.GL_BLEND);
            Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(camera.combined);
                    
            shapeRenderer.begin(ShapeType.Line);
            for (Polygon r : debugPolygons)
            {
                shapeRenderer.setColor(0f, 1f, 0f, 0.5f);
                shapeRenderer.polygon(r.getTransformedVertices());
                shapeRenderer.setColor(1f, 0f, 1f, 0.5f);
                shapeRenderer.rect(r.getBoundingRectangle().x, r.getBoundingRectangle().y, r.getBoundingRectangle().width, r.getBoundingRectangle().height);
            }
            shapeRenderer.end();
            Gdx.gl.glDisable(GL10.GL_BLEND);
            
            batch.begin();
        }
    }
    
    /**
     * Dispose
     */
    public void dispose()
    {
        for (Entity e : entities)
        {
            e.dispose();
        }
        
        for (TileLayer t : tileLayers)
        {
            t.dispose();
        }
        
        for (Container c : containers)
        {
            c.dispose();
        }
    }
    
    protected void log(String str)
    {
        Gdx.app.log("Container", str);
    }
    
    public String toString()
    {
        return "Container: " + name;
    }
    
    /**
     * If there is a definite number of in your game, this will allow you to enforce that.
     * It also makes the remove() operation much more efficient.
     * @param layers
     */
    public void setFixedLayerCount(int layers)
    {
        maxLayer = layers;
        
        for (Container c : containers)
        {
            c.setFixedLayerCount(layers);
        }
        
        fixedMaxLayer = true;
    }
    
    /**
     * Never worry about your layer count and set it willy nilly!
     */
    public void setFlexibleLayerCount()
    {
        fixedMaxLayer = false;
        
        for (Container c : containers)
        {
            c.setFlexibleLayerCount();
        }
        
        computeMaxLayer();
    }
    
    public int getMaxLayer()
    {
        return maxLayer;
    }
    
    public Boolean maxLayerCountIsFixed()
    {
        return fixedMaxLayer;
    }
    
    /**
     * Update the RenderBounds of every TileLayer recursively.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void setRenderBounds(int x, int y, int width, int height)
    {
        for (TileLayer t : tileLayers){
            t.setRenderBounds(x, y, width, height);
        }
        
        for (Container c : containers){
            c.setRenderBounds(x, y, width, height);
        }
    }
    
    public ArrayList<Entity> getEntitiesWithinZone(Rectangle r)
    {
        return getEntitiesWithinZone(r.x, r.y, r.width, r.height);
    }
    
    /**
     * Retrieve all the entities within a certain rectangle of the world.
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public ArrayList<Entity> getEntitiesWithinZone(float x, float y, float width, float height)
    {
        ArrayList<Entity> ents = new ArrayList<Entity>();
        
        for (Entity e : entities)
        {
            if (e.x > x && e.x < width && e.y > y && e.y < height)
            {
                ents.add(e);
            }
        }
        
        for (Container c : containers)
        {
            ents.addAll(c.getEntitiesWithinZone(x, y, width, height));
        }

        return ents;
    }
    
    public ArrayList<Polygon> getPolygons()
    {
        ArrayList<Polygon> polys = new ArrayList<Polygon>();
        
        for (Entity e : entities)
        {
            if (e.hitbox != null) polys.add(e.hitbox);
        }
        
        for (Container c : containers)
        {
            polys.addAll(c.getPolygons());
        }

        return polys;
    }

}
