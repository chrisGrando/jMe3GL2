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
package jme3gl2.physics.debug.shape;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Class <code>Cross</code> responsible for generating a cross shape.
 * 
 * @author wil
 * @version 1.0-SNAPSHOT 
 * @since 2.5.0
 */
public class Cross extends Mesh {

    /** Number of segments. */
    private float segmentSize;

    /**
     * Default constructor for serialization only. Do not use.
     */
    public Cross() {
    }

    /**
     * Main class constructor <code>Cross2D</code> to build an instance of a
     * cross.
     * 
     * @param segmentSize the size of the cross segment.
     */
    public Cross(final float segmentSize) {
        this(segmentSize, Mode.Lines);
    }

    /**
     * Main class constructor <code>Cross2D</code> to build an instance of a
     * cross.
     * 
     * @param segmentSize the size of the cross segment.
     * @param mode mode for this mesh.
     */
    public Cross(final float segmentSize, final Mode mode) {
        super();
        Cross.this.setMode(mode);
        Cross.this.updateGeometry(segmentSize);
    }

    /**
     * Returns the size of the cross segment.
     * @return the size.
     */
    public float getSegmentSize() {
        return this.segmentSize;
    }

    /**
     * Reconstructs the cross based on a new set of parameters.
     * @param segmentSize the size of the cross segment.
     */
    public void updateGeometry(final float segmentSize) {
        // Create buffers
        this.setBuffer(Type.Position, 3, 
                BufferUtils.createVector3Buffer(getFloatBuffer(Type.Position),
                        4));
        this.setBuffer(Type.Index, 1,
                BufferUtils.createShortBuffer(getShortBuffer(Type.Index),
                        4));
        final FloatBuffer pb = getFloatBuffer(Type.Position);
        final IndexBuffer ib = getIndexBuffer();

        // Set the vertices
        pb.put(this.segmentSize).put(this.segmentSize).put(0);
        pb.put(-this.segmentSize).put(this.segmentSize).put(0);
        pb.put(this.segmentSize).put(-this.segmentSize).put(0);
        pb.put(-this.segmentSize).put(-this.segmentSize).put(0);

        // Set the indexes
        ib.put(0, 0);
        ib.put(1, 3);
        ib.put(2, 1);
        ib.put(3, 2);
        updateBound();
    }
}
