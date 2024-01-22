package jme3gl2.renderer;

import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 * Interface <code>Jme3GL2Camera</code> in charge of listing the methods for the
 * 2D fake camera manager.
 * <p>
 * The objects/classes that implement this interface are in charge of managing
 * the camera control in the game scenes, usually in a 3D world with a 2D approach.
 * </p>
 * 
 * @author wil
 * @version 1.0-SNAPSHOT 
 * 
 * @since 2.0.5
 */
public interface Jme3GL2Camera {
    
    /**
     * Method for setting a new distance for the camera, this value is used to
     * move the camera away from or towards the scene.
     * @param frustum camera distance.
     */
    public void setCameraDistanceFrustum(float frustum);
    
    /**
     * Method for preparing the camera so that it can be used in the scenes of
     * the fake 2D world.
     * <p>
     * This method is called only once, if you want to change the camera properties
     * you can use the following methods:
     * </p>
     * {@link #setProperty(java.lang.String, java.lang.Object) }
     * {@link #getProperty(java.lang.String, java.lang.Object) }
     * 
     * @param cam camera scenes.
     */
    void initialize(Camera cam);
    
    /**
     * Method responsible for updating the changes for the camera.
     * 
     * @param tpf time lapse.
     */
    void update(float tpf);
    
    /**
     * Returns a clipping object, where it is in charge of managing the camera
     * limits.
     * 
     * @return clipping object.
     */
    public Jme3GL2Clipping getClipping();
    
    /**
     * Method in charge of establishing a target to be followed by the camera in
     * the game scene.
     * 
     * @param <T> data type.
     * @param target camera target.
     */
    public <T extends Spatial> void setTarget(T target);
    
    /**
     * Sets a property to the camera handler, if it is a non-existent property,
     * it is simply set by the administrator.
     * 
     * @param <T> data type.
     * @param key key name of the property.
     * @param value new value of the property.
     * @return {@link Jme3GL2Camera}.
     */
    public <T extends Object> Jme3GL2Camera setProperty(String key, T value);
    
    /**
     * Returns the value of a property according to its key. If no such property
     * exists; the default value is returned.
     * 
     * @param <T> data type.
     * @param key key name of the property.
     * @param defaultVal default value to return.
     * @return value of the property.
     */
    public <T extends Object> T getProperty(String key, T defaultVal);
    
    /**
     * Returns the value of a property according to it's key.
     * @see Jme3GL2Camera#getProperty(java.lang.String, java.lang.Object) 
     * 
     * @param <T> data type.
     * @param key key name of the property.
     * @return value of the property.
     */
    public default <T extends Object> T getProperty(String key) {
        return getProperty(key, null);
    }
}
