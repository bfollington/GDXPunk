package au.com.voltic.gdx.control;

import com.badlogic.gdx.controllers.Controllers;

public class ControllerButton extends Button {

    private int button;
    private int controller;
    
    public ControllerButton(int button, int controller) {
        this.button = button;
        this.controller = controller;
    }
    
    @Override
    public Boolean check() {
        if (Controllers.getControllers().get(controller).getButton(button)) return true;
        
        return false;
    }

}
