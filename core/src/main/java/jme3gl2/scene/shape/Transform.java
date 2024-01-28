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
import com.jme3.export.Savable;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import java.io.IOException;

/**
 * An object <code>Transform</code> is in charge of managing the transformation
 * of the mesh <code>Sprite</code> for 2D models.
 * <p>
 * With this class you can manipulate the vertices of the mesh, as well as the
 * coordinates of the mesh so that it can be flipped.
 * </p>
 * <p>
 * <b>NOTE:</b> Do not directly change the properties of your mesh with this
 * class, use the methods provided by the mesh <code>Sprite</code>.
 * </p>.
 * 
 * @author wil
 * @version 1.5-SNAPSHOT
 * 
 * @since 1.0.0
 */
public final 
class Transform implements Savable, Cloneable {
    
    /**
     * An {@code FlipType} is an internal class in charge of the enumeration of
     * the different types of flips that the mesh {@code Sprite} can have.
     */
    public static enum FlipType {
        
        /**
         * Flip horizontally, making the object face the other way (left/right).
         */
        Flip_H, 
        
        /**
         * Flip vertically, making the object face the other way (up/down).
         */
        Flip_V, 
        
        /**
         * When you want to flip horizontally and vertically at the same time,
         * you can use this option.
         */
        Flip_HV, 
        
        /**
         * Leave the texture coordinates of the mesh object as they are.
         */
        NonFlip;
    }
    
    /**
     * Vector in charge of storing mesh dimensions of the {@code Sprite} in 2D.
     */
    private Vector2f size = new Vector2f(0.0F, 0.0F);
    
    /** Vector in charge of storing the columns and rows of the mesh. */
    private Vector2f columnsAndRows = new Vector2f(1.0F, 1.0F);
    
    /** Vector in charge of storing the positions. */
    private Vector2f position = new Vector2f(0.0F, 0.0F);
    
    /** Vector in charge of storing the scale value. */
    private Vector2f scale = new Vector2f(1.0F, 1.0F);
    
    /**
     * Type of flip.
     * 
     * @see FlipType
     */
    private FlipType flipType = FlipType.NonFlip;

    /**
     * Serialization only. Do not use.
     */
    public Transform() {        
    }
    
    /**
     * Instantiate a new object <code>Transform</code>. Set the dimensions of
     * the mesh vertices.
     * 
     * @param width the desired width.
     * @param height the desired height.
     */
    public Transform(float width, float height) {
        this(width, height, 1, 1, 0, 0);
    }
    
    /**
     * Instantiate a new object <code>Transform</code>. Set the default values
     * of the transformation.
     * 
     * @param width the desired width.
     * @param height the desired height.
     * @param columns desired number of columns.
     * @param rows desired number of rows.
     * @param colPosition column position.
     * @param rowPosition row position.
     */
    public Transform(float width, float height, int columns, int rows, int colPosition, int rowPosition) {
        this.size.set(width, height);                
        this.columnsAndRows.set(columns, rows);
        this.position.set(colPosition, rowPosition);
    }
    
