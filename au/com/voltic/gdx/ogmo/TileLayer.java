package au.com.voltic.gdx.ogmo;

import java.util.Arrays;

import au.com.voltic.gdx.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class TileLayer {
    
    private int TILE_SIZE, WIDTH, HEIGHT;
    private int startRenderX, startRenderY, endRenderX, endRenderY;
    private TextureRegion[] tiles;
    private Texture tileset;
    private Boolean ydown;
    public float x, y;
    public float scrollFactorX = 0;
    public float scrollFactorY = 0;
    
    public World world = null;
    public Boolean usesScrollFactor = false;
    
    public int layer = 0;
    
    private int[][] mapData;

    /**
     * Create a new TileLayer.
     * @param tileSetFileName The name of the texture to load, relative to assets folder
     * @param tileSize The tile size in pixels
     * @param mapWidth The map width in pixels
     * @param mapHeight The map height in pixels
     */
    public TileLayer(String tileSetFileName, int tileSize, int mapWidth, int mapHeight, Boolean ydown) {
        
        TILE_SIZE = tileSize;
        WIDTH = mapWidth / TILE_SIZE;
        HEIGHT = mapHeight / TILE_SIZE;
        this.ydown = ydown;
        tiles = loadTileset(tileSetFileName, tileSize);

    }
    
    /**
     * Set the boundaries on which tiles to render (prevents rendering things out of view).
     * @param x Min X to render (pixels)
     * @param y Min Y to render (pixels)
     * @param width Min Width to render (pixels)
     * @param height Min Height to render (pixels)
     */
    public void setRenderBounds(int x, int y, int width, int height)
    {
        
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (width < 0) width = 0;
        if (height < 0) height = 0;
        
        startRenderX = Math.max(0, (int) ((x - this.x) / TILE_SIZE));
        startRenderY = Math.max(0, (int) ((y - this.y) / TILE_SIZE));
        endRenderX   = Math.max(0, (int) ((x - this.x + width) / TILE_SIZE));
        endRenderY   = Math.max(0, (int) ((y - this.y + height) / TILE_SIZE));
    }
    
    public Boolean collision(Polygon poly)
    {
        Rectangle bounds = poly.getBoundingRectangle();
        //Polygon r;
        
        float x = bounds.x / TILE_SIZE;
        float width = bounds.width / TILE_SIZE;
        float y = bounds.y / TILE_SIZE;
        float height = bounds.height / TILE_SIZE;
        
        if (x < 0 || y < 0) return false;

        for (int i = (int)x; i < Math.min(x + width, this.WIDTH); i++)
        {
            for (int j = (int)y; j < Math.min(y + height, this.HEIGHT); j++)
            {
                if (mapData[j][i] != -1)
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Load values from a CSV string, such as:
     * 
     * 1, 2, 3, 4
     * 4, 5, 2, 3
     * 3, 5, 6, 1
     * 
     * etc.
     * 
     * @param csv String to load
     */
    public void loadCsvString(String csv)
    {
        String[] rows = csv.split("\n");
        
        mapData = new int[HEIGHT][WIDTH];
        for (int[] i : mapData) Arrays.fill(i, -1);
        
        for (int i = 0; i < rows.length; i ++)
        {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < cols.length; j++)
            {
                mapData[i][j] = Integer.parseInt(cols[j].trim());
            }
        }
        
    }
    
    @Override
    public String toString()
    {
        
        String result = "";
        
        for (int i[] : mapData)
        {
           result += Arrays.toString(i);
        }
        
        return result;
    }
    
    /**
     * Render the TileLayer
     * @param batch
     */
    public void draw(SpriteBatch batch)
    {
        if (world != null && usesScrollFactor)
        {
            x = world.camera.position.x * scrollFactorX;
            y = world.camera.position.y * scrollFactorY;
        }
        
        int multi = 1;
        if (!ydown) multi = -1;
        
        for (int i = startRenderY; i < Math.min(endRenderY, HEIGHT); i++)
        {
            for (int j = startRenderX; j < Math.min(endRenderX, WIDTH); j++)
            {
                if (mapData[i][j] != -1)
                {
                    batch.draw(tiles[mapData[i][j]], j*TILE_SIZE + x, multi*i*TILE_SIZE + y);
                }
            }
        }
    }

    public void dispose()
    {
        tileset.dispose();
        System.out.println("Disposing TileLayer");
    }
    
    /**
     * Load a tileset texture from a file and slice it into an array of tiles.
     * Tiles are assigned indices based on their position in the tileset:
     * 
     * 0 1 2 3 4
     * 5 6 7 8 9 etc.
     * @param fileName The name of the file to load, from the assets folder root
     * @param tileSize Width/Height of tile in pixels
     * @return List of tiles in the tileset
     */
    private TextureRegion[] loadTileset(String fileName, int tileSize)
    {
        tileset = new Texture(Gdx.files.internal(fileName));
        tileset.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        int width = tileset.getWidth()/tileSize;
        int height = tileset.getHeight()/tileSize;
        
        TextureRegion[] result = new TextureRegion[width*height];
        
        for (int i = 0; i < width*height; i++)
        {
            
            int x = i % width;
            int y = i / width;
            
            TextureRegion tr = new TextureRegion(tileset, x*tileSize, y*tileSize, tileSize, tileSize);
            if (ydown) tr.flip(false, true);
            result[i] = tr;
            
        }
        
        return result;
    }
    

}
