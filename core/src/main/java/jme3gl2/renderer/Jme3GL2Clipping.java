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

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * An object of the class <code>Jme3GL2Clipping</code> is responsible for managing
 * the 2D camera clipping.
 * <p>
 * In this class is where you define the minimum and maximum clipping, both a
 * backward and forward movement of the camera with respect to the target you have.
 * </p>
 * 
 * @author wil
 * @version 1.0-SNAPSHOT 
 * 
 * @since 2.0.0
 */
public class Jme3GL2Clipping {
    
    /** Minimum clipping. */
    private Vector2f minimum;
    
    /** Maximum clipping. */
    private Vector2f maximum;
    
    /**
     * Backward or forward point.
     * <p>
     * That is, the offset that the camera will have with respect to it's target
     * (if it has one set).
     * </p>
     */
    private Vector2f offset;

    /**
     * Class constructor <code>Jme3GL2Clipping</code> where the clipping and
     * offset objects are initialized.
     */
    public Jme3GL2Clipping() {
        this.maximum = new Vector2f(0.0F, 0.0F);
        this.maximum = new Vector2f(0.0F, 0.0F);
        this.offset  = new Vector2f(0.0F, 0.0F);
    }
    
    /**
     * Method responsible for calculating the camera position within the
     * clipping ranges.
     * 
     * @param x position of the object on the axis {@code x}.
     * @param y position of the object on the axis {@code y}.
     * @return new calculated position.
     */
    public Vector2f clamp(float x, float y) {
        if (isClipping()) {
            return new Vector2f(FastMath.clamp((x + offset.x), minimum.x, maximum.x), 
                                FastMath.clamp((y + offset.y), minimum.y, maximum.y));
        }
        return new Vector2f(x + offset.x, y + offset.y);
    }

    /**
     * Determine if there is a clipping to be applied.
     * @return boolean.
     */
    public boolean isClipping() {
        return minimum != null && maximum != null;
    }
    
    /**
     * Establishes a new clipping <code>Minimum</code>.
     * @param minimum new clipping.
     */
    public void setMinimum(Vector2f minimum) {
        this.minimum = minimum;
    }

    /**
     * Establishes a new clipping <code>Maximum</code>.
     * @param maximum new clipping.
     */
    public void setMaximum(Vector2f maximum) {
        this.maximum = maximum;
    }

    /**
     * Method in charge of establishing a new offset. If the value is
     * <code>null</code>, such a offset will be of <code>0</code>.
     * 
     * @param offset new offset point.
     */
    public void setOffset(Vector2f offset) {
        if (offset == null) {
            this.offset.zero();
        } else {
            this.offset = offset;
        }
    }

    /**
     * Returns a minimum clipping value.
     * @return clipping.
     */
    public Vector2f getMinimum() {
        return minimum;
    }

     /**
     * Returns a maximum clipping value.
     * @return clipping.
     */
    public Vector2f getMaximum() {
        return maximum;
    }

    /**
     * Returns the value of the offset.
     * @return offset.
     */
    public Vector2f getOffset() {
        return offset;
    }
}
