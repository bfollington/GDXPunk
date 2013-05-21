GDXPunk
=======

This project is incredibly young and should not be used in any games yet.

A Java Game Engine built on libGDX and inspired by Flashpunk. Want access to all the goodess libGDX supplies? Want a really simple world / entity management system? Still want to make pixel-art based games? GDXPunk.

Features:
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

To-Do:
- Entities using Spine animations as their graphic
- Optional polygonal tilemap collisions for slopes etc.
- Nested containers may be redundant, possible more useful to have Scenes and Groups?
- Attempt better scaled rendering (chunky pixels!)
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
