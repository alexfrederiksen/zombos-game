package com.frederiksen.zombos.objs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.Ray;
import com.frederiksen.zombos.Renderer;
import com.frederiksen.zombos.Resources;
import com.frederiksen.zombos.World;

public class PillObj implements Collidable {
    protected World world;

    protected Vector2 pos;
    protected Vector2 dir;
    protected float length;
    protected float radius;

    protected Color color;

    private Texture circle;
    private Texture block;

    public PillObj(Vector2 pos, Vector2 dir, float length, float radius, Color color) {
        this.pos = pos;
        this.dir = dir;
        this.length = length;
        this.radius = radius;
        this.color = color;
    }

    public PillObj(Vector2 p1, Vector2 p2, float radius, Color color) {
        setPoints(p1, p2);
        this.radius = radius;
        this.color = color;
    }

    public void setPoints(Vector2 p1, Vector2 p2) {
        this.pos = p1;
        Vector2 v = new Vector2(p2).sub(p1);
        this.length = v.len();
        this.dir = v.nor();
    }

    @Override
    public Vector2 visitCollide(Collidable obj) {
        return obj.collide(this);
    }

    @Override
    public float visitCollide(Ray ray) {
        return Collidable.collide(this, ray);
    }

    @Override
    public void load(World world) {
        this.world = world;
        load(world.resources);
    }

    @Override
    public void load(Resources resources) {
        circle = resources.circle;
        block = resources.block;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setColor(color);
        renderer.drawRay(block, pos.x, pos.y, dir.angleRad(), length, radius * 2f);
        renderer.draw(circle, pos.x, pos.y, radius * 2f, radius * 2f, 0f);
        renderer.draw(circle, pos.x + dir.x * length, pos.y + dir.y * length,
                      radius * 2f, radius * 2f, 0f);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }
}
