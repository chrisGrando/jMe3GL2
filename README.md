# jMe3GL2 - [jMonkeyEngine3 Graphics Library 2D]
jMe3GL2 is a set of classes that can be used to develop a 2D game in jMonkeyEngine3.
It is a mapping library for jME3 to Dyn4J.

To use jMe3GL2, prior knowledge of how the [jME3 graphics engine](https://jmonkeyengine.org/) works,
as well as the [Dyn4J physics engine](https://dyn4j.org/), is required to create wonderful 2D worlds.

**Some features provided by this library:**
1. Creation of 2D models using a Sprite mesh.
2. Integration of the Dyn4J engine through Dyn4jAppState.
3. A convert class that acts as a bridge between jME3 and Dyn4J.
4. Controls for applying physics and animations: AbstractAnimatedControl, BodyControl.

## Dyn4J
As mentioned, jMe3GL2 uses Dyn4J as its physics engine. If you are not familiar with this topic,
we advise you to check out the following resources:

* **Dyn4J Documentation** -> [https://dyn4j.org/pages/getting-started](https://dyn4j.org/pages/getting-started)
* **Dyn4J Examples** -> [https://github.com/dyn4j/dyn4j-samples](https://github.com/dyn4j/dyn4j-samples)

## Requirements

* Java **11** or higher.
* jMonkeyEngine3 version **3.8.1-stable**.
* Dyn4J version **5.0.2**.

## Project changes

* Translated jMe3GL2-awt documentation and code commentaries to English.
* Translated jMe3GL2-core documentation and code commentaries to English.
* Ported to Apache Netbeans 19.
* Minimum Java version set to 11.
* Downgraded minimum screen resolution **from** 1024x576 **to** 640x480.
* Generate *POM* file for *Apache Maven* users.
* Fixed small inconsistencies in the code.
* Added missing documentation.
* [Includes two commits](https://github.com/chrisGrando/jMe3GL2/commit/37697c53db94b5b7f88f3a2079aca224a38ee99c)
uploaded only after [version 2.5.0](https://github.com/JNightRide/jMe3GL2/releases/tag/v2.5.0)
original release was published.
