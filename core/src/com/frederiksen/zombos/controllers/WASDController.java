package com.frederiksen.zombos.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.frederiksen.zombos.objs.Player;

public class WASDController extends Player.Controller {
    @Override
    public void poll() {
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.moveForward();
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.moveBackward();
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.rotateCW();
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.rotateCCW();
    }
}
