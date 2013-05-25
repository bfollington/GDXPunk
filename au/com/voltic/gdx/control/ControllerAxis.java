package au.com.voltic.gdx.control;

import com.badlogic.gdx.controllers.Controllers;

    public class ControllerAxis extends Axis
    {
        
        private int controller;
        private int axis = -1;
        private int pos = -1;
        private int neg = -1;
        
        public ControllerAxis(int controller, int axis)
        {
            this.controller = controller;
            this.axis = axis;
        }
        
        public ControllerAxis(int controller, int positive, int negative)
        {
            this.controller = controller;
            this.pos = positive;
            this.neg = negative;
        }
        
        @Override
        public float check()
        {
            if (axis != -1) return Controllers.getControllers().get(controller).getAxis(axis);
            else {
                if (Controllers.getControllers().get(controller).getButton(pos)) return 1;
                if (Controllers.getControllers().get(controller).getButton(neg)) return -1;
            }
            
            return 0;
        }
    }