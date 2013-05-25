package au.com.voltic.gdx.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;

    public class KeyboardAxis extends Axis
    {
        private int pos = -1;
        private int neg = -1;
        
        public KeyboardAxis(int positiveKey, int negativeKey)
        {
            this.pos = positiveKey;
            this.neg = negativeKey;
        }
        
        @Override
        public float check()
        {
            if (Gdx.input.isKeyPressed(pos)) return 1;
            if (Gdx.input.isKeyPressed(neg)) return -1;
            
            return 0;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
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
            KeyboardAxis other = (KeyboardAxis) obj;
            if (neg != other.neg)
                return false;
            if (pos != other.pos)
                return false;
            return true;
        }

        public String toString()
        {
            return "Pos: " + pos + " Neg: " + neg;
        }
    }