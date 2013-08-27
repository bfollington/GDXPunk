GDXPunk
=======

This project is incredibly young and should not be used in any games yet.

A Java Game Engine built on libGDX and inspired by Flashpunk. Want access to all the goodess libGDX supplies? Want a really simple world / entity management system? Still want to make pixel-art based games? GDXPunk.

**GDXPunk does not currently support Android (though, you may be able to get it to work) and has no plans to.**

Current Features:
- ```Entity```
  - Static Image or Animated
  - Supporting Polygon/Rectangle collisions
- ```Group```
  - Collection of Entities
  - Logic can be applied to the entire group
  - Useful for HUD etc.
- ```OgmoLevel```
  - Ogmo Editor (v2) level loading and rendering
  - Supports (Rectangle) collisions with tilemap
- ```Game```
  - Maintains aspect ratio for you
  - Clear pixel rendering (Nearest Neighbour)
  - y-Down co-ordinate system by default
  - Handles resolution etc.
- ```World```
  - Comprehensive Z-Ordered rendering system, with layers and ordering within them
    - This is a little performance intensive due to linked-list implementation
    - Layers can have scrolling factors applied for parallax or HUD implementation
- ```Control```
  - Static input manager that can define Keyboard and Joystick controls using names
  - Allows multiple control schemes easily and redefining of controls

Notes:
- Projects MUST have the Gdx-Controllers extension added to them, see: http://www.badlogicgames.com/wordpress/?p=2743 for set up
- In the desktop project the following parts of cfg should be defined:

        cfg.width = 960;
        cfg.height = 640;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = -1;

- They do not have to use these values, but without an FPS limit the engine will go crazy fast

In many ways this is reducing the power of libGDX by making it structured, however you can still easily hook in to every feature of libGDX and still should for pretty much every other aspect of development.

Planned Features:
- ```Entities```
  - Use Spine animations as graphic
  - Use Rectangle as graphic
  - Use Circle as graphic
  - Use Line as graphic
  - Creating animations from a sequence of single images
  - Loading textures from atlases (for Entities or TileLayers)
- ```TileLayers```
  - Optional polygonal tilemap collisions for slopes etc.
- Implement chunky pixel rendering using render to texture 
  - Note to self: http://stackoverflow.com/questions/7551669/libgdx-spritebatch-render-to-texture
  - Having done testing, performance is very poor with render-to-texture...
- Add ```Text``` class to support adding fonts to world (Extends ```BitmapFont```)
- Look into optimised collision handling (perhaps ```Entity```s have a collided() function?)

Entity Example
=======

Want to display an image on screen?

    world = new World();
    Entity e = new Entity(128, 128, "TestEntity", "data/test.png");
    world.add(e);
  
That's it.

Ogmo Editor Integration
=============

Want to load an ogmo level?

    //Create the level with origin 0, 0 (Top Left)
    OgmoLevel ogmo = new OgmoLevel("data/level1.oel", 0, 0);
    
    //Load ground with Z-Layer 1
    tileLayer = ogmo.loadTileLayer("ground", 1);
    add(tileLayer);
    
    //Load all entities with Z-Layer 2
    //GDXPunk uses Entity Lookup dictionary classes to decode the information
    add(ogmo.loadEntityLayer("entities", 2, new TestEntityLookup()));
        
Input Management
================
        
    //In create()
    Control.define("jump", new KeyboardButton(Keys.A));
    Control.define("jump", new ControllerButton(0, 1);
    
    //In update()
    if (Control.checkButton("jump")) jump();
    
    
Game Example
=============

Want to make a new game?

    public class MyGame extends Game implements ApplicationListener {
        
        @Override
        public void create() 
      	{
      	    super.create(480, 320);
      		
              changeWorld(new TestWorld());
      	}
      
      	@Override
      	public void dispose()
      	{
      	    super.dispose();
      	}
      
      	@Override
      	public void render()
      	{		
      	    super.render();
      	}
      	
      	@Override
      	public void update()
      	{
      	    super.update();
      	}
      
      	@Override
      	public void resize(int width, int height) {
      	    super.resize(width, height);
      	}
      
      	@Override
      	public void pause() {
      	}
      
      	@Override
      	public void resume() {
      	}
    }
