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

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An object of the class <code>RibbonBox</code> is in charge of managing the
 * texture and frames of the animation by means of the control 
 * {@link RibbonBoxAnimationSprite}.
 * 
 * @author wil
 * @version 1.0-SNAPSHOT
 *
 * @since 2.0.0
 */
public class RibbonBox implements Savable, Cloneable {

    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(RibbonBox.class.getName());

    /** Vector in charge of storing the columns and files of the mesh. */
    private Vector2f columnsAndRows = new Vector2f(1.0F, 1.0F);
    
    /** Texture of this ribbon. */
    private Texture texture;
    
    /** Animation frames. */
    private int[] frames;

    /**
     * {@code true} if this ribbon is being used for animation of a 2D model,
     * otherwise {@code false}.
     */
    private boolean inAction;
    
    /**
     * Default constructor.
     */
    protected RibbonBox() {
        this(null, new int[0], 0, 0);
    }
    
    /**
     * Instantiate a new object of the class <code>RibbonBox</code> to generate
     * an animation ribbon.
     * 
     * @param texture animated texture.
     * @param frames animation frames.
     * @param columns number of columns for the new ribbon.
     * @param rows number of rows for the new ribbon.
     */
    public RibbonBox(Texture texture, int[] frames, int columns, int rows) {
        this.columnsAndRows.set(columns, rows);
        this.texture = texture;
        this.frames = frames;
    }

    /**
     * Method in charge of generating a clone of this object.
     * @see Object#clone() 
     * @return object clone.
     */
    @Override
    public RibbonBox clone() throws CloneNotSupportedException {
        RibbonBox clon = (RibbonBox) super.clone();
        clon.texture = texture != null ? texture.clone() : null;
        clon.frames  = frames  != null ? frames.clone()  : null;
        return clon;
    }

    /**
     * Sets the action status of this ribbon.
     * @param inAction status.
     */
    public void setInAction(boolean inAction) {
        this.inAction = inAction;
    }

    /**
     * Sets a new animated texture.
     * @param texture new texture.
     */
    public void setTexture(Texture texture) {
        if (inAction) {
            LOG.log(Level.WARNING, "This animation is in use.");
        } else {
            this.texture = texture;
        }
    }

    /**
     * Sets a new frame array for the animation.
     * @param frames new frames.
     */
    public void setFrames(int[] frames) {
        if (inAction) {
            LOG.log(Level.WARNING, "This animation is in use.");
        } else {
            this.frames = frames;
        }
    }
    
    /**
     * Returns the current texture.
     * @return texture.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Returns the frames of the current animation.
     * @return animation frames.
     */
    public int[] getFrames() {
        return frames;
    }

    /**
     * Method in charge of managing the status of this ribbon.
     * @see RibbonBox#inAction
     * @return boolean.
     */
    public boolean inAction() {
        return inAction;
    }
    
    /**
     * Returns the number of columns.
     * @return columns.
     */
    public int getColumns() {
        return (int) columnsAndRows.x;
    }
    
    /**
     * Returns the number of rows.
     * @return rows.
     */
    public int getRows() {
        return (int) columnsAndRows.y;
    }

    /**
     * (non-JavaDoc).
     * 
     * @param ex JmeExporter.
     * @see Savable#write(com.jme3.export.JmeExporter) 
     * 
     * @throws IOException exception.
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);
        out.write(texture, "texture", null);
        out.write(frames, "frames", null);
        out.write(columnsAndRows , "columnsAndRows ", null);
    }

    /**
     * (non-JavaDoc).
     * 
     * @param im JmeImporter
     * @see Savable#read(com.jme3.export.JmeImporter) 
     * 
     * @throws IOException exception.
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        columnsAndRows  = (Vector2f) in.readSavable("columnsAndRows ", columnsAndRows);
        texture = (Texture) in.readSavable("texture", texture);
        frames  = in.readIntArray("frames", frames);
    }
}
