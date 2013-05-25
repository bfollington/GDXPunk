package groups;

import au.com.voltic.gdx.Entity;
import au.com.voltic.gdx.Group;

public class Spinners extends Group {

    public Spinners(String name) {
        super(name);
    }
    
    @Override
    public void updateEntity(Entity e) {
        float rot = e.getSprite().getRotation();
        e.getSprite().setRotation(rot + 3);
    }

}
