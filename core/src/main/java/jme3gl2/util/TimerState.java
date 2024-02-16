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

/**
 * An object of class <code>TimerState</code> is responsible for enumerating the
 * 3 possible states of a {@link Timer}:
 * <div>
 *  <ul>
 *   <li><b>Start:</b> initializing.</li>
 *   <li><b>Pause:</b> momentary pause.</li>
 *   <li><b>Stop:</b> dead status.</li>
 *  </ul>
 * </div>
 * 
 * @author wil
 * @version 1.0-SNAPSHOT
 * 
 * @since 2.0.0
 */
public enum TimerState {
    
    /**
     * Initialization state of the timer {@link Timer}, this calls the method
     * <code>start()</code>.
     */
    Start,
    
    /**
     * Temporary pause or stop state of the timer {@link Timer}, this calls the
     * method <code>pause(boolean pause)</code>.
     */
    Pause,
    
    /**
     * It does the opposite of {@code Pause}, i.e. it resumes the processes as
     * long as it has not died.
     */
    Resume,
    
    /**
     * Completely stops the timer processes.
     */
    Stop;
}
