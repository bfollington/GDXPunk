package au.com.voltic.gdx.control;

import com.badlogic.gdx.Gdx;

public class KeyboardButton extends Button {

    private int key = -1;
    
    public KeyboardButton(int key) {
        this.key = key;
    }
    
    @Override
    public Boolean check() {
        return Gdx.input.isKeyPressed(key);
    }
}
