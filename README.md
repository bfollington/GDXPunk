GDXPunk
=======

This project is incredibly young and should not be used in any games yet.

A Java Game Engine built on libGDX and inspired by Flashpunk. Want access to all the goodess libGDX supplies? Want a really simple world / entity management system? Still want to make pixel-art based games? GDXPunk.

Features:
- Entities
  - Static and Animated
  - Supporting Polygon collisions
- Nestable containers
  - Worlds are just containers
- Ogmo Editor (v2) level loading and rendering
- Maintains aspect ratio
- Clear pixel rendering (Nearest Neighbour)
- y-Down co-ordinate system by default

In many ways this is reducing the power of libGDX by making it structured, however you can still easily hook in to every feature of libGDX and still should for pretty much every other aspect of development.

To-Do:
- Entities using Spine animations as their graphic
- Attempt better scaled rendering (chunky pixels!)
- ... Lots!

Example
=======

Want to display an image on screen?

  world = new Container();
  Entity e = new Entity(128, 128, "TestEntity", "data/test.png");
  world.add(e);
  
That's it.
