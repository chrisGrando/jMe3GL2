package jme3gl2.renderer;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Map;

/**
 * Class <code>AbstractjMe3GL2camera</code> in charge of implementing the interface
 * {@link Jme3GL2Camera} where a template for property management is prepared.
 * <p>
 * This class only provides a template for managing the camera in scenes.
 * </p>
 * 
 * @author wil
 * @version 1.0-SNAPSHOT 
 * 
 * @since 2.0.5
 */
@SuppressWarnings(value = {"unchecked"})
public abstract 
class AbstractJme3GL2camera implements Jme3GL2Camera {
    
    /**
     * Internal class <code>Target</code> in charge of managing the camera lens.
     * <p>
     * This is used to keep track of camera displacement during gameplay.
     * 
     * To prevent the camera from moving at the point <code>(0,0,0)</code> by
     * removing its target.
     * </p>
     * 
     * @param <E> type of target
     * @version 1.5.0
     */
    protected static 
    class Target<E extends Spatial> {
        
        /** Target. */
        private E value;
        
        /**
         * Registration of camera displacement during game execution.
         * 
         * <p>
         * This is used when there is no target set by the user.
         * </p>
         */
        private Vector3f aux;
        
        /**
         * Default constructor of the inner class <code>Target</code> where the
         * objects are initialized for the operation of this new object.
         */
        public Target() {
            this.aux = new Vector3f();
        }

        /**
         * Method for establishing a new target.
         * @param value new target.
         */
        public void setValue(E value) {
            if (value == null && this.value != null) {
                this.aux = this.value.getLocalTranslation().clone();
            }
            this.value = value;
        }
        
        /**
         * Returns the position of the current target, otherwise it will be the
         * record of it.
         * 
         * @param isWorld <code>true</code> if the world/global position of the
         * target is to be obtained, otherwise <code>false</code> for
         * local/relative position.
         * 
         * @return translation of the target.
         */
        public Vector3f getLocalTranslation(boolean isWorld) {
            if (this.value == null) {
                return this.aux;
            }
            return (isWorld) ? this.value.getWorldTranslation() : this.value.getLocalTranslation();
        }
        
        /**
         * Returns the target if there is one, otherwise the value is
         * <code>null</code>.
         * @return target.
         */
        public E getValue() {
            return value;
        }
    }
    
    /**
     * Attribute map for camera management.
     */
    private final Map<String, Object> attributes;
    
    /** Object / Target. */
    protected final Target<Spatial> target;

    /**
     * Class constructor <code>AbstractjMe3GL2camera</code> where the
     * properties/attributes of this instantiated object are initialized.
     */
    public AbstractJme3GL2camera() {
        this.attributes = new HashMap<>();
        this.target     = new Target<>();
    }

    /**
     * (non-JavaDoc)
     * @see Jme3GL2Camera#setTarget(com.jme3.scene.Spatial) 
     */
    @Override
    public <T extends Spatial> void setTarget(T target) {
        this.target.setValue(target);
    }
    
    /**
     * (non-JavaDoc)
     * @see Jme3GL2Camera#setProperty(java.lang.String, java.lang.Object) 
     */
    @Override
    public <T> Jme3GL2Camera setProperty(String key, T value) {
        if (key == null) {
            throw new IllegalArgumentException("Invalid attribute key.");
        }
        this.attributes.put(key, value);
        return this;
    }

    /**
     * (non-JavaDoc)
     * @see Jme3GL2Camera#getProperty(java.lang.String, java.lang.Object) 
     */
    @Override
    public <T> T getProperty(String key, T defaultVal) {
        Object value = this.attributes.get(key);
        if (value == null) {
            return defaultVal;
        }
        return (T) value;
    }

    /**
     * Method in charge of generating a text message with the information of the
     * properties used by the camera manager.
     * @return message/string.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ Class ]: ").append(getClass().getName());     
        builder.append('\n');
        for (final Map.Entry<?,?> entry : this.attributes.entrySet()) {
            builder.append(" *").append(' ');
            builder.append(entry.getKey()).append(": ")
                   .append(entry.getValue()).append('\n');
        }
        return String.valueOf(builder);
    }
}
