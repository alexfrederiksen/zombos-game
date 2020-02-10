package com.frederiksen.zombos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Renderer extends SpriteBatch {
    private float minVisibleUnits;
    private static final float WIDEN_BORDER = 500;
    private OrthographicCamera camera;

    private Matrix4 guiTransform = new Matrix4();

    private Vector2 windowSize = new Vector2();

    public Renderer(float minVisibleUnits) {
        this.minVisibleUnits = minVisibleUnits;

        camera = new OrthographicCamera();
    }

    public void beginWorld() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setProjectionMatrix(camera.combined);
        super.begin();
    }

    public void beginGUI() {
        setProjectionMatrix(guiTransform);
        super.begin();
    }

    public void lookAt(Vector2 x) {
        camera.position.set(x, 0f);
        camera.update();
    }

    public void widen(Vector2 min, Vector2 max) {
        float unitWidth = max.x - min.x + 2f * WIDEN_BORDER;
        float unitHeight = max.y - min.y + 2f * WIDEN_BORDER;

        float unitSize = Math.min(windowSize.x / unitWidth, windowSize.y / unitHeight);

        float centerX = 0.5f * (min.x + max.x);
        float centerY = 0.5f * (min.y + max.y);

        camera.setToOrtho(false, windowSize.x / unitSize, windowSize.y / unitSize);
        camera.position.set(centerX, centerY, 0f);

        camera.update();
    }

    @Override
    public void end() {
        super.end();
    }

    public void resize(int width, int height) {
        windowSize.set(width, height);

        guiTransform.setToOrtho2D(0, 0, width, height);

        //float unitSize = Math.min(width / minVisibleUnits, height / minVisibleUnits);
        //camera.setToOrtho(true, width / unitSize, height / unitSize);
    }

    public void drawSegment(Texture texture, Vector2 start, Vector2 end, float thickness) {
        float rot = MathUtils.atan2(end.y - start.y, end.x - start.x);
        float len = start.dst(end);
        drawRay(texture, start.x, start.y, rot, len, thickness);
    }

    /**
     * Draw a ray
     *
     * @param texture
     * @param startX
     * @param startY
     * @param rotation in radians
     * @param length
     * @param thickness full thickness (not radius)
     */
    public void drawRay(Texture texture, float startX, float startY, float rotation, float length, float thickness) {
        draw(texture, startX, startY - thickness * 0.5f, 0f, thickness * 0.5f,
             length, thickness, 1f, 1f, rotation * MathUtils.radiansToDegrees,
             0, 0, texture.getWidth(), texture.getHeight(),
             false, false);
    }

    public void draw(Texture texture, float centerX, float centerY, float width, float height, float rot) {
        draw(texture, centerX - width * 0.5f, centerY - height * 0.5f,
             width * 0.5f, height * 0.5f,
             width, height, 1f, 1f, rot,
             0, 0, texture.getWidth(), texture.getHeight(),
             false, false);
    }

    public float getUpperViewRadius() {
        return 2f * Math.max(camera.viewportWidth, camera.viewportHeight);
    }

    public static ShaderProgram createShader() {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "uniform mat4 u_projTrans;\n" //
			+ "varying vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
			+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "}\n";
		String fragmentShader = "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" //
			+ "varying LOWP vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "uniform sampler2D u_texture;\n" //
			+ "void main()\n"//
			+ "{\n" //
			+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
			+ "}";

		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());

		return shader;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
