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
package jme3gl2.physics.debug.control;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.TempVars;

import jme3gl2.physics.control.PhysicsBody2D;
import jme3gl2.physics.debug.Dyn4jDebugColor;
import jme3gl2.util.Converter;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Ellipse;
import org.dyn4j.geometry.HalfEllipse;
import org.dyn4j.geometry.Slice;
import org.dyn4j.geometry.Vector2;

/**
 * Class <code>AbstractConvexDebugControl</code> in charge of controlling the
 * position and rotation of a physical form.
 * <p>
 * <b>Dyn4j</b> has the freedom to transfer a form <code>Convex</code>
 * to a specific point of a body, since it is relative to the physical body it
 * has to be controlled separately.
 * </p>
 * <p>
 * The following shapes, their positions and rotations are peculiar and therefore
 * have a class dedicated to obtain these coordinates:
 * </p>
 * <ul>
 *  <li>Capsule</li>
 *  <li>Circle</li>
 *  <li>Ellipse</li>
 *  <li>HalfEllipse</li>
 *  <li>Slice</li>
 * </ul>
 * <p>
 * On the other hand, a form <code>Polygon</code> or that implements the interface
 * <code>Wound</code> has no need to have a control since its points/vertices are
 * modified by moving or rotating them in its shape.
 * </p>
 * 
 * @author wil
 * @version 1.5.0
 * 
 * @since 2.5.0
 * @param <E> body type
 */
public abstract class AbstractConvexDebugControl<E extends Convex> extends AbstractControl {

    /**
     * Internal class <code>CapsuleDebugControl</code> responsible for managing
     * the coordinates of the form <code>Capsule</code>.
     */
    public static final class CapsuleDebugControl extends AbstractConvexDebugControl<Capsule> {
        
