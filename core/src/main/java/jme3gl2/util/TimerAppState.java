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
package jme3gl2.util;

import com.jme3.app.state.AbstractAppState;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An <code>TimerAppState</code> is responsible for managing all {@link Timer}
 * running in the game.
 * 
 * <p>
 * In simpler terms, it is in charge of updating them, checking when they are
 * paused or resuming them.
 * </p>
 * 
 * @author wil
 * @version 1.0-SNAPSHOT
 *
 * @since 1.3.0
 */
public final 
class TimerAppState extends AbstractAppState {

    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(TimerAppState.class.getName());

    /** Map of the timers. */
    private final Map<String, EntryTimer> timerMap = new IdentityHashMap<>();

    /** Default delay. */
    private float defaultDelay = 0.5F;
    
    /**
     * Returns the default delay.
     * @return delay.
     */
    public float getDefaultDelay() {
        return defaultDelay;
    }

    /**
     * Sets a new delay that is used as default value for all <code>Timer</code>
     * without a delay set.
     * @param defaultDelay new default delay.
     */
    public void setDefaultDelay(float defaultDelay) {
        this.defaultDelay = defaultDelay;
    }
    
    /**
     * Method in charge of setting a timer to a specific {@link Timer}.
     * @param name timer codename.
     * @param state status that it will take.
     * @return {@code true} if the changes have been applied, otherwise {@code false}.
     */
    public boolean setState(String name, TimerState state) {
        EntryTimer entryTimer = this.timerMap.get(name);
        if ( entryTimer == null ) {
            LOG.log(Level.WARNING, "[ {0} ] Nonexistent timer.", name);
            return false;
        }
        
        Timer timer = entryTimer.getTimer();
        switch (state) {
            case Pause:
                timer.pause(true);
                break;
            case Resume:
                timer.pause(false);
                break;
            case Start:
                timer.start();
                break;
            case Stop:
                timer.stop();
                break;
            default:
                throw new AssertionError();
        }
        return true;
    }
    
    /**
     * Adds a new timer. Where it will use the default delay.
     * @param name timer codename.
     * @param timer timer.
     * @return the same {@link Timer}.
     */
    public Timer attachTimer(String name, Timer timer) {
        return this.attachTimer(name, timer, defaultDelay);
    }
    
    /**
     * Adds a new timer.
     * @param name timer codename.
     * @param timer timer.
     * @param delay delay of this timer.
     * @return the same {@link Timer}.
     */
    public Timer attachTimer(String name, Timer timer, float delay) {
        if (this.timerMap.containsKey(name)) {
            LOG.log(Level.WARNING, "[ {0} ] Timer exist.", name);
            return null;
        }
        EntryTimer entryTimer = new EntryTimer(delay, timer);
        entryTimer.validate();
        this.timerMap.put(name, entryTimer);
        return timer;
    }

    /**
     * Deletes a timer from the manager.
     * @param name timer codename.
     * @return the deleted timer if it exists, otherwise it will return a value
     * of {@code null}.
     */
    public Timer detachTimer(String name) {
        EntryTimer entryTimer = this.timerMap.remove(name);
        if ( entryTimer != null ) {
            return entryTimer.getTimer();
        }
        return null;
    }
    
    /**
     * Search for a timer by its codename.
     * @param name timer codename.
     * @return the {@link Timer}.
     */
    public Timer getTimer(String name) {
        EntryTimer entryTimer = this.timerMap.get(name);
        if ( entryTimer == null ) {
            return null;
        }
        return entryTimer.getTimer();
    }
    
    /**
     * Adds a new task to a specific timer.
     * @see Timer#addTask(jMe3GL2.util.TimerTask) 
     * @param name timer codename.
     * @param task task timer.
     * @return {@code true} if the task has been successfully added, otherwise {@code false}.
     */
    public boolean addTask(String name, TimerTask task) {
        EntryTimer entryTimer = this.timerMap.get(name);
        if ( entryTimer == null ) {
            LOG.log(Level.WARNING, "[ {0} ] Nonexistent timer.", name);
            return false;
        }
        return entryTimer.getTimer().addTask(task);
    }
    
    /**
     * Deletes a task at a specific timer.
     * @see Timer#removeTask(jMe3GL2.util.TimerTask)  
     * @param name timer codename.
     * @param task task timer.
     * @return {@code true} if the task has been deleted correctly, otherwise {@code false}.
     */
    public boolean removeTask(String name, TimerTask task) {
        EntryTimer entryTimer = this.timerMap.get(name);
        if ( entryTimer == null ) {
            LOG.log(Level.WARNING, "[ {0} ] Nonexistent timer.", name);
            return false;
        }
        return entryTimer.getTimer().removeTask(task);
    }
    
    /**
     * (non-JavaDoc)
     * @see AbstractAppState#update(float) 
     * @param tpf {@code float}
     */
    @Override
    public void update(float tpf) {
        for (final Map.Entry<String, EntryTimer> entry : this.timerMap.entrySet()) {
            EntryTimer timer = entry.getValue();
            if (timer == null) {
                continue;
            }
            
            timer.getTimer().update(tpf, timer.getDelay());
        }
    }

    /**
     * (non-JavaDoc)
     * @see AbstractAppState#setEnabled(boolean) 
     * @param enabled {@code boolean}
     */
    @Override
    public void setEnabled(boolean enabled) {
        for (final Map.Entry<String, EntryTimer> entry : this.timerMap.entrySet()) {
            EntryTimer timer = entry.getValue();
            if (timer == null) {
                continue;
            }
            
            timer.getTimer().pause(!enabled);
        }
        super.setEnabled(enabled);
    }
    
    /**
     * Method in charge of deleting all the timers registered in this time
     * manager/administrator.
     */
    public void detachAllTimer() {
        this.timerMap.clear();
    }
    
    /**
     * Internal class in charge of storing the timer and its delay in memory.
     */
    class EntryTimer {
        
        /** Timer delay. */
        private final float delay;
        
        /** Timer. */
        private final Timer timer;

        /**
         * Default constructor of the class <code>EntryTimer</code>.
         * @param delay timer delay
         * @param timer timer.
         */
        public EntryTimer(float delay, Timer timer) {
            this.delay = delay;
            this.timer = timer;
        }
        
        /**
         * It is responsible for validating the data.
         */
        public void validate() {
            if (timer == null) {
                throw new NullPointerException("The timer is null.");
            }
            if (delay < 0) {
                throw new IllegalArgumentException("[" + delay + "] Invalid delay.");
            }
        }

        /**
         * Returns the delay.
         * @return {@code float}
         */
        public float getDelay() {
            return delay;
        }

        /**
         * Returns the timer.
         * @return {@link Timer}
         */
        public Timer getTimer() {
            return timer;
        }
    }
}
