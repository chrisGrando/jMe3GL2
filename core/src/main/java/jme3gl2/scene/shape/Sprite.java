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
package jme3gl2.scene.shape;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector2f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import java.io.IOException;

/**
 * An object of the class <code>Sprite</code> is a mesh that handles 2D models.
 * 
 * <p>
 * With a <code>Sprite</code> we can flip an image horizontally as well as
 * vertically or a combination of both.
 * </p>
 * 
 * <p>
 * This mesh uses a scale of <code>0.0-1.0</code> which can be translated as 0%
 * and 100%, keep this scale in mind when loading the texture of a 2D model.
 * </p>
 * 
 * <p>
 * <b>Example:</b>
 * <br>
 * If a texture has the following data:
 * </p>
 *
 * <pre><code>
 * Width  : 100px
 * Height :  50px
 * </code></pre>
 * 
 * <p>
 * We have to take a measurement as a reference to 100% of the dimensions to be
 * able to define and thus avoid deforming the texture of the geometry.
 * </p>
 * 
 * <p>
 * In this case we will take the width, with this data referenced the dimensions
 * of this mesh would be:
 * </p>
 * 
 * <pre><code>
 * width  : 1.0F --- Equivalent to 100%
 * height : 0.5F --- Equivalent to 50%
 * </code></pre>
 * 
 * <p>
 * This analysis is equivalent to saying that the width is 100%, while the
 * height is equivalent to 50% of the width. If the data had been taken
 * inversely, it would mean that the width is now 50% of the height, which
 * is 100%.
 * </p>
 * 
 * @author wil
 * @version 1.6-SNAPSHOT
 * 
 * @since 1.0.0
 */
public class Sprite extends Mesh implements Cloneable {
    
    /**
     * Object in charge of managing the transformation of the mesh, i.e. it's
     * verticals as well as the texture coordinates of this mesh.
     */
    private Transform transform;
    
    /**
     * {@code true} if you want to flip the mesh texture horizontally, otherwise
     * {@code false} if you want to return the texture to its original state.
     */
    private boolean flipH;
    
    /**
     * {@code true} if you want to flip the mesh texture in the vertical,
     * otherwise {@code false} if you want to return the texture to its original
     * state.
     */
    private boolean flipV;
    
    /**
     * Serialization only. Do not use.
     */
    public Sprite() {
    }

    /**
     * Instantiate a new object <code>Sprite</code>. Set the dimensions of the
     * mesh vertices.
     * 
     * @param width the desired width.
     * @param height the desired height.
     */
    public Sprite(float width, float height) {
        this(width, height, 1, 1, 0, 0);
    }

    /**
     * Instantiate a new object <code>Sprite</code>. Set the default mesh values.
     * 
     * @param width the desired width.
     * @param height the desired height.
     * @param columns desired number of columns. 
     * @param rows desired number of rows.
     * @param colPosition column position.
     * @param rowPosition row position.
     */
    public Sprite(float width, float height, int columns, int rows, int colPosition, int rowPosition) {
        this.transform = new Transform(width, height, columns, rows, colPosition, rowPosition);
        this.initializeMesh();
    }

