package au.com.voltic.gdx.ogmo;

import au.com.voltic.gdx.Entity;

import com.badlogic.gdx.utils.XmlReader.Element;

public interface EntityLookup {

    /**
     * Called when an Entity is to be created, check what the Entity is and create the relevant class.
     * @param e XML Element of entity
     * @return An Actual Entity
     */
    public Entity create(Element e);

}
