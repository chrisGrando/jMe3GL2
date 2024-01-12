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

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

import jme3gl2.physics.control.PhysicsBody2D;
import jme3gl2.physics.debug.Dyn4jDebugAppState;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dyn4j.collision.Bounds;
import org.dyn4j.dynamics.Settings;

/**
 * An <code>Dyn4jAppState</code> is the state in charge of managing the
 * physics engine <code>Dyn4j</code>.
 * <p>
 * Please note that the engine {@code dyn4j} is independent of {@code jme3},
 * so you must have the knowledge to manage both.
 * </p>
 * 
 * @author wil
 * @version 1.0.5-SNAPSHOT
 * 
 * @since 1.0.0
 * @param <E> the type {@code PhysicsBody}.
 */
@SuppressWarnings(value = {"unchecked"})
public class Dyn4jAppState<E extends PhysicsBody2D> extends AbstractAppState {

    /** Waiting time in microseconds. */
    private static final long TIME_STEP_IN_MICROSECONDS = (long) (Settings.DEFAULT_STEP_FREQUENCY * 1000L);
    
    /**
     * See {@link Application} for more information.
     */
    protected Application app = null;
    
    /** Status manager. */
    protected AppStateManager stateManager = null;
    
    /** Initial body capacity of the world. */
    protected Integer initialCapacity = null;
    
    /** Initial capacity for joints. */
    protected Integer initialJointCapacity = null;
    
    /** Limit of the world.
     * <p>
     * Note that if a limit is set, the physics engine will disable the world
     * when the limit is reached.
     * </p>
     */
    protected Bounds bounds = null;
    
    /**
     * See {@link PhysicsSpace} for more information.
     */
    protected PhysicsSpace<E> physicsSpace = null;
    
    /** FPS for the engine. */
    protected float tpf = 0;
    
    /** FPS accumulated for the engine. */
    protected float tpfSum = 0;
    
    // MultiTreading Fields //    
    /** See {@link ThreadingType} for more information. */
    protected ThreadingType threadingType = null;
    
    /** (non-javadoc) */
    protected ScheduledThreadPoolExecutor executor;
    /** (non-javadoc) */
    private final Runnable parallelPhysicsUpdate = () -> {
         if (!isEnabled()) {
             return;
         }
         
         Dyn4jAppState.this.physicsSpace.updateFixed(Dyn4jAppState.this.tpfSum);
         Dyn4jAppState.this.tpfSum = 0;
    };
    
     // Debugger.
     /** State-debugging. */
     protected Dyn4jDebugAppState<E> debugAppState;
     
     /**
      * <code>true</code> if the debugger state is activated for the physical
      * bodies and joints of the physical-space, otherwise
      * <code>false</code> to deactivate it.
      */
     protected boolean debug;
     
    /**
     * Instantiates a new object <code>Dyn4jAppState</code> with the
     * default values.
     */
    public Dyn4jAppState() {
        this(null, null, ThreadingType.PARALLEL);
    }

    /**
     * Instantiates a new object <code>Dyn4jAppState</code> and initialize it
     * with the default values.
     * 
     * @param bounds limit of the world.
     */
    public Dyn4jAppState(final Bounds bounds) {
        this(null, null, bounds, ThreadingType.PARALLEL);
    }

    /**
     * Instantiates a new object <code>Dyn4jAppState</code> and initialize it
     * with the default values.
     * 
     * @param initialCapacity initial capacity (bodies in the world).
     * @param initialJointCapacity initial capacity of the joints.
     */
    public Dyn4jAppState(final Integer initialCapacity, final Integer initialJointCapacity) {
        this(initialCapacity, initialJointCapacity,null, ThreadingType.PARALLEL);
    }

    /**
     * Instantiates a new object <code>Dyn4jAppState</code> and initialize it
     * with the default values.
     * 
     * @param initialCapacity initial capacity (bodies in the world).
     * @param initialJointCapacity initial capacity of the joints.
     * @param bounds limit of the world.
     */
    public Dyn4jAppState(final Integer initialCapacity, final Integer initialJointCapacity, final Bounds bounds) {
        this(initialCapacity, initialJointCapacity, bounds, ThreadingType.PARALLEL);
    }

    /**
     * Instantiates a new object <code>Dyn4jAppState</code> and initialize it
     * with the default values.
     * 
     * @param threadingType engine integration type.
     */
    public Dyn4jAppState(final ThreadingType threadingType) {
        this(null, null, threadingType);
    }

    /**
     * Instantiates a new object <code>Dyn4jAppState</code> and initialize it
     * with the default values.
     * 
     * @param bounds limit of the world.
     * @param threadingType engine integration type.
     */
    public Dyn4jAppState(final Bounds bounds, final ThreadingType threadingType) {
        this(null, null, bounds, threadingType);
    }

    /**
     * Instantiates a new object <code>Dyn4jAppState</code> and initialize it
     * with the default values.
     * 
     * @param initialCapacity initial capacity (bodies in the world).
     * @param initialJointCapacity initial capacity of the joints.
     * @param threadingType engine integration type.
     */
    public Dyn4jAppState(final Integer initialCapacity, final Integer initialJointCapacity, final ThreadingType threadingType) {
        this(initialCapacity, initialJointCapacity, null, threadingType);
    }

    /**
     * Instantiates a new object <code>Dyn4jAppState</code> and initialize it
     * with the default values.
     * 
     * @param initialCapacity initial capacity (bodies in the world).
     * @param initialJointCapacity initial capacity of the joints.
     * @param bounds limit of the world.
     * @param threadingType engine integration type.
     */
    public Dyn4jAppState(final Integer initialCapacity, final Integer initialJointCapacity, final Bounds bounds, final ThreadingType threadingType) {
        this.threadingType = threadingType;
        this.initialCapacity = initialCapacity;
        this.bounds = bounds;
    }

