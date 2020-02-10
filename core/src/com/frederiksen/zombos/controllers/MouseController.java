package com.frederiksen.zombos.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.frederiksen.zombos.Renderer;
import com.frederiksen.zombos.objs.Player;

public class MouseController extends Player.Controller {
    private static final float MIN_MOVE = 200f;
    @Override
    public void poll() {
        Renderer renderer = player.getWorld().renderer;

        if (Gdx.input.isTouched()) player.shoot();

        Vector3 pointer = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 pointTo = renderer.getCamera().unproject(pointer);

        Vector2 v = new Vector2(pointTo.x, pointTo.y).sub(player.getPos());
        float angle = v.angleRad();

        player.rotate(angle);

        if (v.len2() > MIN_MOVE * MIN_MOVE) {
            player.moveForward();
        }

    }
}
