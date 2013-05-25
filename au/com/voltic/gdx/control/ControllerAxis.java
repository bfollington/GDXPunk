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
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + axis;
            result = prime * result + controller;
            result = prime * result + neg;
            result = prime * result + pos;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ControllerAxis other = (ControllerAxis) obj;
            if (axis != other.axis)
                return false;
            if (controller != other.controller)
                return false;
            if (neg != other.neg)
                return false;
            if (pos != other.pos)
                return false;
            return true;
        }

        public String toString()
        {
            if (axis != -1) return "Controller: " + axis + " Axis: " + axis;
            else return "Pos: " + pos + " Neg: " + neg;
        }
    }