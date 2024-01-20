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

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

import java.util.logging.Logger;

/**
 * An object of the class <code>Camera2DState</code> is responsible for preparing
 * and operating the 2D camera.
 * <p>
 * With this we can use the 3D camera that provides us with {@code JME} in a 2D
 * world.
 * </p>
 * 
 * @author wil
 * @version 2.0
 * 
 * @since 2.0.0
 */
@SuppressWarnings(value = {"unchecked"})
public class Camera2DRenderer extends BaseAppState {
    
    /**
     * Internal enumerated class <code>GLRendererType</code> responsible for
     * listing the types of camera managers.
     */
    public static enum GLRendererType {
        /**
         * It uses a manager where it places the camera in a parallel projection.
         * <p>
         * Objects will never touch the camera, i.e. no matter how close they
         * get, they cannot reach the front view.
         * </p>
         */
        GL_2D,
        
        /**
         * It uses a manager where the 3D camera is used to ensure a more
         * realistic approach.
         */
        GL_3D;
    }

    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(Camera2DRenderer.class.getName());

    /** 2D camera. */
    private Jme3GL2Camera gL2Camera;

    /** Type of manager/camera. */
    private GLRendererType rendererType;

    /**
     * Class constructor <code>Camera2DRenderer</code> where the type of
     * manager/camera is specified.
     * @param rendererType type of manager/camera.
     */
    public Camera2DRenderer(GLRendererType rendererType) {
        switch (rendererType) {
            case GL_2D:
                gL2Camera = new JCameraG2D();
                break;
            case GL_3D:
                gL2Camera = new JCameraG3D();
                break;
            default:
                throw new AssertionError();
        }
        this.rendererType = rendererType;
    }
        
    
    /**
     * Generates an object of the class <code>Camera2DRenderer</code> with the
     * main characteristics to be managed.
     * 
     * @param rendererType camera rendering type.
     * @param cameraDistanceFrustum camera distance.
     * @param followInterpolationAmount interpolation speed.
     */
    public Camera2DRenderer(GLRendererType rendererType, float cameraDistanceFrustum, float followInterpolationAmount) {
        this(rendererType);        
        /*
        We simply add the new object attributes 'Camara2D' to initialize them
        later.
        */
        this.gL2Camera.setProperty("CameraDistanceFrustum", cameraDistanceFrustum);
        this.gL2Camera.setProperty("FollowInterpolationAmount", followInterpolationAmount);
    }
    
    /**
     * (non-JavaDoc)
     * @param app {@code Application}
     * @see BaseAppState#initialize(com.jme3.app.Application) 
     */
    @Override
    protected void initialize(Application app) {
        final Camera camera = app.getCamera();
        if (app instanceof SimpleApplication) {
            SimpleApplication simpleApplication = (SimpleApplication) app;
            simpleApplication.getFlyByCamera().setEnabled(false);
            simpleApplication.getFlyByCamera().unregisterInput();
            LOG.info("PlatformerCameraState is removing default fly camera");
        }
        
        gL2Camera.initialize(camera);
    }

    /**
     * (non-JavaDoc)
     * @param tpf {@code float}
     * @see BaseAppState#update(float) 
     * @see JCameraG2D#update(float) 
     */
    @Override
    public void update(float tpf) {
        gL2Camera.update(tpf);
    }

    /**
     * (non-JavaDoc)
     * @param s {@code Spatial}
     * @see JCameraG2D#setTarget(com.jme3.scene.Spatial) 
     */
    public void setTarget(Spatial s) {
        gL2Camera.setTarget(s);
    }
    
    /**
     * Method responsible for setting the 2D camera interpolation speed.
     * <p>
     * This speed is used to move the camera towards your target if you have one
     * set.
     * </p>
     * @param follow interpolation value.
     */
    public void setFollowInterpolationAmount(float follow) {
        gL2Camera.setProperty("FollowInterpolationAmount", follow);
    }

    /**
     * (non-JavaDoc)
     * @param frustum {@code float}
     * @see JCameraG2D#setCameraDistanceFrustum(float) 
     */
    public void setDistanceFrustum(float frustum) {
        gL2Camera.setCameraDistanceFrustum(frustum);
    }
    
    /**
     * Sets new clipping values for the camera.
     * <p>
     * If you want to remove the trimmings, with values <code>null</code> as a
     * parameter they will be erased.
     * </p>
     * 
     * @param minimumClipping minimum clipping.
     * @param maxClipping maximum clipping.
     */
    public void setClipping(Vector2f minimumClipping, Vector2f maxClipping) {
        Jme3GL2Clipping gL2Clipping = gL2Camera.getClipping();
        gL2Clipping.setMaximum(maxClipping);
        gL2Clipping.setMinimum(minimumClipping);
    }
    
    /**
     * (non-JavaDoc)
     * @param offset {@code Vector2f}
     * @see Jme3GL2Clipping#setOffset(com.jme3.math.Vector2f) 
     */
    public void setOffset(Vector2f offset) {
        Jme3GL2Clipping gL2Clipping = gL2Camera.getClipping();
        gL2Clipping.setOffset(offset);
    }
    
    /**
     * Returns the object managed by the camera in 2D.
     * @return 2D camera.
     * @deprecated use {@link #getJme3GL2Camera()}.
     */
    @Deprecated
    public Jme3GL2Camera getCamera2D() {
        return gL2Camera;
    }
    
    /**
     * Method in charge of returning the fake 2D camera manager.
     * @param <T> data type.
     * @return camera manager.
     */
    public <T extends Jme3GL2Camera> T getJme3GL2Camera() {
        return (T) gL2Camera;
    }

    /**
     * Returns the type of camera used.
     * @return rendering type.
     */
    public GLRendererType getRendererType() {
        return rendererType;
    }

    /**
     * (non-JavaDoc)
     * @param app application
     * @see BaseAppState#cleanup(com.jme3.app.Application) 
     */
    @Override protected void cleanup(Application app) {
    }
    
    /**
     * (non-JavaDoc)
     * @see BaseAppState#onEnable() 
     */
    @Override protected void onEnable() {
    }
    
    /**
     * (non-JavaDoc)
     * @see BaseAppState#onDisable() 
     */
    @Override protected void onDisable() {
    }   
}
