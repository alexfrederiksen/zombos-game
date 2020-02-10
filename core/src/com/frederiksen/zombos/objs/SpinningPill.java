package com.frederiksen.zombos.objs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class SpinningPill extends PillObj implements Movable {
    protected float angularVel = 0.5f;

    public SpinningPill(Vector2 p1, Vector2 p2, float radius, Color color) {
        super(p1, p2, radius, color);
    }

    @Override
    public boolean update(float delta) {
        float w = angularVel * delta;
        Vector2 dv = new Vector2(-dir.y, dir.x).scl(w);
        dir.add(dv);
        dir.nor();
        pos.mulAdd(dv, -0.5f * length);

        return true;
    }

    @Override
    public float getAngularVel() {
        return angularVel;
    }

    @Override
    public Vector2 getLinearVel() {
        return Vector2.Zero;
    }
}
