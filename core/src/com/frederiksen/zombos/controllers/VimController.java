package com.frederiksen.zombos.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.frederiksen.zombos.objs.Player;

public class VimController extends Player.Controller {
     @Override
    public void poll() {
        if (Gdx.input.isKeyPressed(Input.Keys.K))
            player.moveForward();
        if (Gdx.input.isKeyPressed(Input.Keys.J))
            player.moveBackward();
        if (Gdx.input.isKeyPressed(Input.Keys.L))
            player.rotateCW();
        if (Gdx.input.isKeyPressed(Input.Keys.H))
            player.rotateCCW();
        if (Gdx.input.isKeyPressed(Input.Keys.U))
            player.shoot();
    }
}
