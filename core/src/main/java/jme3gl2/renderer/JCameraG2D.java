/* Copyright (c) 2009-2023 jMonkeyEngine.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3gl2.renderer;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An object of the class <code>JCameraG2D</code> is responsible for managing
 * the behavior of the default camera in games <code>JME</code> in 2D.
 * <p>
 * With this class we can simulate a two-dimensional world, to handle the
 * properties of the camera manager we can use the following:
 * </p>
 * <p>
 * List of properties:
 * </p>
 * <ul>
 *  <li><b>FollowInterpolationAmount</b>: Defines the interpolation speed at
 *  which the camera is moved.</li>
 *  <li><b>CameraDistanceFrustum</b>: Defines the distance of the camera from
 *  the objects in the scene.</li>
 *  <li><b>InterpolationByTPF</b>: <code>true</code> if a transition is used in
 *  addition to the normal interpolation, i.e. the interpolation value is multiplied by FPS.</li>
 * </ul>
 * 
 * @author wil
 * @version 2.0
 * 
 * @since 2.0.0
 */
@SuppressWarnings("unchecked")
public class JCameraG2D extends AbstractJme3GL2camera {
    
    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(JCameraG2D.class.getName());
    
    /** Camera position. */
    private final Vector3f position;
    
    /** Camera clipping. */
    private final Jme3GL2Clipping clipping;
    
    /** 3D camera to be managed in 2D. */
    private Camera camera3D;

    /**
     * Default class constructor <code>Camera2D</code> where the objects to be
     * used are initialized.
     */
    public JCameraG2D() {
        this.position = new Vector3f();
        this.clipping = new Jme3GL2Clipping();
        
        JCameraG2D.this.setProperty("FollowInterpolationAmount", 0.2F);
        JCameraG2D.this.setProperty("CameraDistanceFrustum", 10.0F);
    }
    
    /**
     * (non-JavaDoc)
     */
    @Override
    public void initialize(Camera cam) {
        // We must change the camera projection
        camera3D = cam;
        camera3D.setParallelProjection(true);
        
        float aspect = (float) camera3D.getWidth() / camera3D.getHeight();
        float cameraDistanceFrustum = getProperty("CameraDistanceFrustum", 10.0F);
        
        camera3D.setFrustum(-1000, 1000, -aspect * cameraDistanceFrustum, aspect * cameraDistanceFrustum, cameraDistanceFrustum, -cameraDistanceFrustum);
        camera3D.setLocation(new Vector3f(0, 0, 0));
        
        LOG.log(Level.INFO, toString());
    }
    
    /**
     * (non-JavaDoc)
     */
    @Override
    public void update(float tpf) {
        Vector3f translation = target.getLocalTranslation(false);
        Vector2f pos = clipping.clamp(translation.x, translation.y);
        
        position.set(pos.x, pos.y, getProperty("CameraDistanceFrustum", 10.0F));
        if (getProperty("InterpolationByTPF", true)) {
            camera3D.setLocation(camera3D.getLocation().interpolateLocal(position, getProperty("FollowInterpolationAmount", 0.2F) * tpf));
        } else {
            camera3D.setLocation(camera3D.getLocation().interpolateLocal(position, getProperty("FollowInterpolationAmount", 0.2F)));
        }
    }
    
    /**
     * (non-JavaDoc)
     */
    @Override
    public void setCameraDistanceFrustum(float frustum) {
        float aspect = (float) camera3D.getWidth() / camera3D.getHeight();
        camera3D.setFrustum(-1000, 1000, -aspect * frustum, aspect * frustum, frustum, -frustum);
        setProperty("CameraDistanceFrustum", frustum);
    }
    
    /**
     * Returns the camera target.
     * @param <T> data type.
     * @return target.
     */
    public <T extends Spatial> T getTarget() {
        return (T) target.getValue();
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public Jme3GL2Clipping getClipping() {
        return clipping;
    }
}
