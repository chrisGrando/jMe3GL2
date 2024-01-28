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

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An animation with the class <code>RibbonBoxAnimationSprite</code> uses an
 * image as an animated ribbon, where the animation can be controlled by frames.
 * 
 * <p>
 * <b>Example:</b>
 * </p>
 * 
 * <pre><code>
 * Image:
 * 
 * +----+----+----+---+
 * | A  | B  | C  | D |
 * +----+----+----+---+
 *   0    1    2    3
 * 
 * Where A, B, C and D are images that act as frames of a ribbon.
 * </code></pre>
 * 
 * <p>
 * Each frame corresponds to a movement of the animation, by means of the frames
 * we can animate a 2D model, it's like a movie ribbon.
 * </p>
 * 
 * @author wil
 * @version 1.5-SNAPSHOT
 *
 * @since 2.0.0
 */
public class RibbonBoxAnimationSprite extends AbstractAnimatedControl<RibbonBox> {

    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(RibbonBoxAnimationSprite.class.getName());
    
    /** Current index of the animation frame. */
    private int index;

    /** Material of the 2D models. */
    private Material mat;

    /**
     * Default class constructor {@code RibbonBoxAnimationSprite}.
     */
    public RibbonBoxAnimationSprite() {
    }

    /**
     * Class constructor <code>ibbonBoxAnimationSprite</code>.
     * @param lighting boolean.
     */
    public RibbonBoxAnimationSprite(boolean lighting) {
        super(lighting);
    }
        
    /**
     * (non-JavaDoc)
     * @param spatial Spatial
     * @see AbstractAnimatedControl#setSpatial(com.jme3.scene.Spatial) 
     */
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        mat = ((Geometry) spatial).getMaterial();
    }
    
    /**
     * Implementation of the method <code>addAnimation(String, E[])</code>.
     * @param name An {@code String} as key.
     * @param frames An array of {@code Integer} as value.
     */
    @Override
    public void addAnimation(String name, RibbonBox... frames) {
        if ( this.animations.containsKey(name) ) {
            LOG.log(Level.WARNING, "Animation [{0}] existing.", name);
        } else {
            if (frames == null) {
                throw new IllegalArgumentException("Invalid animation");
            }
            this.animations.put(name, frames);
        }
    }

    /**
     * Implementation of the method <code>playAnimation(String, float)</code>.
     * @param name An {@code String} as key.
     * @param timePerFrame An {@code Float} as value.
     * @see AbstractAnimatedControl#playAnimation(java.lang.String, float) 
     */
    @Override
    public void playAnimation(String name, float timePerFrame) {
        RibbonBox[] rb = this.animations.get(name);
        if ( rb == null ) {
            LOG.log(Level.WARNING, "[{0}] animation not found.", name);
            return;
        }
        
        if ( !(name.equals(this.currentAnimationName)) ) {
            this.currentAnimationName = name;
            this.index = 0;
            
            if ( this.mat != null ) {
                this.mat.setTexture(getNameParam(), rb[0].getTexture());
            }
            
            this.sprite.updateMeshCoords(rb[0].getColumns(), rb[0].getRows());
            this.sprite.showIndex(rb[0].getFrames()[0]);
            
            this.currentAnimation   = rb;
            this.animationFrameTime = timePerFrame;
            
            this.currentIndex = 0;
            this.elapsedeTime = 0f;
            fireSpriteAnimationChangeListener();
        }
    }

    /**
     * Implementation of the method <code>controlUpdate(float)</code>.
     * @param tpf An {@code Float} as value.
     * @see AbstractAnimatedControl#controlUpdate(float) 
     */
    @Override
    protected void controlUpdate(float tpf) {
        if ( this.sprite != null
                && this.currentAnimation != null ) {
            
            int oldIndex = this.currentIndex;
            this.elapsedeTime += (tpf * this.speed);
            if ( this.elapsedeTime >= this.animationFrameTime ) {
                this.currentIndex++;
                
                if ( this.currentIndex >= this.currentAnimation[index].getFrames().length ) {
                    if (currentAnimation.length > 1) {
                        index++;
                        currentIndex = 0;
                        if (index >= currentAnimation.length) {
                            if (isLoop()) {
                                index = 0;
                            } else {
                                index        = currentAnimation.length - 1;
                                currentIndex = currentAnimation[index].getFrames().length - 1;
                            }
                        }
                    } else {
                        if (isLoop()) {
                            currentIndex = 0;
                        } else {
                            currentIndex = currentAnimation[index].getFrames().length - 1;
                        }
                    }
                }
                
                if (oldIndex != this.currentIndex) {
                    fireSpriteAnimationChangeListener();
                }
                
                this.elapsedeTime = 0f;
                this.sprite.showIndex(this.currentAnimation[index].getFrames()[this.currentIndex]);
                this.mat.setTexture(getNameParam(), this.currentAnimation[index].getTexture());
            }
        }
    }
}
