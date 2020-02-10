package com.frederiksen.zombos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class Main extends ApplicationAdapter {
	private World world;

	@Override
	public void create() {
	    world = new World();
	}

	public void update(float delta) {
	    world.update(delta);
	}

	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());

		world.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		world.resize(width, height);
	}

	@Override
	public void dispose() {
	    world.dispose();
	}
}