        /**
         * Class constructor of <code>CapsuleDebugControl</code>.
         * @param shape physical form.
         * @param body physical body.
         */
        public CapsuleDebugControl(BodyFixture shape, PhysicsBody2D body) {
            super(shape, body);
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getRotationAngle() 
         * 
         * @return double
         */
        @Override
        double getRotationAngle() {
            return this.getShape().getRotationAngle();
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getLocaTranslation() 
         * 
         * @return double
         */
        @Override
        Vector2 getLocaTranslation() {
            return this.getShape().getCenter();
        }        
    }

    /**
     * Internal class <code>CircleDebugControl</code> responsible for managing
     * the coordinates of the form <code>Circle</code>.
     */
    public static final class CircleDebugControl extends AbstractConvexDebugControl<Circle> {
        
        /**
         * Class constructor of <code>CircleDebugControl</code>.
         * @param shape physical form.
         * @param body physical body.
         */
        public CircleDebugControl(BodyFixture shape, PhysicsBody2D body) {
            super(shape, body);
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getRotationAngle() 
         * 
         * @return double
         */
        @Override
        double getRotationAngle() {
            return 0;
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getLocaTranslation() 
         * 
         * @return double
         */
        @Override
        Vector2 getLocaTranslation() {
            return getShape().getCenter();
        }        
    }
    
    /**
     * Internal class <code>EllipseDebugControl</code> responsible for managing
     * the coordinates of the form <code>Ellipse</code>.
     */
    public static final class EllipseDebugControl extends AbstractConvexDebugControl<Ellipse> {
        
        /**
         * Class constructor of <code>EllipseDebugControl</code>.
         * @param shape physical form.
         * @param body physical body.
         */
        public EllipseDebugControl(BodyFixture shape, PhysicsBody2D body) {
            super(shape, body);
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getRotationAngle() 
         * 
         * @return double
         */
        @Override
        double getRotationAngle() {
            return getShape().getRotationAngle();
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getLocaTranslation() 
         * 
         * @return double
         */
        @Override
        Vector2 getLocaTranslation() {
            return getShape().getCenter();
        }        
    }
    
    /**
     * Internal class <code>HalfEllipseDebugControl</code> responsible for
     * managing the coordinates of the form <code>HalfEllipse</code>.
     */
    public static final class HalfEllipseDebugControl extends AbstractConvexDebugControl<HalfEllipse> {
        
        /**
         * Class constructor of <code>HalfEllipseDebugControl</code>.
         * @param shape physical form.
         * @param body physical body.
         */
        public HalfEllipseDebugControl(BodyFixture shape, PhysicsBody2D body) {
            super(shape, body);
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getRotationAngle() 
         * 
         * @return double
         */
        @Override
        double getRotationAngle() {
            return getShape().getRotationAngle();
        }

        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getLocaTranslation() 
         * 
         * @return double
         */
        @Override
        Vector2 getLocaTranslation() {
            return getShape().getEllipseCenter();
        }        
    }
    
    /**
     * Internal class for any convex shape.
     */
    public static final class ConvexDebugControl extends AbstractConvexDebugControl<Convex> {

        /** Default position. */
        private final Vector2 v = new Vector2();
        
        /**
         * Class constructor.
         * @param shape physical form.
         * @param body physical body.
         */
        public ConvexDebugControl(BodyFixture shape, PhysicsBody2D body) {
            super(shape, body);
        }

        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getRotationAngle() 
         * 
         * @return double
         */
        @Override
        double getRotationAngle() {
            return 0;
        }

         /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getLocaTranslation() 
         * 
         * @return double
         */
        @Override
        Vector2 getLocaTranslation() {
            return v;
        }
    }
    
    /**
     * Internal class <code>SliceDebugControl</code> responsible for managing
     * the coordinates of the form <code>Slice</code>.
     */
    public static final class SliceDebugControl extends AbstractConvexDebugControl<Slice> {
        /**
         * Class constructor of <code>SliceDebugControl</code>.
         * @param shape physical form.
         * @param body physical body.
         */        
        public SliceDebugControl(BodyFixture shape, PhysicsBody2D body) {
            super(shape, body);
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getRotationAngle() 
         * 
         * @return double
         */
        @Override
        double getRotationAngle() {
            return getShape().getRotationAngle();
        }
        
        /**
         * (non-JavaDoc)
         * @see AbstractConvexDebugControl#getLocaTranslation() 
         * 
         * @return double
         */
        @Override
        Vector2 getLocaTranslation() {
            return getShape().getCircleCenter();
        }        
    }
    
    /** Physical form of the object. */
    final BodyFixture fixture;

    /** Physical bodies. */
    final PhysicsBody2D body2D;
    
    /**
     * Private class constructor <code>AbstractConvexDebugControl</code>.
     * @param fixture physical form.
     * @param body2D physical body.
     */
    private AbstractConvexDebugControl(BodyFixture fixture, PhysicsBody2D body2D) {
        this.fixture = fixture;
        this.body2D = body2D;
    }
    
    /**
     * Returns physical shape.
     * @return Convex
     */
    @SuppressWarnings("unchecked")
    public E getShape() {
        return (E) fixture.getShape();
    }
    
    /**
     * (non-JavaDoc)
     * @see com.jme3.scene.control.AbstractControl#controlUpdate(float) 
     * 
     * @param tpf float
     */
    @Override
    protected void controlUpdate(float tpf) {
        applyPhysicsLocation();
        applyPhysicsRotation();
        renderMaterial();
    }
    
    /**
     * Renders the object; manages the geometry material.
     */
    void renderMaterial() {
        ColorRGBA color;        
        if (!(body2D.isEnabled())) {
            color = Dyn4jDebugColor.DISABLED;
        } else {
            if (fixture.isSensor()) {
                color = Dyn4jDebugColor.SENSOR;
            } else {
                if (body2D.isBullet()) {
                    color = Dyn4jDebugColor.BULLET;
                } else if (body2D.isStatic()) {
                    color = Dyn4jDebugColor.STATIC;
                } else if (body2D.isKinematic()) {
                    color = Dyn4jDebugColor.KINEMATIC;
                } else if (body2D.isAtRest()) {
                    color = Dyn4jDebugColor.AT_RESET;
                } else {
                    color = Dyn4jDebugColor.DEFAULT;
                }
            }
        }
        
        Material mat = ((Geometry) spatial).getMaterial();   
        mat.setColor("Color", color);
    }

    /**
     * Applies the rotation that has the shape to the geometry.
     */
    void applyPhysicsRotation() {
        double rotation = getRotationAngle();
        final TempVars tempVars = TempVars.get();
        final Quaternion quaternion = tempVars.quat1;

        quaternion.fromAngleAxis(Converter.toFloat(rotation), new Vector3f(0.0F, 0.0F, 1.0F));
        this.spatial.setLocalRotation(quaternion);

        tempVars.release();
    }
    
    /**
     * Method in charge of returning the rotation angle of the shape.
     * @return angle.
     */
    abstract double getRotationAngle();
    
    /**
     * Method in charge of returning the local position of the form.
     * @return position.
     */
    abstract Vector2 getLocaTranslation();
    
    /**
     * Method in charge of applying a physical translation.
     */
    void applyPhysicsLocation() {
        final Vector2 center = getLocaTranslation();
        if (center == null)
            return;

        final float posX = Converter.toFloat(center.x);
        final float posY = Converter.toFloat(center.y);
        
        spatial.setLocalTranslation(posX, posY, spatial.getLocalTranslation().z);
    }
    
    /**
     * (non-JavaDoc)
     * @see com.jme3.scene.control.AbstractControl#controlRender(com.jme3.renderer.RenderManager, com.jme3.renderer.ViewPort) 
     * 
     * @param rm render-manager
     * @param vp view-port
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
