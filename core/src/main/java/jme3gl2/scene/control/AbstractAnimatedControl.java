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
package jme3gl2.scene.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import jme3gl2.scene.shape.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jme3gl2.physics.control.PhysicsBody2D;

/**
 * An <code>AbstractAnimatedControl</code> is in charge of managing a template
 * for applying a certain animation to a <code>Sprite</code>.
 * <p>
 * It will depend on how you want to apply the animation, i.e. whether it is an
 * animation with textures or one that manipulates vertices.
 * </p>
 * 
 * @author wil
 * @version 1.5-SNAPSHOT
 * @param <E> animation type.
 * 
 * @since 1.0.0
 */
public abstract 
class AbstractAnimatedControl<E extends Object> extends AbstractControl {
    
    /** Animation map. */
    protected Map<String, E[]> animations = new HashMap<>();
    
    /**
     * Map in charge of storing the change listeners for the animations so that
     * they can be managed from the outside.
     */
    protected List<SpriteAnimationChangeListener<PhysicsBody2D>> animationChangeListeners = 
        new ArrayList<>();
    
    /**
     * <code>true</code> if the animation is repeated constantly from beginning
     * to end, otherwise <code>false</code> to animate a 'strip' where it stops
     * once the set limit (size) is reached.
     */
    protected boolean loop;
    
    /** Current active animation. */
    protected E[] currentAnimation;
    
    /** {@code Sprite} where the animation will be applied. */
    protected Sprite sprite;
    
    /** Speed at which the animation will be applied. */
    protected float speed = 1;
    
    /** Current index of animations. */
    protected int currentIndex;
    
    /** Current name of the animation. */
    protected String currentAnimationName;
    
    /** Time at which the animation will be activated. */
    protected float animationFrameTime = 1f;
    
    /** The lapse of time elapsed. */
    protected float elapsedeTime = 0f;
    
    /**
     * <code>true</code> if you are using a material of type: <code>
     * Common/MatDefs/Light/Lighting.j3md</code> or inherit from it, otherwise
     * it will be <code>false</code>.
     */
    protected boolean lighting;

    /**
     * Default constructor.
     */
    public AbstractAnimatedControl() {
        this(false);
    }

    /**
     * Class constructor <code>AbstractAnimatedControl</code> where you can
     * specify the type of material you are using.
     * @param lighting boolean.
     */
    public AbstractAnimatedControl(boolean lighting){
        this.lighting = lighting;
        this.loop     = true;
    }
    
    /**
     * Method for adding a new listener to the list.
     * @param sacl new listener of changes.
     */
    public void addSpriteAnimationChangeListener(SpriteAnimationChangeListener<PhysicsBody2D> sacl) {
        this.animationChangeListeners.add(sacl);
    }
    
    /**
     * Deletes a previously registered listener.
     * @param sacl listener to be eliminated.
     */
    public void removeSpriteAnimationChangeListener(SpriteAnimationChangeListener<PhysicsBody2D> sacl) {
        this.animationChangeListeners.remove(sacl);
    }
    
    /**
     * Clears the 'records' of all listeners.
     */
    public void removeAllSpriteAnimationChangeListener() {
        this.animationChangeListeners.clear();
    }
    
    /**
     * Returns <code>true</code> if the animation is infinite, otherwise
     * <code>false</code>.
     * @return boolean.
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * Sets the status of this animation control.
     * @param loop boolean.
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    /**
     * Method in charge of triggering the animation change event to notify the
     * listener.
     */
    protected void fireSpriteAnimationChangeListener() {
        for (final SpriteAnimationChangeListener<PhysicsBody2D> changeListener : this.animationChangeListeners) {
            if (changeListener == null)
                continue;
            
            changeListener.changeAnimation(spatial.getControl(PhysicsBody2D.class), currentIndex);
        }
    }
    
    /**
     * (non-JavaDoc)
     * 
     * @param spatial Spatial.
     * @see AbstractControl#setSpatial(com.jme3.scene.Spatial) 
     */
    @Override
    public void setSpatial(Spatial spatial) {
        if ( !(spatial instanceof Geometry) ) {
            throw new UnsupportedOperationException("Spatial not supported.");
        }
        
        Mesh mesh = ((Geometry) spatial).getMesh();
        if ( !(mesh instanceof Sprite) ) {
            throw new IllegalArgumentException("The Spatial does not have a Sprite.");
        }
        
        sprite = (Sprite)mesh;
        super.setSpatial(spatial);
    }

    /**
     * Returns the current name of the current animation applied.
     * @return animation name.
     */
    public String getCurrentAnimationName() {
        return currentAnimationName;
    }

    /**
     * Returns a {@code Set} with the names of the animations.
     * @return list of names.
     */
    public Set<String> getAnimations() {
        return animations.keySet();
    }

    /**
     * Returns the speed of the animation.
     * @return animation speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets a new speed for animations.
     * @param speed animation speed.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * (non-JavaDoc)
     * @param rm RenderManager
     * @param vp ViewPort
     * @see AbstractControl#render(com.jme3.renderer.RenderManager, com.jme3.renderer.ViewPort) 
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    /**
     * Method in charge of adding a new animation to the list.
     * @param name name of the animation.
     * @param frames animation setup (data).
     */
    public abstract void addAnimation(String name, E[] frames);
    
    /**
     * Method responsible for starting an animation.
     * @param name name of the animation.
     * @param timePerFrame time per frame.
     */
    public abstract void playAnimation(String name, float timePerFrame);
    
    /**
     * Method for managing the name of the parameter used to establish the
     * texture of a material.
     * <p>
     * If {@code lighting} is as a value <code>true</code>, in value returned is:
     * <b>DiffuseMap</b> to set a texture, otherwise <b>ColorMap</b>.
     * </p>
     * 
     * @return key name to establish the text.
     */
    protected String getNameParam() {
        return lighting ? "DiffuseMap" : "ColorMap";
    }
}