    /**
     * (non-JavaDoc.)
     * @see Object#clone() 
     * @return Clone.
     */
    @Override
    public Transform clone() {
        try {
            Transform clon = (Transform) super.clone();
            clon.columnsAndRows = columnsAndRows.clone();
            clon.position       = position.clone();
            clon.size           = size.clone();
            clon.scale          = scale.clone();
            clon.flipType       = flipType;
            return clon;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
    
    /**
     * Method in charge of generating the positions of the vertices in space.
     * @return vertices for a 2D mesh.
     */
    public Vector3f[] getVertices() {
        Vector3f[] vertices = new Vector3f[4];
        
        /*
        We set the values of the width and height of the mesh, then scale it
        with the respective values (x,y).
        */
        float width  = size.getX() * scale.getX(), 
              height = size.getY() * scale.getY();
        
        // Positions of vertices in space
        vertices[0] = new Vector3f(-width * 0.5f, -height * 0.5f, 0f);
        vertices[1] = new Vector3f(width * 0.5f, -height * 0.5f, 0f);
        vertices[2] = new Vector3f(-width * 0.5f, height * 0.5f, 0f);
        vertices[3] = new Vector3f(width * 0.5f, height * 0.5f, 0f);
        return vertices;
    }
    
    /**
     * Method in charge of generating the texture coordinates.
     * @return texture coordinates.
     */
    public Vector2f[] getTextureCoordinates() {
        Vector2f[] texCoord = new Vector2f[4];
        int rows    = (int) columnsAndRows.y,
            columns = (int) columnsAndRows.x; 
            
        int colPosition = (int) position.x, 
            rowPosition = (int) position.y;
        
        float uvSpacing = 0.001f;        
        float colSize = 1f / (float) columns;
        float rowSize = 1f / (float) rows;
        
        if ( null == flipType 
                || FlipType.NonFlip == flipType ) {
            texCoord[0] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + rowSize - uvSpacing);
            texCoord[1] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + rowSize - uvSpacing);
            texCoord[2] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + uvSpacing);
            texCoord[3] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + uvSpacing);
        } else switch (flipType) {
            case Flip_H:
                texCoord[0] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + rowSize - uvSpacing);
                texCoord[1] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + rowSize - uvSpacing);
                texCoord[2] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + uvSpacing);
                texCoord[3] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + uvSpacing);
                break;
            case Flip_V:                
                texCoord[0] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + uvSpacing);
                texCoord[1] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + uvSpacing);
                texCoord[2] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + rowSize - uvSpacing);
                texCoord[3] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + rowSize - uvSpacing);
                break;
            case Flip_HV:
                texCoord[0] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + uvSpacing);
                texCoord[1] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + uvSpacing);
                texCoord[2] = new Vector2f(colSize * colPosition + colSize - uvSpacing, rowSize * rowPosition + rowSize - uvSpacing);
                texCoord[3] = new Vector2f(colSize * colPosition + uvSpacing,           rowSize * rowPosition + rowSize - uvSpacing);
                break;
            default:
                break;
        }
        return texCoord;
    }

    /**
     * Sets a new scale for the width and length of the mesh.
     * @param scale new scale.
     */
    public void setScale(Vector2f scale) {
        if (scale == null) {
            throw new NullPointerException("Null scale vector.");
        }
        this.scale = scale;
    }

    /**
     * Establishes a new <code>FlipType</code> for the mesh.
     * @param flipType a new type of flip.
     */
    public void setFlipType(FlipType flipType) {
        this.flipType = flipType;
    }

    /**
     * (non-JavaDoc).
     * @param columns Integer.
     * @param rows Integer.
     * @see Transform#position
     */
    void setPosition(int colPosition, int rowPosition) {
        this.position.set(colPosition, rowPosition);
    }
    
    /**
     * (non-JavaDoc).
     * @param width Float.
     * @param height Float.
     * @see Transform#size
     */
    void setSize(float width, float height) {
        this.size.set(width, height);
    }
    
    /**
     * (non-JavaDoc)
     * @param columns Float.
     * @param rows Float.
     * @see Transform#columnsAndRows
     */
    void setCoords(int columns, int rows) {
        this.columnsAndRows.set(columns, rows);
    }
    
    /**
     * Returns the current scale.
     * @return scale vector.
     */
    public Vector2f getScale() {
        return scale;
    }
    
    /**
     * Returns the type of flip.
     * @return type of flip.
     */
    public FlipType getFlipType() {
        return flipType;
    }
    
    /**
     * Returns the width of the transformation.
     * @return width.
     */
    public float getWidth() {
        return size.x;
    }
    
    /**
     * Returns the height of the transformation.
     * @return height.
     */
    public float getHeight() {
        return size.y;
    }
    
    /**
     * Returns the number of rows.
     * @return rows.
     */
    public int getRows() {
        return (int) columnsAndRows.y;
    }
    
    /**
     * Returns the number of columns.
     * @return columns.
     */
    public int getColumns() {
        return (int) columnsAndRows.x;
    }
    
    /**
     * Returns the position of the column.
     * @return column position.
     */
    public int getColPosition() { 
        return (int) position.x;
    }
    
    /**
     * Returns the position of the row.
     * @return row position.
     */
    public int getRowPosition() {
        return (int) position.y;
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
        out.write(size, "size", new Vector2f(0, 0));
        out.write(scale, "scale", new Vector2f(1.0F, 1.0F));
        out.write(columnsAndRows, "columnsAndRows", new Vector2f(1, 1));
        out.write(position, "position", new Vector2f(0, 0));
        out.write(flipType, "flipType", null);
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
        size = (Vector2f) in.readSavable("size", new Vector2f(0, 0));
        scale = (Vector2f) in.readSavable("scale", new Vector2f(1.0F, 1.0F));
        columnsAndRows = (Vector2f) in.readSavable("columnsAndRows", new Vector2f(1, 1));
        position = (Vector2f) in.readSavable("position", new Vector2f(0, 0));
        flipType = in.readEnum("flipType", FlipType.class, FlipType.NonFlip);
    }
}
