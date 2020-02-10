package com.frederiksen.zombos;

import com.badlogic.gdx.graphics.Texture;

public class Resources {
    public Texture circle;
    public Texture rod;
    public Texture block;

    public void load() {
        circle = new Texture("circle.png");
        rod = new Texture("rod.png");
        block = new Texture("block.png");
    }

    public void dispose() {
        circle.dispose();
        rod.dispose();
        block.dispose();
    }
}
