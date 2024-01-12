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
package jme3gl2.physics;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import jme3gl2.physics.control.PhysicsBody2D;
import jme3gl2.physics.control.PhysicsControl;
import jme3gl2.util.Converter;

import java.util.Iterator;
import java.util.List;

import org.dyn4j.collision.Bounds;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

/**
 * An <code>PhysicsSpace</code> is a class that is in charge of initializing
 * and preparing the space (<code>org.dyn4j.world.World</code>) for physics.
 * 
 * @author wil
 * @version 1.0.5-SNAPSHOT
 * 
 * @since 1.0.0
 * @param <E> the type {@code PhysicsBody}.
 */
@SuppressWarnings(value = {"unchecked"})
public class PhysicsSpace<E extends PhysicsBody2D> {

    /**
     * Predetermined speed of physics.
     */
    private static final float DEFAULT_SPEED = 1f;

    /**
     * World of physics {@code Dyn4j}.
     */
    private World<E> physicsWorld;
    
    /**
     * Speed of the physics updates using the world {@code Dyn4j}.
     */
    protected float speed = DEFAULT_SPEED;

    /**
     * Instantiates a new object <code>PhysicsSpace</code>. Set the default
     * values of the physics space.
     * 
     * @param initialCapacity initial capacity (bodies in the world).
     * @param initialJointCapacity initial capacity of the joints.
     * @param bounds Limit of the world.
     */
    public PhysicsSpace(final Integer initialCapacity, final Integer initialJointCapacity, final Bounds bounds) {
        if (initialCapacity != null && initialJointCapacity != null) {
            this.physicsWorld = new World<>(initialCapacity, initialJointCapacity);
        } else {
            this.physicsWorld = new World<>();
        }        
        if ( bounds != null ) {
            this.physicsWorld.setBounds(bounds);
        }
    }

    /**
     * (non-Javadoc)
     * @param body Body
     */
    public void addBody(final E body) {
        if (body instanceof PhysicsControl) {
            ((PhysicsControl)  body).setPhysicsSpace(this);
        }
        this.physicsWorld.addBody(body);
    }
    
    /**
     * (non-Javadoc)
     * @param body Body
     * @return Boolean
     */
    public boolean removeBody(final E body) {
        boolean b = this.physicsWorld.removeBody(body);
        if ( b && (body instanceof PhysicsControl) ) {
            ((PhysicsControl) body).setPhysicsSpace(null);
        }
        return b;
    }
    
    /**
     * (non-Javadoc)
     * @param body Body
     * @param notify Boolean
     * @return Boolean
     */
    public boolean removeBody(final E body, final boolean notify) {
        boolean b = this.physicsWorld.removeBody(body, notify);
        if ( b && (body instanceof PhysicsControl) ) {
            ((PhysicsControl) body).setPhysicsSpace(null);
        }
        return b;
    }

    /**
     * (non-Javadoc)
     * @param joint Joint
     */
    public void addJoint(final Joint joint) {
        Iterator<E> it = joint.getBodyIterator();
        while (it.hasNext()) {
            E next = it.next();
            if ( next != null && (next instanceof PhysicsControl)) {
                ((PhysicsControl) next).setPhysicsSpace(this);
            }
        }
        this.physicsWorld.addJoint(joint);
    }
    
    /**
     * (non-Javadoc)
     * @param joint Joint
     * @return Boolean
     */
    public boolean removeJoint(final Joint joint) {
        Iterator<E> it = joint.getBodyIterator();
        while (it.hasNext()) {
            E next = it.next();
            if ( next != null && (next instanceof PhysicsControl)) {
                ((PhysicsControl) next).setPhysicsSpace(null);
            }
        }
        return this.physicsWorld.removeJoint(joint);
    }

    /**
     * Method in charge of updating the world of physics.
     * @param elapsedTime time lapse.
     */
    public void updateFixed(final float elapsedTime) {
        this.physicsWorld.update(elapsedTime);
    }

    /**
     * Clean the world.
     * @deprecated Use <code>destroy()</code> to eliminate the physical world.
     */
    @Deprecated(since = "2.5.0")
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * Clean the world (physical bodies and joints).
     * @param notify <code>true</code> if you wish to inform the listeners that
     * there is a destruction of body or joint, otherwise the value
     * is <code>false</code>.
     */
    public void removeAll(boolean notify) {        
        this.physicsWorld.removeAllBodiesAndJoints(true);
    }
    
    /**
     * Method in charge of destroying the physical world. Set <code>null</code>
     * to object <code>World</code>.
     */
    public void destroy() {
        this.physicsWorld = null;
    }
    
    /**
     * Sets a new speed for the world of physics.
     * @param speed new speed.
     */
    public void setSpeed(final float speed) {
        this.speed = speed;
        this.physicsWorld.getSettings().setMinimumAtRestTime(Settings.DEFAULT_STEP_FREQUENCY * speed);
    }

    /**
     * Returns the current world speed.
     * @return speed.
     */
    public float getSpeed() {
        return this.speed;
    }
    
    /**
     * (non-Javadoc)
     * @param gravity Vector2f
     */
    public void setGravity(final Vector2f gravity) {
        this.physicsWorld.setGravity(new Vector2(gravity.x, gravity.y));
    }
    
    /**
     * (non-Javadoc)
     * @param x double
     * @param y double
     */
    public void setGravity(final double x, final double y) {
        this.physicsWorld.setGravity(new Vector2(x, y));
    }

    /**
     * (non-Javadoc)
     * @return Vector3f
     */
    public Vector3f getGravity() {
        return Converter.toVector3f(this.physicsWorld.getGravity());
    }
    
    /**
     * Returns a list of bodies contained in the world.
     * @return list of bodies.
     */
    public List<E> getBodies() {
        return this.physicsWorld.getBodies();
    }
    
    /**
     * Returns a list of joints.
     * @return list of joints.
     */
    public List<Joint<E>> getJoints() {
        return this.physicsWorld.getJoints();
    }
    
    /**
     * Returns the world of physics {@code Dyn4j}.
     * @return world of physics.
     */
    public World<E> getPhysicsWorld() {
        return this.physicsWorld;
    }
    
    /**
     * Returns the number of existing bodies in the world.
     * @return body count.
     */
    public int getBodyCount() {
        return this.physicsWorld.getBodyCount();
    }
}
