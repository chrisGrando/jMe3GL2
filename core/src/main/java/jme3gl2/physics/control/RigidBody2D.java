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
package jme3gl2.physics.control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

import jme3gl2.physics.collision.AbstractCollisionShape;
import jme3gl2.util.Converter;

import org.dyn4j.geometry.Convex;

/**
 * An object of the class <code>RigidBody2D</code> is responsible for the
 * control and application of physics in the <code>Spatial</code> (2D model)
 * with a rigid body.
 *
 * @author wil
 * @version 1.0.1-SNAPSHOT
 *
 * @since 1.0.0
 */
public class RigidBody2D extends PhysicsBody2D {

    /**
     * Class constructor <code>RigidBody2D</code>.
     */
    public RigidBody2D() {
    }
    
    /**
     * Class constructor <code>RigidBody2D</code>.
     * @param collisionShape physical form.
     */
    public RigidBody2D(AbstractCollisionShape<? extends Convex> collisionShape) {
        this.addFixture(collisionShape.getCollisionShape());
    } 
    
    /**
     * (non-JavaDoc).
     * @param tpf float
     * @see PhysicsBody2D#controlUpdate(float) 
     */
    @Override
    protected void controlUpdate(float tpf) {
        applyPhysicsLocation(this);
        applyPhysicsRotation(this);
    }

    /**
     * Detects if this body contains more than one {@code Fixture}.
     * @return {@code true} if it has more than one {@code Fixture},
     * otherwise it will return {@code false}.
     */
    public boolean hasMultipleBodies() {
        return this.getFixtureCount() > 1;
    }
    
    /**
     * Method in charge of returning the current position.
     * @return current position.
     */
    public Vector3f getPhysicsLocation() {
        return new Vector3f(Converter.toFloat(getTransform().getTranslationX()), Converter.toFloat(getTransform().getTranslationY()), this.spatial.getLocalTranslation().z);
    }

    /**
     * Method in charge of returning the world position of this body.
     * @return world position.
     */
    public Vector3f getWorldLocation() {
        return new Vector3f(Converter.toFloat(getWorldCenter().x), Converter.toFloat(getWorldCenter().y), this.spatial.getWorldTranslation().z);
    }

    /**
     * Cleans the forces.
     */
    public void clearForces() {
        this.clearForce();
        this.clearAccumulatedForce();
        this.clearTorque();
        this.clearAccumulatedTorque();
        this.setAngularVelocity(0);
        this.setLinearVelocity(0, 0);
    }
    
    /**
     * (non-JavaDoc).
     * @param rm RenderManager
     * @param vp ViewPort
     * @see PhysicsBody2D#render(com.jme3.renderer.RenderManager, com.jme3.renderer.ViewPort) 
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
