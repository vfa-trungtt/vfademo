package vfa.vfdemo.fragments.drawing.glObjects;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Vitalify on 3/22/17.
 */

public class Box extends BaseGLObject{

    private final FloatBuffer mCubePositions;
    private final FloatBuffer mCubeColors;
    private final FloatBuffer mCubeNormals;

    private final int mBytesPerFloat = 4;
    private int mPerVertexProgramHandle;
    private int mPointProgramHandle;

    public Box(){
        // Define points for a cube.
        // X, Y, Z
        final float[] cubePositionData =
                {
                        // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                        // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                        // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                        // usually represent the backside of an object and aren't visible anyways.

                        // Front face
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,

                        // Right face
                        1.0f, 1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, -1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Back face
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,

                        // Left face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, 1.0f, 1.0f,

                        // Top face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Bottom face
                        1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                };

        // R, G, B, A
        final float[] cubeColorData =
                {
                        // Front face (red)
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,

                        // Right face (green)
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,

                        // Back face (blue)
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,

                        // Left face (yellow)
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,

                        // Top face (cyan)
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,

                        // Bottom face (magenta)
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f
                };

        // X, Y, Z
        // The normal is used in light calculations and is a vector which points
        // orthogonal to the plane of the surface. For a cube model, the normals
        // should be orthogonal to the points of each face.
        final float[] cubeNormalData =
                {
                        // Front face
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,

                        // Right face
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,

                        // Back face
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,

                        // Left face
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,

                        // Top face
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,

                        // Bottom face
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f
                };

        // Initialize the buffers.
        mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions.put(cubePositionData).position(0);

        mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeColors.put(cubeColorData).position(0);

        mCubeNormals = ByteBuffer.allocateDirect(cubeNormalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeNormals.put(cubeNormalData).position(0);

        String vertexShader = getVertexShader();
        String fragmentShader = getFragmentShader();

        int vertexShaderHandle      = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderHandle    = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color", "a_Normal"});

        // Define a simple shader program for our point.
        final String pointVertexShader =
                "uniform mat4 u_MVPMatrix;      \n"
                        +	"attribute vec4 a_Position;     \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   gl_Position = u_MVPMatrix   \n"
                        + "               * a_Position;   \n"
                        + "   gl_PointSize = 5.0;         \n"
                        + "}                              \n";

        final String pointFragmentShader =
                "precision mediump float;       \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   gl_FragColor = vec4(1.0,    \n"
                        + "   1.0, 1.0, 1.0);             \n"
                        + "}                              \n";

        int pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
        int pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);
        mPointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                new String[] {"a_Position"});
    }
    private int compileShader(final int shaderType, final String shaderSource)
    {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
//                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
//                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }


    protected String getVertexShader() {
        // TODO: Explain why we normalize the vectors, explain some of the vector math behind it all. Explain what is eye space.
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
                        + "uniform mat4 u_MVMatrix;       \n"		// A constant representing the combined model/view matrix.
                        + "uniform vec3 u_LightPos;       \n"	    // The position of the light in eye space.

                        + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color;        \n"		// Per-vertex color information we will pass in.
                        + "attribute vec3 a_Normal;       \n"		// Per-vertex normal information we will pass in.

                        + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.

                        + "void main()                    \n" 	// The entry point for our vertex shader.
                        + "{                              \n"
                        // Transform the vertex into eye space.
                        + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
                        // Transform the normal's orientation into eye space.
                        + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
                        // Will be used for attenuation.
                        + "   float distance = length(u_LightPos - modelViewVertex);             \n"
                        // Get a lighting direction vector from the light to the vertex.
                        + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
                        // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
                        // pointing in the same direction then it will get max illumination.
                        + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
                        // Attenuate the light based on distance.
                        + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
                        // Multiply the color by the illumination level. It will be interpolated across the triangle.
                        + "   v_Color = a_Color * diffuse;                                       \n"
                        // gl_Position is a special variable used to store the final position.
                        // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                        + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                        + "}                                                                     \n";

        return vertexShader;
    }
    private final int mPositionDataSize = 3;
    protected String getFragmentShader() {
        final String fragmentShader =
                "precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a
                        // precision in the fragment shader.
                        + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the
                        // triangle per fragment.
                        + "void main()                    \n"		// The entry point for our fragment shader.
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.
                        + "}                              \n";

        return fragmentShader;
    }
    private final int mColorDataSize = 4;
    private final int mNormalDataSize = 3;
    private int mNormalHandle;

    private float[] mModelMatrix = new float[16];
    /** This will be used to pass in the transformation matrix. */
//    private int mMVPMatrixHandle;
    /** This will be used to pass in the modelview matrix. */
    private int mMVMatrixHandle;

//    private void draw() {
//        // Pass in the position information
//        mCubePositions.position(0);
//        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mCubePositions);
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//
//        // Pass in the color information
//        mCubeColors.position(0);
//        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, 0, mCubeColors);
//
//        GLES20.glEnableVertexAttribArray(mColorHandle);
//
//        // Pass in the normal information
//        mCubeNormals.position(0);
//        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 0, mCubeNormals);
//
//        GLES20.glEnableVertexAttribArray(mNormalHandle);
//
//        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
//        // (which currently contains model * view).
//        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
//
//        // Pass in the modelview matrix.
//        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
//
//        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
//        // (which now contains model * view * projection).
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
//
//        // Pass in the combined matrix.
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
//
//        // Pass in the light position in eye space.
////        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
//
//        // Draw the cube.
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
//    }

    @Override
    public void draw2GL() {

        Matrix.multiplyMM(modelMatrix, 0, GL2Render.ProjectionMatrix, 0, GL2Render.ViewMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, -2.0f, 2.0f);

        mCubePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mCubePositions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mCubeColors.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, 0, mCubeColors);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        // Pass in the normal information
        mCubeNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 0, mCubeNormals);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
//        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, modelMatrix, 0);
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(GL2Render.ViewMatrix, 0, GL2Render.ProjectionMatrix, 0, GL2Render.ViewMatrix, 0);
        // Pass in the combined matrix.
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, GL2Render.ViewMatrix, 0);
        // Pass in the light position in eye space.
//        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }
}
