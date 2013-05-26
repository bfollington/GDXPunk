GDXPunk
=======

This project is incredibly young and should not be used in any games yet.

A Java Game Engine built on libGDX and inspired by Flashpunk. Want access to all the goodess libGDX supplies? Want a really simple world / entity management system? Still want to make pixel-art based games? GDXPunk.

Current Features:
- Entities
  - Static and Animated
  - Supporting Polygon/Rectangle collisions
- Grouping of entities
  - Logic can be applied to the entire group
- Ogmo Editor (v2) level loading and rendering
  - Also supports (Rectangle) collisions with tilemap
- Maintains aspect ratio for you
- Clear pixel rendering (Nearest Neighbour)
- y-Down co-ordinate system by default
- Comprehensive Z-Ordered rendering system, with layers and ordering within them
  - This is a little performance intensive due to linked-list implementation
  - Layers can have scrolling factors applied for parallax or HUD implementation
- Input
  - Static input manager that can define Keyboard and Joystick controls using names
  - Allows multiple control schemes easily and redefining of controls

In many ways this is reducing the power of libGDX by making it structured, however you can still easily hook in to every feature of libGDX and still should for pretty much every other aspect of development.

Planned Features:
- Entities using Spine animations as their graphic
- Optional polygonal tilemap collisions for slopes etc.
- Implement chunky pixel rendering using render to texture 
  - Note to self: http://stackoverflow.com/questions/7551669/libgdx-spritebatch-render-to-texture
  - Having done testing, performance is very poor with render-to-texture...
- Text class to support adding fonts to world (Extends ```BitmapFont```)

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
