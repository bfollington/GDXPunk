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
    }