# jMe3GL2 - [jMonkeyEngine3 Graphics Library 2D]

> [!CAUTION]
> With the release of the official jMe3GL2 v3.1.0, this fork is now **deprecated** and no
> longer will receive updates. However, this repository will remain here, both for archive
> purposes as well as a alternative for users of the olders versions (v2.5.0 and v3.0.0),
> since the newer versions aren't compatible with the older versions.

> [!NOTE]
> This branch is intended to modify version **3.0.0** of the jMe3GL2 library. If you're looking
> for the modifications of version 2.5.0, check the [legacy branch](https://github.com/chrisGrando/jMe3GL2/tree/legacy).

jMe3GL2 is a set of classes that can be used to develop a 2D game in jMonkeyEngine3. 
It is a mapping library for JME3 to Dyn4J.

![jMe3GL2-Sample](https://github.com/chrisGrando/jMe3GL2/blob/master/modules/samples/src/main/resources/Textures/jme3gl2-sample.jpeg?raw=true)

To use jMe3GL2, prior knowledge of how the [JME3](https://jmonkeyengine.org/) graphics 
engine works, as well as the [dyn4j](https://dyn4j.org/) physics engine, is required 
to create wonderful 2D worlds.

**Some features provided by this library:**
1. Creation of 2D models using a *Sprite* mesh.
2. Integration of the Dyn4J engine through *Dyn4jAppState*.
3. Control to manage 2D animations.
4. Support for strongly customized *TileMap*
5. Support for exporting and importing 2D (binary) models.
6. Has a built-in debugger.

## Dyn4j
As mentioned, jMe3GL2 uses Dyn4J as its physics engine. If you are not familiar 
with this topic, we advise you to check out the following resources:

- **[Documentation - Dyn4j](https://dyn4j.org/pages/getting-started)**
- **[Samples - Dyn4j](https://github.com/dyn4j/dyn4j-samples)**

## Requirements
- Java 11 or higher.
- jMonkeyEngine3 version 3.8.1-stable.
- Dyn4J version 5.0.2
