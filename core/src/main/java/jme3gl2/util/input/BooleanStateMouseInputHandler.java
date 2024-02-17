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
package jme3gl2.util.input;

import com.jme3.math.Vector2f;

/**
 * Class <code>BooleanStateMouseInputHandler</code> which exists from the parent
 * class {@link AbstractMouseInputHandler} where it manages the logical state of
 * an input, active or not.
 * 
 * @author wil
 * @version 1.0-SNAPSHOT
 * @since 2.0.0
 */
public class BooleanStateMouseInputHandler extends AbstractMouseInputHandler {

    /** If the key status is active (pressed).  */
    private boolean active;
    
    /** Cursor position. */
    private Vector2f location;
    
    /** {@code true} if the active status has been handled. */
    private boolean hasBeenHandled;

    /**
     * Instantiate a new object of the class <code>BooleanStateMouseInputHandler</code>.
     * @param mouseTrigger data input.
     */
    public BooleanStateMouseInputHandler(MouseTrigger mouseTrigger) {
        super(mouseTrigger);
    }

    /**
     * (non-JavaDoc)
     * @param point vector2f
     * @see AbstractMouseInputHandler#onMousePressed(com.jme3.math.Vector2f) 
     */
    @Override
    protected void onMousePressed(Vector2f point) {
        boolean active0 = this.active;

        this.active = true;
        this.location = point;

        // si el estado pasó de inactivo a activo
        // marca que necesita ser manejado
        if (!active0) {
            this.hasBeenHandled = false;
        }
    }

    /**
     * (non-JavaDoc)
     * @see AbstractMouseInputHandler#onMouseRelease() 
     */
    @Override
    protected void onMouseRelease() {
        this.active = false;
    }

    /**
     * (non-JavaDoc)
     * @param flag boolean
     * @see AbstractMouseInputHandler#setEnabled(boolean) 
     */
    @Override
    public void setEnabled(boolean flag) {
        super.setEnabled(flag);
        if (!flag) {
            this.clearState();
        }
    }

    /**
     * (non-JavaDoc)
     * @see AbstractMouseInputHandler#uninstall() 
     */
    @Override
    public void uninstall() {
        super.uninstall();
        this.clearState();
    }

    /**
     * Clears the status of this data input.
     */
    private void clearState() {
        this.active = false;
        this.location = null;
        this.hasBeenHandled = false;
    }

    /**
     * Returns the current cursor position.
     * @return position.
     */
    public Vector2f getMouseLocation() {
        return this.location;
    }

    /**
     * (non-JavaDoc)
     * @see AbstractMouseInputHandler#isActive() 
     * @return boolean
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Method in charge of managing if the input is active and has not been
     * handled.
     * @return boolean.
     */
    public boolean isActiveButNotHandled() {
        if (this.hasBeenHandled) {
            return false;
        }

        return this.active;
    }

    /**
     * Sets the status of the input.
     * @param hasBeenHandled status.
     */
    public void setHasBeenHandled(boolean hasBeenHandled) {
        this.hasBeenHandled = hasBeenHandled;
    }

    /**
     * (non-JavaDoc)
     * @param start vector2f
     * @param current vector2f
     * @see AbstractMouseInputHandler#onMouseDrag(com.jme3.math.Vector2f, com.jme3.math.Vector2f) 
     */
    @Override
    protected void onMouseDrag(Vector2f start, Vector2f current) { }

    /**
     * (non-JavaDoc)
     * @param rotation float
     * @see AbstractMouseInputHandler#onMouseWheel(double) 
     */
    @Override
    protected void onMouseWheel(double rotation) { }
}