    /**
     * Method in charge of initializing and/or configuring the mesh.
     */
    private void initializeMesh() {
        transform.setFlipType(Transform.FlipType.NonFlip);
        
        // Indexes. We define the order in which the mesh should be constructed
        short[] indexes = {2, 0, 1, 1, 3, 2};

        // Buffer configuration
        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(transform.getVertices()));
        setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(transform.getTextureCoordinates()));
        setBuffer(VertexBuffer.Type.Normal, 3, new float[]{0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1});
        setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        updateBound();
    }
    
    /**
     * (non-JavaDoc)
     * @see Mesh#jmeClone() 
     * @return Clone
     */
    @Override
    public Sprite jmeClone() {
        return (Sprite) super.jmeClone();
    }

    /**
     * (non-JavaDoc)
     * @see Mesh#deepClone() 
     * @return Clone
     */
    @Override
    public Sprite deepClone() {
        Sprite clon = (Sprite) super.deepClone();
        clon.transform = transform.clone();
        clon.flipH = flipH;
        clon.flipV = flipV;
        return clon;
    }

    /**
     * (non-JavaDoc)
     * @see Mesh#clone() 
     * @return Clone
     */
    @Override
    public Sprite clone() {
        Sprite clon = (Sprite) super.clone();
        clon.transform = transform.clone();
        clon.flipH = flipH;
        clon.flipV = flipV;
        return clon;
    }
    
    /**
     * Updates the size of this mesh.
     * @param width mesh width.
     * @param height mesh height.
     */
    public void updateVertexSize(float width, float height) {
        transform.setSize(width, height);
        
        // Buffer configuration
        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(transform.getVertices()));
        updateTextureCoords();
        updateBound();
    }

    /**
     * Method in charge of updating the coordinates of this mesh.
     * @param columns new number of columns.
     * @param rows new number of rows.
     */
    public void updateMeshCoords(int columns, int rows) {
        updateMeshCoords(columns, rows, 0, 0);
    }
    
    /**
     * Method in charge of updating the coordinates of this mesh.
     * @param columns new number of columns.
     * @param rows new number of rows.
     * @param colPosition initial position of the new columns.
     * @param rowPosition initial position of the new rows.
     */
    public void updateMeshCoords(int columns, int rows, int colPosition, int rowPosition) {
        int update = 0;
        if (transform.getColumns() != columns || transform.getRows() != rows) {
            transform.setCoords(columns, rows);
            update++;
        }
        
        if (transform.getColPosition() != colPosition 
                || transform.getRowPosition() != rowPosition) {
            update++;
        }
        
        if (update > 0) {
            setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(transform.getVertices()));
            updateTextureCoords(colPosition, rowPosition);
            updateBound();
        }
    }
    
    /**
     * Updates the texture coordinates.
     * <p>
     * This can be used when a change is made in the {@code Transform} of this
     * mesh.
     * </p>
     */
    public void updateTextureCoords() {
        updateTextureCoords(transform.getColPosition(), transform.getRowPosition());
    }
    
    /**
     * Updates the texture coordinates with new positions.
     * @param colPosition new column position.
     * @param rowPosition new row position.
     */
    public void updateTextureCoords(int colPosition, int rowPosition) {
        transform.setPosition(colPosition, rowPosition);
        
        if ( flipH && flipV ) {
            transform.setFlipType(Transform.FlipType.Flip_HV);
        } else if ( flipH ) {
            transform.setFlipType(Transform.FlipType.Flip_H);
        } else if ( flipV ) {
            transform.setFlipType(Transform.FlipType.Flip_V);
        } else {
            transform.setFlipType(Transform.FlipType.NonFlip);
        }

        setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(transform.getTextureCoordinates()));
    }

    /**
     * Sets the type of the horizontal flip.
     * @param flipH {@code true} or {@code false} to flip.
     */
    public void flipH(boolean flipH) {
        if (this.flipH != flipH) {
            this.flipH = flipH;
            this.updateTextureCoords();
        }
    }

    /**
     * Sets the type of the vertical flip.
     * @param flipV {@code true} or {@code false} to flip.
     */
    public void flipV(boolean flipV) {
        if (this.flipV != flipV) {
            this.flipV = flipV;
            this.updateTextureCoords();
        }
    }
    
    /**
     * Method in charge of scaling the dimensions of this mesh.
     * @param scale new scale.
     */
    public void scale(float scale) {
        this.scale(scale, scale);
    }
    
    /**
     * Method in charge of scaling the dimensions of this mesh.
     * @param scaleX scale on the axis {@code x}.
     * @param scaleY scale on the axis {@code y}.
     */
    public void scale(float scaleX, float scaleY) {
        final Vector2f newVs = new Vector2f(scaleX, scaleY);
        if (this.transform.getScale()
                .equals(newVs)) {
            return;
        }
        
        this.transform.setScale(newVs);
        
        // New vertices are established for the mesh.
        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(transform.getVertices()));
        
        // Update the coordinates.
        updateTextureCoords();
        updateBound();
    }

    /**
     * Returns the type of flip in the horizontal.
     * @return flip type.
     */
    public boolean isFlipH() {
        return flipH;
    }

    /**
     * Returns the type of flip in the vertical.
     * @return flip type.
     */
    public boolean isFlipV() {
        return flipV;
    }
    
    /**
     * Method in charge of distorting the texture of this mesh through an index.
     * @param index new update index.
     */
    public void showIndex(int index) {
        updateTextureCoords(index % transform.getColumns(), index / transform.getColumns());
    }
    
    /**
     * Method in charge of distorting the texture of this mesh through an index.
     * @param colPosition new column position.
     * @param rowPosition new row position.
     */
    public void showIndex(int colPosition, int rowPosition) {
        updateTextureCoords(colPosition, rowPosition);
    }

    /**
     * Returns the transformer of this mesh.
     * @return Transform.
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * (non-JavaDoc).
     * @see Transform#getWidth() 
     * @return Float.
     */
    public float getWidth() {
        return transform.getWidth();
    }

    /**
     * (non-JavaDoc).
     * @see Transform#getHeight() 
     * @return Float.
     */
    public float getHeight() {
        return transform.getHeight();
    }

    /**
     * (non-JavaDoc).
     * @see Transform#getScale() 
     * @return Vector2f.
     */
    public Vector2f getScale() {
        return transform.getScale();
    }

    /**
     * (non-JavaDoc).
     * 
     * @param im JmeImporter
     * @see Mesh#read(com.jme3.export.JmeImporter) 
     * 
     * @throws IOException exception.
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        transform = (Transform) in.readSavable("transform", new Transform());
        flipH = in.readBoolean("flipH", Boolean.FALSE);
        flipV = in.readBoolean("flipV", Boolean.FALSE);
        initializeMesh();
    }

    /**
     * (non-JavaDoc).
     * 
     * @param ex JmeExporter.
     * @see Mesh#write(com.jme3.export.JmeExporter) 
     * 
     * @throws IOException exception.
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);        
        OutputCapsule out = ex.getCapsule(this);
        out.write(transform, "transform", null);
        out.write(flipH, "flipH", Boolean.FALSE);
        out.write(flipV, "flipV", Boolean.FALSE);
    }
}
