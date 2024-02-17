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

import com.jme3.input.InputManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class <code>AbstractInputHandler</code> where the {@link InputHandler}
 * interface is implemented and implements some of its methods to have a base
 * template.
 * 
 * @author wil
 * @version 1.0-SNAPSHOT
 * @since 2.0.0
 */
public abstract class AbstractInputHandler implements InputHandler {

    /** Input status. */
    private boolean enabled;
    
    /** Status of the dependencies of this input. */
    private boolean additive;
    
    /**
     * <code>initialized</code> is set to {@code true} when method
     * <code>initialize(InputManager im)</code> is called by the
     * <code>InputHandlerAppState</code> state manager when adding a new input.
     */
    private boolean initialized;
    
    /** Dependency list for this input. */
    private final List<InputHandler> dependentBehaviors;
    
    /** Ticket manager {@code jme3}. */
    private InputManager inputManager;

    /**
     * Default constructor of the class <code>AbstractInputHandler</code>.
     */
    public AbstractInputHandler() {
        this.dependentBehaviors = new ArrayList<>();
        this.initialized = false;
        this.enabled     = true;
        this.additive    = false;
    }

    /**
     * (non-JavaDoc)
     * @param im input manager
     * @see InputHandler#initialize(com.jme3.input.InputManager) 
     */
    @Override
    public void initialize(InputManager im) {
        boolean result = false;
        if (im != null) {
            result = true;
        }
        
        this.inputManager = im;
        this.initialized  = result;
    }

    /**
     * (non-JavaDoc)
     * @see InputHandler#isInitialized() 
     * @return boolean
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns the input manager.
     * @return input manager.
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * (non-JavaDoc)
     * @see InputHandler#isEnabled() 
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * (non-JavaDoc)
     * @param flag boolean
     * @see InputHandler#setEnabled(boolean) 
     */
    @Override
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    /**
     * (non-JavaDoc)
     * @see InputHandler#getDependentBehaviors() 
     * @return list
     */
    @Override
    public List<InputHandler> getDependentBehaviors() {
        return this.dependentBehaviors;
    }

    /**
     * (non-JavaDoc)
     * @see InputHandler#isDependentBehaviorActive() 
     * @return boolean
     */
    @Override
    public boolean isDependentBehaviorActive() {
        boolean result = false;
        for (InputHandler behavior : this.dependentBehaviors) {
            if (behavior.isActive()) {
                result = true;
            }
        }
        if (this.additive) {
            return !result;
        }
        return result;
    }

    /**
     * (non-JavaDoc)
     * @see InputHandler#isDependentBehaviorsAdditive() 
     * @return boolean
     */
    @Override
    public boolean isDependentBehaviorsAdditive() {
        return this.additive;
    }

    /**
     * (non-JavaDoc)
     * @param flag boolean
     * @see InputHandler#setDependentBehaviorsAdditive(boolean) 
     */
    @Override
    public void setDependentBehaviorsAdditive(boolean flag) {
        this.additive = flag;
    }
}
