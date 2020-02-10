package com.frederiksen.zombos.objs;

import com.badlogic.gdx.math.Vector2;

public interface Movable extends GameObj {
    float getAngularVel();
    Vector2 getLinearVel();
}
