package au.com.voltic.gdx.control;

import com.badlogic.gdx.controllers.Controllers;

public class ControllerButton extends Button {

    private int button;
    private int controller;
    
    public ControllerButton(int controller, int button) {
        this.button = button;
        this.controller = controller;
    }
    
    @Override
    public Boolean check() {
        if (!Control.enableJoypadInput) return false;
        if (Controllers.getControllers().size <= controller) return false;
        if (Controllers.getControllers().get(controller).getButton(button)) return true;
        
        return false;
    }

}
