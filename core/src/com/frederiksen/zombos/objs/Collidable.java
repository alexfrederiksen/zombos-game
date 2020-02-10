package com.frederiksen.zombos.objs;

import com.badlogic.gdx.math.Vector2;
import com.frederiksen.zombos.Ray;

public interface Collidable extends GameObj {

    /* Object to object collisions ---------------------------------------------------------------------------------- */

    /**
     * Visitor function for multiple dispatch
     * @param obj to visit
     * @return
     */
    Vector2 visitCollide(Collidable obj);

    default Vector2 collide(Collidable obj) {
        return obj.visitCollide(this);
    }

    /**
     * Collide with other objects
     *
     * @param obj to collide with
     * @return normal of collision w.r.t. this object
     */
    default Vector2 collide(CircleObj obj) { return Vector2.Zero; }
    default Vector2 collide(PillObj obj) { return Vector2.Zero; }


    /* Ray to object collisions ------------------------------------------------------------------------------------- */

    float visitCollide(Ray ray);

    static float collide(Collidable obj, Ray ray) {
        return obj.visitCollide(ray);
    }

    static float collide(CircleObj circle, Ray ray) {
        Vector2 P = circle.pos;
        float r = circle.radius;
        Vector2 R0 = ray.pos;
        Vector2 R = ray.dir;

        Vector2 v = new Vector2(R0).sub(P);
        float a = R.len2();
        float b = 2f * v.dot(R);
        float c = v.len2() - r * r;

        float det = b * b - 4f * a * c;
        if (det < 0) return -1f;

        return (-b - (float) Math.sqrt(det)) / (2f * a);
    }

    static float collide(PillObj pill, Ray ray) {
        Vector2 v1 = pill.dir;
        Vector2 v2 = ray.dir;

        float a = v1.x;
        float b = -v2.x;
        float c = v1.y;
        float d = -v2.y;

        float det = a * d - b * c;

        // if it's not invertible, then we don't wanna deal with it (either no solution or many)
        if (det == 0) return -1f;

        // compute inverse
        float idet = 1f / det;
        float ia = d * idet;
        float ib = -b * idet;
        float ic = -c * idet;
        float id = a * idet;

        // compute constant
        float ax = ray.pos.x - pill.pos.x;
        float ay = ray.pos.y - pill.pos.y;

        // apply inverse
        float t1 = ia * ax + ib * ay;
        float t2 = ic * ax + id * ay;

        if (t1 >= 0 && t1 <= pill.length && t2 >= 0) {
            return t2;
        } else {
            return -1f;
        }
    }
}
