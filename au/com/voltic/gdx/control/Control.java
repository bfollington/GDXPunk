package au.com.voltic.gdx.control;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Control {
    
    private static ArrayMap<String, Array<Axis>> axes = new ArrayMap<String, Array<Axis>>();

    public Control() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Define a new axis with a name, names can correspond to multiple axes.
     * @param name
     * @param axis
     */
    public static void define(String name, Axis axis)
    {
       if (axes.get(name) != null)
       {
           axes.get(name).add(axis);
       } else
       {
           axes.put(name, new Array<Axis>());
           
           if (!axes.get(name).contains(axis, false)) axes.get(name).add(axis);
       }
       
       System.out.println(axes);
    }
    
    /**
     * Check the value of an axis, by name
     * @param name
     * @return The value of the axis form -1 to 1
     */
    public static float checkAxis(String name)
    {
        if (axes.get(name) != null)
        {
            for (Axis a : axes.get(name))
            {
                if (Math.abs(a.check()) > 0) return a.check();
            }
        }
        
        return 0;
    }
    

}
