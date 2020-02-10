package com.frederiksen.zombos.objs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.*;

public class Player extends CircleObj implements Physics.RigidObj {
    public static final float WALK_SPEED = 400f;
    public static final float ROT_SPEED  = 3f;

    private Controller controller;

    private float angularVel = 0f;
    private Vector2 vel = new Vector2();

    private boolean laser = true;
    private float laserLength = -1f;

    private Ray laserRay = new Ray();

    private Texture block;
    private Physics.RigidBody body;

    public Player(Vector2 pos, float radius, Color color, Controller controller) {
        super(pos, radius, color);
        this.controller = controller;
        controller.setPlayer(this);
    }

    @Override
    public void load(World world) {
        super.load(world);

        body = new Physics.RigidBody(this, world.collidables);
    }

    @Override
    public void load(Resources resources) {
        super.load(resources);
        block = resources.block;
    }

    private void shootLaser() {
        laserRay.pos.set(pos);
        laserRay.dir.x = MathUtils.cos(rotation);
        laserRay.dir.y = MathUtils.sin(rotation);

        laserLength = -1f;
        for (Collidable obj : world.collidables) {
            if (obj != this) {
                float length = Collidable.collide(obj, laserRay);
                if (length >= 0 && (laserLength < 0 || length < laserLength))
                    laserLength = length;
            }
        }
    }

    public boolean update(float delta) {
        // poll for input
        controller.poll();

        rotation += angularVel * delta;
        angularVel = 0f;

        if (laser) {
            shootLaser();
        }

        // do physics update
        body.update(delta);

        return true;
    }

    public void moveForward() {
        vel.x = MathUtils.cos(rotation) * WALK_SPEED;
        vel.y = MathUtils.sin(rotation) * WALK_SPEED;
    }

    public void moveBackward() {
        vel.x = MathUtils.cos(rotation) * -WALK_SPEED;
        vel.y = MathUtils.sin(rotation) * -WALK_SPEED;
    }

    public void rotateCW() {
        angularVel = -ROT_SPEED;
    }

    public void rotateCCW() {
        angularVel = ROT_SPEED;
    }

    public void rotate(float angle) {
        rotation = angle;
    }

    public void shoot() {
        Vector2 dir = new Vector2(1f, 0f).rotateRad(rotation);
        BouncyBall ball = new BouncyBall(new Vector2(pos).mulAdd(dir, 2 * radius), 10f, Color.TAN);
        ball.load(world);
        ball.launch(dir.scl(2 * WALK_SPEED));

        world.balls.add(ball);
    }

    public void useLaser(boolean enable) {
        laser = enable;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.getShader();
        if (laser) {
            float length = laserLength;
            if (laserLength < 0f) {
                length = renderer.getUpperViewRadius();
            }
            renderer.drawRay(block, pos.x, pos.y, rotation, length, 10f);
        }
        super.render(renderer);
    }

    public static abstract class Controller {
        protected Player player;

        public void setPlayer(Player player) {
            this.player = player;
        }

        public abstract void poll();

    }

    @Override
    public float getAngularVel() {
        return angularVel;
    }

    @Override
    public Vector2 getLinearVel() {
        return vel;
    }

    @Override
    public Physics.RigidBody getBody() {
        return body;
    }
}
