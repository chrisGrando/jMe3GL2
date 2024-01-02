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
package jme3gl2.awt;

/**
 * Class in charge of managing the screen resolution used by the game. In case
 * the game is running on a PC, the dimensions of the screen must be obtained
 * with the help of the libraries {@code AWT}.
 * 
 * Otherwise, a way must be found to obtain such resolutions.
 * 
 * @author wil
 * @version 1.1-SNAPSHOT
 * 
 * @since 2.0.0
 */
public interface Jme3GL2DisplaySystem {
    
    /**
     * Returns an array of resolutions to use.
     * 
     * @return {@link AWTResolution}.
     */
    public AWTResolution[] getResolutions();
    
    /**
     * Generates the resolution of the full screen, this resolution will be of
     * the screen where the game is running.
     * 
     * @return {@link AWTResolution}.
     */
    public AWTResolution getFullScreenResolution();
    
    /**
     * Method to determine if the full screen is supported by the equipment.
     * 
     * @return
     * {@code true} if it's full screen, otherwise it will be {@code false}.
     */
    public boolean isFullScreenSupported();
    
    /**
     * Method responsible for determining whether display changes are supported.
     * @return result
     */
    public boolean isDisplayChangeSupported();
}
