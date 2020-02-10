package com.frederiksen.zombos.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.frederiksen.zombos.objs.Player;

public class ArrowController extends Player.Controller {
     @Override
    public void poll() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            player.moveForward();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            player.moveBackward();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player.rotateCW();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player.rotateCCW();
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
            player.shoot();
    }
}
