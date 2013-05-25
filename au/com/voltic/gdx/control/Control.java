package au.com.voltic.gdx.control;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Control {
    
    private static ArrayMap<String, Array<Axis>> axes = new ArrayMap<String, Array<Axis>>();
    private static ArrayMap<String, Array<Button>> buttons = new ArrayMap<String, Array<Button>>();

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
    }
    
    /**
     * Define a new button with a name, names can correspond to multiple buttons.
     * @param name
     * @param axis
     */
    public static void define(String name, Button button)
    {
       if (buttons.get(name) != null)
       {
           buttons.get(name).add(button);
       } else
       {
           buttons.put(name, new Array<Button>());
           
           if (!buttons.get(name).contains(button, false)) buttons.get(name).add(button);
       }
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
                if (Math.abs(a.check()) > 0.2) return a.check();
            }
        }
        
        return 0;
    }
    
    /**
     * Check the status of an button, by name
     * @param name
     * @return True if button presssed
     */
    public static Boolean checkButton(String name)
    {
        if (buttons.get(name) != null)
        {
            for (Button a : buttons.get(name))
            {
                if (a.check() != false) return a.check();
            }
        }
        
        return false;
    }
    

}
