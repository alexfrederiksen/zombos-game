package com.frederiksen.zombos.objs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.Ray;
import com.frederiksen.zombos.Renderer;
import com.frederiksen.zombos.Resources;
import com.frederiksen.zombos.World;

public class CircleObj implements Collidable {
    protected World world;

    protected Vector2 pos = new Vector2();
    protected float radius;
    private float radius2;
    protected float rotation;

    private Texture sprite;
    private Color color;


    public CircleObj(Vector2 pos, float radius, Color color) {
        this.pos.set(pos);
        this.radius = radius;
        this.radius2 = radius * radius;
        this.color = color;
    }

    @Override
    public void load(World world) {
        this.world = world;
        load(world.resources);
    }

    @Override
    public void load(Resources resources) {
        sprite = resources.circle;
    }

    @Override
    public Vector2 collide(CircleObj obj) {
        if (obj.pos.dst2(pos) < (radius + obj.radius) * (radius + obj.radius) ) {
            Vector2 d = new Vector2(obj.pos).sub(pos);
            float dst = d.len();
            float overlap = radius + obj.radius - dst;
            return d.scl(overlap / dst);
        } else {
            return Vector2.Zero;
        }
    }

    @Override
    public Vector2 collide(PillObj obj) {
        Vector2 v = new Vector2(pos).sub(obj.pos);
        float p = MathUtils.clamp(v.dot(obj.dir), 0f, obj.length);
        Vector2 q = new Vector2(obj.dir).scl(p).add(obj.pos).sub(pos);
        float d2 = q.len2();
        float r = radius + obj.radius;

        if (d2 < (r * r)) {
            float d = (float) Math.sqrt(d2);
            float overlap = r - d;
            return q.scl(overlap / d);
        }
        return Vector2.Zero;
    }

    @Override
    public Vector2 visitCollide(Collidable obj) {
        return obj.collide(this);
    }

    @Override
    public float visitCollide(Ray ray) {
        return Collidable.collide(this, ray);
    }

    public float getRadius2() {
        return radius2;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void render(Renderer renderer) {
        renderer.setColor(color);
        renderer.draw(sprite, pos.x, pos.y,
                      radius * 2f, radius * 2f,
                      (float) Math.toDegrees(rotation) - 90f);
    }

    @Override
    public World getWorld() {
        return world;
    }
}
