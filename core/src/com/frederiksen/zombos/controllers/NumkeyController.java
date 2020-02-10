package com.frederiksen.zombos.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.frederiksen.zombos.objs.Player;

public class NumkeyController extends Player.Controller {
     @Override
    public void poll() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8))
            player.moveForward();
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_5))
            player.moveBackward();
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6))
            player.rotateCW();
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4))
            player.rotateCCW();
    }
}
