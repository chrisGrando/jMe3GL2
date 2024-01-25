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
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class <code>AnimatedSprite</code> is responsible for providing multiple
 * texture handling control.
 * <br>
 * <b>With this class we can create animations.</b>
 * 
 * @author wil
 * @version 1.5-SNAPSHOT
 *
 * @since 1.0.0
 */
public class AnimatedSprite extends AbstractAnimatedControl<Texture> {
    
    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(AnimatedSprite.class.getName());

    /** 2D model material. */
    private Material mat;
    
    /**
     * {@code true} if the mesh of the model is resized according to the
     * dimensions of its texture/sprite, otherwise {@code false}.
     */
    protected boolean dynamic;
    
    /**
     * Scaling type.
     * @see ScaleType
     */
    protected ScaleType scaleType;

    /** Original mesh dimensions. */
    protected Vector2f originalDim;
    
    /**
     * Default class constructor {@code AnimatedSprite}.
     */
    public AnimatedSprite() {
    }

    /**
     * Class constructor <code>AnimatedSprite</code>.
     * @param lighting boolean.
     */
    public AnimatedSprite(boolean lighting) {
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
        originalDim = new Vector2f(sprite.getWidth(), sprite.getHeight());
    }

    /**
     * Implementation of the method <code>addAnimation(String, E[])</code>.
     * @param name An {@code String} as key.
     * @param frames An array of {@code Texture} as value.
     */
    @Override
    public void addAnimation(String name, Texture[] frames) {
        if ( this.animations.containsKey(name) ) {
            LOG.log(Level.WARNING, "Animation [{0}] existing.", name);
        } else {
            this.animations.put(name, frames);
        }
    }

    /**
     * Implementation of the method <code>playAnimation(String, float)</code>.
     * @param name An {@code String} as key.
     * @param timePerFrame An {@code Float} as value.
     */
    @Override
    public void playAnimation(String name, float timePerFrame) {
        Texture[] text = this.animations.get(name);
        if ( text == null ) {
            LOG.log(Level.WARNING, "[{0}] animation not found.", name);
            return;
        }

        if ( !(name.equals(this.currentAnimationName)) ) {
            this.currentAnimationName = name;

            if ( this.mat != null ) {
                if (dynamic) {
                    Vector2f newSize = getDynamicSize(text[0]);
                    sprite.updateVertexSize(newSize.x, newSize.y);
                }
                
                this.mat.setTexture(getNameParam(), text[0]);
            }

            this.currentAnimation = text;
            this.animationFrameTime = timePerFrame;
            this.currentIndex = 0;
            this.elapsedeTime = 0.0F;
            fireSpriteAnimationChangeListener();
        }
    }

    /**
     * Sets a new animation state.
     * @param dynamic boolean
     * @see AnimatedSprite#dynamic
     */
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * Sets a new type of scaling.
     * @param scaleType scale type.
     */
    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
    }
    
    /**
     * Method for managing the dynamic dimensions of an animated 2D model.
     * @param text texture.
     * @return new size.
     */
    private Vector2f getDynamicSize(Texture text) {
        float max, min;
        float mm_dim;
        if (null == scaleType) {
            throw new NullPointerException("LockScaling is Null.");
        } else switch (scaleType) {
            case GL2_HEIGHT:
                max = text.getImage().getHeight();
                mm_dim = originalDim.y;
                return new Vector2f((text.getImage().getWidth() / max) *  mm_dim, mm_dim);
            case GL2_WIDTH:
                max = text.getImage().getWidth();
                mm_dim = originalDim.x;
                return new Vector2f(mm_dim, (text.getImage().getHeight() / max) * mm_dim);
            case GL2_MAX:
                max = text.getImage().getHeight();
                min = text.getImage().getWidth();
                if (max >= min) {
                    mm_dim = originalDim.y;
                    return new Vector2f((min / max) * mm_dim, mm_dim);
                } else {
                    mm_dim = originalDim.x;
                    return new Vector2f(mm_dim, (max / min) * mm_dim);
                }
            case GL2_MIN:
                max = text.getImage().getHeight();
                min = text.getImage().getWidth();
                if (max >= min) {
                    mm_dim = originalDim.x;
                    return new Vector2f(mm_dim, (max / min) * mm_dim);
                } else {
                    mm_dim = originalDim.y;
                    return new Vector2f((min / max) * mm_dim, mm_dim);
                }
            default:
                throw new AssertionError();
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
                
                if ( this.currentIndex >= this.currentAnimation.length ) {
                    if (isLoop()) {
                        this.currentIndex = 0;
                    } else {
                        this.currentIndex = this.currentAnimation.length - 1;
                    }
                }
                
                if (oldIndex != this.currentIndex) {
                    fireSpriteAnimationChangeListener();
                }
                
                if (dynamic) {
                    Vector2f newSize = getDynamicSize(this.currentAnimation[this.currentIndex]);
                    sprite.updateVertexSize(newSize.x, newSize.y);
                }
                
                this.elapsedeTime = 0f;
                this.mat.setTexture(getNameParam(), this.currentAnimation[this.currentIndex]);
            }
        }
    }
}
