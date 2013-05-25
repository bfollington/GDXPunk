package au.com.voltic.gdx;

import com.badlogic.gdx.utils.Array;

public class Group {
    
    private Array<Entity> contents = new Array<Entity>();
    private String name;

    public Group(String name) {
        this.name = name;
    }
    
    public void update()
    {
        for (int i = 0; i < contents.size; i++)
        {
            updateEntity(contents.get(i));
        }
    }
    
    public void updateEntity(Entity e)
    {
        
    }
    
    public String getName()
    {
        return name;
    }
    
    public void add(Entity e)
    {
        contents.add(e);
    }
    
    public void remove(Entity e){
        contents.removeValue(e, true);
    }
    
    public Entity get(int i)
    {
        return contents.get(i);
    }
    
    public int size()
    {
        return contents.size;
    }

}
