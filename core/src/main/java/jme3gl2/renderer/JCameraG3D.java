package jme3gl2.renderer;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An object of the class <code>JCameraG3D</code> is responsible for managing
 * the 3D camera.
 * <p>
 * With this camera manager we can create minimalistic scenes, that is to say
 * that we can bring the sprites as close to the camera as we can to have several
 * perspectives of the scene/world.
 * </p>
 * <p>
 * The following can be used to manage the properties of the camera manager:
 * </p>
 * <p>
 * List of properties:
 * </p>
 * <ul>
 *  <li><b>FollowInterpolationAmount</b>: Defines the interpolation speed at
 *  which the camera moves.</li>
 *  <li><b>CameraDistanceFrustum</b>: Defines the distance of the camera from
 *  the objects in the scene.</li>
 *  <li><b>InterpolationByTPF</b>: <code>true</code> if a transition is used in
 *  addition to the normal interpolation, i.e. the interpolation value is
 *  multiplied by the FPS.</li>
 *  <li><b>SmoothingDepth</b>: <code>true</code> if the distance is applied
 *  without any interpolation, otherwise <code>false</code> where the camera
 *  distance has a transition. This is valid if <code>InterpolationByTPF</code>
 *  is not active (<code>false</code>).</li>
 * </ul>
 * 
 * @author wil
 * @version 1.0-SNAPSHOT 
 * 
 * @since 2.0.5
 */
public class JCameraG3D extends AbstractJme3GL2camera {

    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(JCameraG3D.class.getName());

    /** Camera position. */
    private final Vector3f position;
    
    /** Camera clipping. */
    private final Jme3GL2Clipping clipping;
    
    /** 3D camera to be managed in 2D. */
    private Camera cam;

    /**
     * Default class constructor <code>JCameraG3D</code> where the objects to be
     * used are initialized.
     */
    public JCameraG3D() {
        this.position = new Vector3f(0.0F, 0.0F, 0.0F);
        this.clipping = new Jme3GL2Clipping();
        
        JCameraG3D.this.setProperty("FollowInterpolationAmount", 0.2F);
        JCameraG3D.this.setProperty("CameraDistanceFrustum", 10.0F);
    }
    
    /**
     * (non-JavaDoc)
     */
    @Override
    public void initialize(Camera cam) {
        cam.setParallelProjection(false);
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 1f, 1000f);
        cam.setLocation(new Vector3f(0f, 0f, 10f));
        cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);        
        this.cam = cam;
        LOG.log(Level.INFO, toString());
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public void update(float tpf) {
        Vector3f translation = target.getLocalTranslation(true);
        Vector2f pos = clipping.clamp(translation.x, translation.y);
        
        position.set(pos.x, pos.y, getProperty("CameraDistanceFrustum", 10.0F));
        if (getProperty("InterpolationByTPF", true)) {
            cam.setLocation(cam.getLocation().interpolateLocal(position, getProperty("FollowInterpolationAmount", 0.2F) * tpf));
        } else {
            Vector3f newpos = cam.getLocation().interpolateLocal(position, getProperty("FollowInterpolationAmount", 0.2F));
            if (getProperty("SmoothingDepth", true)) {
                cam.setLocation(newpos);
            } else {
                cam.setLocation(newpos.setZ(position.z));
            }            
        }
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public void setCameraDistanceFrustum(float frustum) {
        setProperty("CameraDistanceFrustum", frustum);
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public Jme3GL2Clipping getClipping() {
        return clipping;
    }
}
