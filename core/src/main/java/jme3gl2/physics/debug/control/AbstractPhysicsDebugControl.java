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

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import jme3gl2.physics.control.BasePhysicsControl;
import jme3gl2.physics.control.PhysicsBody2D;

/**
 * Abstract class <code>AbstractPhysicsDebugControl</code> in charge of controlling
 * the geometry for the debugging of physical bodies.
 * 
 * @author wil
 * @version 1.0-SNAPSHOT 
 * 
 * @since 2.5.0
 * @param <E> body type
 */
public abstract 
class AbstractPhysicsDebugControl<E extends PhysicsBody2D> extends AbstractControl implements BasePhysicsControl<E> {

    /** Physical body. */
    protected final E body;

    /**
     * Class constructor of <code>PhysicsDebugBody2D</code> where a physical body
     * to be debugged is established.
     * @param body physical body.
     */
    public AbstractPhysicsDebugControl(E body) {
        this.body = body;
    }
    
    /**
     * (non-JavaDoc)
     * @see com.jme3.scene.control.AbstractControl#controlUpdate(float)
     * @param tpf time per counter per second.
     */
    @Override
    protected void controlUpdate(float tpf) {
        applyPhysicsLocation(this.body);
        applyPhysicsRotation(this.body);
    }

    /**
     * (non-JavaDoc)
     * @see BasePhysicsControl#getJmeObject() 
     * 
     * @param <T> spatial type
     * @return Spatial
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Spatial> T getJmeObject() {
        return (T) spatial;
    }

    /**
     * (non-JavaDoc)
     * @see com.jme3.scene.control.AbstractControl#controlRender(com.jme3.renderer.RenderManager, com.jme3.renderer.ViewPort)
     * @param rm render-manager
     * @param vp view-port
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
