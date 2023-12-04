# jMe3GL2 - [jMonkeyEngine3 Graphics Library 2D]
jMe3GL2 is a set of classes that can be used to develop a 2D game in jMonkeyEngine3.
It is a mapping library for jME3 to Dyn4J.

To use jMe3GL2, prior knowledge of how the [jME3 graphics engine](https://jmonkeyengine.org/) works,
as well as the [Dyn4J physics engine](https://dyn4j.org/), is required to create wonderful 2D worlds.

**Some features provided by this library:**
1. Creation of 2D models using a Sprite mesh.
2. Integration of the Dyn4J engine through Dyn4jAppState.
3. A Convert class that acts as a bridge between jME3 and Dyn4J.
4. Controls for applying physics and animations: AbstractAnimatedControl, BodyControl.

## Dyn4J
As mentioned, jMe3GL2 uses Dyn4J as its physics engine. If you are not familiar with this topic,
we advise you to check out the following resources:

* **Dyn4J Documentation** -> [https://dyn4j.org/pages/getting-started](https://dyn4j.org/pages/getting-started)
* **Dyn4J Examples** -> [https://github.com/dyn4j/dyn4j-samples](https://github.com/dyn4j/dyn4j-samples)
 
## Project changes
This fork has or is aiming to apply the following changes:

### Already implemented
* Ported to Apache Netbeans 19.
* Downgraded minimum screen resolution **from** 1024x576 **to** 640x480.
* Generate *pom.xml*.

### Working in progress
* Translating jMe3GL2-awt to English.
* Translating jMe3GL2-core to English.