    /**
     * (non-JavaDoc).
     * 
     * @param stateManager AppStateManager
     * @param app Application
     * @see AbstractAppState#initialize(com.jme3.app.state.AppStateManager, com.jme3.app.Application) 
     */
    @Override
    public void initialize(final AppStateManager stateManager, final Application app) {
        this.app = app;
        this.stateManager = stateManager;

        // Initiate physics-related objects.
        startPhysics();

        super.initialize(stateManager, app);
    }

    /**
     * Initializes the physics.
     */
    private void startPhysics() {
        if (this.initialized) {
            return;
        }

        if (this.threadingType == ThreadingType.PARALLEL) {
            startPhysicsOnExecutor();
        } else {
            this.physicsSpace = new PhysicsSpace<>(initialCapacity, initialJointCapacity, bounds);
        }

        this.initialized = true;
    }
    
    /**
     * Initializes the physics so that it runs in parallel with the engine
     * {@code jme3}, i.e. with our '2D models'.
     */
    private void startPhysicsOnExecutor() {
        if (this.executor != null) {
            this.executor.shutdown();
        }
        this.executor = new ScheduledThreadPoolExecutor(1);

        final Callable<Boolean> call = () -> {
            Dyn4jAppState.this.physicsSpace = new PhysicsSpace(Dyn4jAppState.this.initialCapacity, Dyn4jAppState.this.initialJointCapacity,
                    Dyn4jAppState.this.bounds);
            return true;
        };

        try {
            this.executor.submit(call).get();
        } catch (final InterruptedException | ExecutionException ex) {
            Logger.getLogger(Dyn4jAppState.class.getName()).log(Level.SEVERE, null, ex);
        }

        schedulePhysicsCalculationTask();
    }

    /**
     * (non-JavaDoc).
     */
    private void schedulePhysicsCalculationTask() {
        if (this.executor != null) {
            this.executor.scheduleAtFixedRate(this.parallelPhysicsUpdate, 0l, TIME_STEP_IN_MICROSECONDS,
                    TimeUnit.MICROSECONDS);
        }
    }

    /**
     * (non-JavaDoc).
     * @param stateManager AppStateManager
     * @see AbstractAppState#stateAttached(com.jme3.app.state.AppStateManager) 
     */
    @Override
    public void stateAttached(final AppStateManager stateManager) {
        // Initiate physics-related objects if appState is not initialized.
        if (!this.initialized) {
            startPhysics();
        }
        
        // Check if the debugger mode is enabled and start the debugger state.
        if (this.debug) {
            this.stateManager = stateManager;
            prepareDebugger(true);
        }

        super.stateAttached(stateManager);
    }
    
    /**
     * (non-JavaDoc).
     * @param tpf float
     * @see AbstractAppState#update(float) 
     */
    @Override
    public void update(final float tpf) {
        if (!isEnabled()) {
            return;
        }
        
        if (this.debug && this.debugAppState == null && this.physicsSpace != null) {
            prepareDebugger(true);
        } else if (!this.debug && this.debugAppState != null) {
            destroyDebugger();
        }
        
        this.tpf = tpf;
        this.tpfSum += tpf;
    }
    
    /**
     * (non-JavaDoc).
     * @param rm RenderManager
     * @see AbstractAppState#render(com.jme3.renderer.RenderManager) 
     */
    @Override
    public void render(final RenderManager rm) {
        if (null == threadingType) {
            /* (non-Code). */
        } else switch (threadingType) {
            case PARALLEL:
                executor.submit(parallelPhysicsUpdate);
                break;
            case SEQUENTIAL:
                final float timeStep = isEnabled() ? this.tpf * this.physicsSpace.getSpeed() : 0;
                this.physicsSpace.updateFixed(timeStep);
                break;
            default:
                break;
        }
    }

    /**
     * (non-JavaDoc).
     * @param enabled boolean
     * @see AbstractAppState#setEnabled(boolean) 
     */
    @Override
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            schedulePhysicsCalculationTask();

        } else if (this.executor != null) {
            this.executor.remove(this.parallelPhysicsUpdate);
        }
        super.setEnabled(enabled);
    }

    /**
     * Method in charge of cleaning the state of physics.
     */
    @Override
    public void cleanup() {
        destroyDebugger();
        if (this.executor != null) {
            this.executor.shutdown();
            this.executor = null;
        }

        this.physicsSpace.destroy();

        super.cleanup();
    }

    /**
     * Returns the physics space.
     * @return physics engine.
     */
    public PhysicsSpace<E> getPhysicsSpace() {
        return this.physicsSpace;
    }

    /**
     * Method in charge of returning the debugger status.
     * @return <code>true</code> if enabled, otherwise it will return
     * <code>false</code> if it is disabled.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Method in charge of activating or deactivating the debugger of the
     * physical bodies.
     * 
     * @param debug <code>true</code> to enable the state, otherwise
     * <code>false</code> to disable.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    /**
     * Method in charge of preparing the debugger for the physical bodies.
     * @param attach <code>true</code> if you want to add it to the status
     * manager, otherwise <code>false</code>.
     */
    protected void prepareDebugger(boolean attach) {
        if (this.debugAppState == null) {
            this.debugAppState = new Dyn4jDebugAppState<>(this.physicsSpace);
            
            if (attach) {
                this.stateManager.attach(this.debugAppState);
            }
        }
    }
    
    /**
     * Method in charge of destroying the debugger state.
     */
    protected void destroyDebugger() {
        if (this.debugAppState != null) {
            this.stateManager.detach(this.debugAppState);
            this.debugAppState = null;
        }
    }
}
