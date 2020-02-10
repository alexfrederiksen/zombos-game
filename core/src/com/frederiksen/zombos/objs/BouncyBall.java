package com.frederiksen.zombos.objs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.Physics;
import com.frederiksen.zombos.Renderer;
import com.frederiksen.zombos.World;

public class BouncyBall extends CircleObj implements Physics.RigidObj {
    private Vector2 vel = new Vector2();

    private Physics.RigidBody body;
    private float timeLeft = 2f;

    public BouncyBall(Vector2 pos, float radius, Color color) {
        super(pos, radius, color);
    }

    public void launch(Vector2 vel) {
        this.vel.set(vel);
    }

    @Override
    public void load(World world) {
        super.load(world);

        body = new Physics.RigidBody(this, world.collidables);
        body.setDecay(0f);
    }

    @Override
    public boolean update(float delta) {
        body.update(delta);

        timeLeft -= delta;

        return timeLeft > 0;
    }

    @Override
    public float getAngularVel() {
        return 0;
    }

    @Override
    public Vector2 getLinearVel() {
        return vel;
    }

    @Override
    public Physics.RigidBody getBody() {
        return null;
    }

    @Override
    public void render(Renderer renderer) {
        super.render(renderer);
    }
}
