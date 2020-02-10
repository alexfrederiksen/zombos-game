package com.frederiksen.zombos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.controllers.ArrowController;
import com.frederiksen.zombos.controllers.MouseController;
import com.frederiksen.zombos.controllers.VimController;
import com.frederiksen.zombos.controllers.WASDController;
import com.frederiksen.zombos.objs.*;
import com.frederiksen.zombos.structures.Group;

import java.util.Iterator;

public class World {
	public Group<GameObj>    objs;
	public Group<Collidable> collidables;
	public Group<Player>     players;
	public Group<BouncyBall> balls;

	public Renderer renderer;
	public Resources resources;
	public FrameBuffer darkness;

	private ShaderProgram satExtractor;
	private ShaderProgram defaultShader;

	public World() {
		renderer = new Renderer(5f);
		renderer.enableBlending();


		resources = new Resources();
		resources.load();

		satExtractor = new ShaderProgram(Gdx.files.internal("extract.vert"),
										 Gdx.files.internal("extract.frag"));

		System.out.println(satExtractor.getLog());


		defaultShader = SpriteBatch.createDefaultShader();

		objs = new Group<>();
		collidables = new Group<>();
		players = new Group<>();
		balls = new Group<>();

		// build group structure
		objs.addSubgroup(collidables);
		collidables.addSubgroup(players);
		collidables.addSubgroup(balls);


		// lock in group structure
		objs.collapse();

		// initialize players
		Player.Controller controller1 = new VimController();
		Player.Controller controller2 = new WASDController();
		Player.Controller controller3 = new ArrowController();
		Player.Controller controller4 = new MouseController();

		players.add(new Player(new Vector2(600, 400), 50, Color.GREEN, controller1));
		players.add(new Player(new Vector2(700, 400), 50, Color.BLUE, controller2));
		players.add(new Player(new Vector2(800, 400), 50, Color.SKY, controller3));
		players.add(new Player(new Vector2(900, 400), 50, Color.PINK, controller4));

		// create test objects
		CircleObj testObj = new CircleObj(new Vector2(200, 200), 50, Color.RED);
		collidables.add(testObj);
		testObj = new CircleObj(new Vector2(200, 320), 50, Color.RED);
		collidables.add(testObj);

		PillObj testPill = new SpinningPill(new Vector2(-400, -400), new Vector2(90, 50), 20f, Color.CORAL);
		collidables.add(testPill);

		// load resources
		for (GameObj obj : objs) obj.load(this);
	}

	public void update(float delta) {
		Iterator<Player> it = players.iterator();

		Player first = it.next();
		Vector2 min = new Vector2(first.getPos());
		Vector2 max = new Vector2(first.getPos());

		while (it.hasNext()) {
			Player obj = it.next();

			Vector2 p = obj.getPos();
			min.x = Math.min(min.x, p.x);
			min.y = Math.min(min.y, p.y);
			max.x = Math.max(max.x, p.x);
			max.y = Math.max(max.y, p.y);
		}

		Iterator<GameObj> objIter = objs.iterator();
		while (objIter.hasNext()) {
			GameObj o = objIter.next();

			boolean alive = o.update(delta);
			if (!alive) objIter.remove();
		}

		renderer.widen(min, max);
	}

	public Matrix4 idt = new Matrix4().scale(1f, -1f, 1f);
	public Vector2 blurDir = Vector2.X;

	public void render() {

		blurDir = (blurDir == Vector2.X) ? Vector2.Y : Vector2.X;

		// render world

		renderer.setShader(defaultShader);
		renderer.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		darkness.begin();
		renderer.beginWorld();

		for (GameObj obj : objs) {
			obj.render(renderer);
		}

		renderer.end();
		darkness.end();

		// render darkness
		renderer.setProjectionMatrix(idt);

        renderer.begin();
     	renderer.setColor(Color.WHITE);
		renderer.draw(darkness.getColorBufferTexture(), -1f, -1f, 2f, 2f);
        renderer.end();

        // add glow
		renderer.setShader(satExtractor);
		renderer.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);


		renderer.begin();

		satExtractor.setUniformf("u_dir", blurDir);
		renderer.setColor(Color.WHITE);
		renderer.draw(darkness.getColorBufferTexture(), -1f, -1f, 2f, 2f);
		renderer.end();

		// render GUI

		renderer.beginGUI();


		renderer.end();
	}

	public void resize(int width, int height) {
		renderer.resize(width, height);

		darkness = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
	}

	public void dispose() {
		renderer.dispose();
		resources.dispose();
	}
}
