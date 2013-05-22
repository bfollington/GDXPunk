GDXPunk
=======

This project is incredibly young and should not be used in any games yet.

A Java Game Engine built on libGDX and inspired by Flashpunk. Want access to all the goodess libGDX supplies? Want a really simple world / entity management system? Still want to make pixel-art based games? GDXPunk.

Current Features:
- Entities
  - Static and Animated
  - Supporting Polygon/Rectangle collisions
- Nestable containers
  - Worlds are just containers
- Ogmo Editor (v2) level loading and rendering
  - Also supports (Rectangle) collisions with tilemap
- Maintains aspect ratio
- Clear pixel rendering (Nearest Neighbour)
- y-Down co-ordinate system by default

In many ways this is reducing the power of libGDX by making it structured, however you can still easily hook in to every feature of libGDX and still should for pretty much every other aspect of development.

Planned Features:
- Entities using Spine animations as their graphic
- Optional polygonal tilemap collisions for slopes etc.
- Nested containers may be redundant, possible more useful to have Scenes and Groups?
  - ```Addable``` interface to define what can be added to World?
- Better Z-Ordering implementation (World contains Layers which contain Objects?)
- Use libGDX Arrays to prevent GC problems
- Implement chunky pixel rendering using render to texture 
  - Note to self: http://stackoverflow.com/questions/7551669/libgdx-spritebatch-render-to-texture
- Text class to support adding fonts to world
- Scroll factors for layers?
- Input wrapper to define controls
  - Example: Control.define("jump", Input.Keys.A); if (Control.pressed("jump")) etc;
  - Inbuilt joypad support? (Research needed)
- ... Lots!

Example
=======

Want to display an image on screen?

    world = new Container();
    Entity e = new Entity(128, 128, "TestEntity", "data/test.png");
    world.add(e);
  
That's it.

Other Example
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
