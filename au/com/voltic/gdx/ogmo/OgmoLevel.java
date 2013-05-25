package au.com.voltic.gdx.ogmo;

import java.io.IOException;
import au.com.voltic.gdx.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class OgmoLevel {
    
    private XmlReader xmlReader;
    private Element level;
    private float offsetX, offsetY;

    public OgmoLevel(String filename, float x, float y) {
        xmlReader = new XmlReader();
        offsetX = x;
        offsetY = y;
        level = null;
        try {
            level = xmlReader.parse(Gdx.files.internal(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns a full functional TileLayer.
     * @param name Name of the layer to load
     * @param layer The destination z-ordering layer in the game
     * @return The set up TileLayer
     */
    public TileLayer loadTileLayer(String name, int layer)
    {
        Element layerNode = level.getChildByName(name);
        
        TileLayer tileLayer = new TileLayer(layerNode.getAttribute("tileset"), 16,
                level.getIntAttribute("width"),
                level.getIntAttribute("height"),
                true);
        tileLayer.loadCsvString(layerNode.getText());
        tileLayer.x = offsetX;
        tileLayer.y = offsetY;
        tileLayer.layer = layer;
        
        return tileLayer;
    }
    
    /**
     * Load an Entity Layer.
     * @param name Name of the Layer
     * @param layer Z-ordering layer for entities to be assigned.
     * @param dict The Entity lookup class used to decide which entity to create.
     * @return List of entities created.
     */
    public Array<Entity> loadEntityLayer(String name, int layer, EntityLookup dict)
    {
        Element layerNode = level.getChildByName(name);
        
        Array<Entity> res = new Array<Entity>();
        
        for (int i = 0; i < layerNode.getChildCount(); i++)
        {
            Element child = layerNode.getChild(i);

            res.add(dict.create(child));
        }
        
        return res;
    }

}
