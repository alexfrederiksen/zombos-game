package com.frederiksen.zombos;

import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.objs.Collidable;
import com.frederiksen.zombos.objs.Movable;
import com.frederiksen.zombos.structures.Group;

public class Physics {
    private static final float COLLISION_DEBOUNCE = 50f;
    private static final float MAX_DEBOUNCE = 300f;

    public interface RigidObj extends Movable, Collidable {
        RigidBody getBody();
    }

    public static class RigidBody extends FreeBody {
        private RigidObj obj;
        protected Group<Collidable> collidables;
        protected Vector2 cforce;

        public RigidBody(RigidObj obj, Group<Collidable> collidables) {
            super(obj);

            this.obj = obj;
            this.collidables = collidables;

            cforce = new Vector2();
        }

        public void update(float delta) {
            cforce.setZero();

            // collide with some stuff
            for (Collidable o : collidables) {
                if (o != obj) {
                    cforce.mulAdd(obj.collide(o), -1f);
                }
            }

            // create collision force
            cforce.scl(COLLISION_DEBOUNCE);

            // basic frame update
            vel.add(cforce);
            super.update(delta);
        }
    }

    public static class FreeBody {
        private Movable obj;

        protected Vector2 pos;
        protected Vector2 vel;

        protected float decay = 1f;

        public FreeBody(Movable obj) {
            this.obj = obj;

            this.pos = obj.getPos();
            this.vel = obj.getLinearVel();
        }

        public void setDecay(float decay) {
            this.decay = decay;
        }

        public void update(float delta) {
            pos.mulAdd(vel, delta);
            vel.scl(1f - decay);
        }
    }
}
