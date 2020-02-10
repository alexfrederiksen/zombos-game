package com.frederiksen.zombos.objs;

import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.Renderer;
import com.frederiksen.zombos.Resources;
import com.frederiksen.zombos.World;

public interface GameObj {

    default void load(World world) {
        load(world.resources);
    }

    default void load(Resources resources) {}
    default boolean update(float delta) { return true; }
    default void render(Renderer renderer) {}

    Vector2 getPos();
    World getWorld();
}
