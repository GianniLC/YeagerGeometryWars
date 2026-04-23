# Drawing Guide for Yaeger Geometry Wars

This document explains how to draw entities on the screen using the Yaeger game engine.

## How to Draw Things on Screen

Drawing in Yaeger follows a pattern:
1. **Create an Entity class** that extends a Yaeger entity type
2. **Configure the entity** in its constructor
3. **Add the entity to a scene** in the scene's `setupEntities()` method

### Available Entity Types

Yaeger provides several built-in entity types you can extend:

- `CircleEntity` - For drawing circles
- `RectangleEntity` - For drawing rectangles/squares
- `TextEntity` - For drawing text
- `SpriteEntity` - For drawing images

## Understanding `super(initialLocation)`

The `super()` call is a Java keyword that calls the constructor of the parent class.

### What It Does

When you write:
```java
public class Player extends CircleEntity {
    public Player(Coordinate2D initialLocation) {
        super(initialLocation);  // <- This line
        // ... configure the entity
    }
}
```

The `super(initialLocation)` line:
1. **Calls the parent class constructor** - In this case, it calls `CircleEntity(Coordinate2D)`
2. **Passes the location parameter up** - Tells the Yaeger engine WHERE to draw this entity
3. **Must be the first line** - Java requires super() to be called before any other code in the constructor
4. **Initializes the entity** - Sets up the internal Yaeger machinery that makes the entity drawable

### Why We Need It

Without `super(initialLocation)`, the parent class (`CircleEntity`, `RectangleEntity`, etc.) wouldn't know:
- Where to position the entity on screen
- How to set up the underlying JavaFX node
- How to register the entity with the game engine

Think of it as: **"Hey parent class, initialize yourself at this location, then I'll customize my appearance."**

## Example: Creating a Circle Enemy

Here's what happens step-by-step:

```java
public class CircleEnemy extends CircleEntity {
    
    public CircleEnemy(Coordinate2D initialLocation) {
        // Step 1: Call parent constructor - sets up base entity at coordinates
        super(initialLocation); 
        
        // Step 2: Customize the circle's appearance
        setRadius(15);              // How big the circle is
        setFill(Color.RED);         // Fill color
        setStrokeColor(Color.DARKRED);  // Border color
        setStrokeWidth(2);          // Border thickness
    }
}
```

When you create it:
```java
var circleEnemy = new CircleEnemy(new Coordinate2D(1000, 520));
addEntity(circleEnemy);
```

1. `new CircleEnemy(...)` creates the object
2. Constructor calls `super(initialLocation)` → parent sets up entity at (1000, 520)
3. Then your customization methods run (setRadius, setFill, etc.)
4. `addEntity()` registers it with the scene to be drawn

## Common Entity Properties

All entities support these customization methods:

### Position & Size
- Circles: `setRadius(double)`
- Rectangles: `setWidth(double)`, `setHeight(double)`
- Any entity: `setRotate(double)` - rotation in degrees

### Colors
- `setFill(Color)` - Fill color
- `setStrokeColor(Color)` - Border/outline color
- `setStrokeWidth(double)` - Border thickness

### Text-Specific
- `setFont(Font)` - Font style and size
- `setAnchorPoint(AnchorPoint)` - How text is positioned relative to coordinates

## Creating a New Entity Type

Follow this pattern:

```java
package SjoerdGianni.org.entities;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.CircleEntity;  // or RectangleEntity, etc.
import javafx.scene.paint.Color;

public class MyNewEntity extends CircleEntity {  // Choose appropriate parent
    
    public MyNewEntity(Coordinate2D initialLocation) {
        super(initialLocation);  // REQUIRED: Initialize parent at location
        
        // Customize appearance:
        setRadius(25);
        setFill(Color.BLUE);
        setStrokeColor(Color.CYAN);
        setStrokeWidth(3);
    }
}
```

Then in your scene:
```java
@Override
public void setupEntities() {
    var myEntity = new MyNewEntity(new Coordinate2D(640, 360));
    addEntity(myEntity);
}
```

## Coordinate System

- Origin (0, 0) is the **top-left corner** of the window
- X increases to the **right**
- Y increases **downward**
- Our game window is **1280 x 720** pixels
- Center of screen is **(640, 360)**

## Labels and Text

To add text labels:

```java
var label = new TextEntity(new Coordinate2D(640, 390), "[player]");
label.setAnchorPoint(AnchorPoint.CENTER_CENTER);  // Center the text
label.setFill(Color.WHITE);
label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
addEntity(label);
```