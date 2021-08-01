package net.whg.minigames.framework.arena;

public class RegionIterator {
    private int minX, minY, minZ;
    private int maxX, maxY, maxZ;
    private int x, y, z;
    private boolean done;

    public RegionIterator(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        reset();
    }

    public void reset() {
        x = minX;
        y = minY;
        z = minZ;
        done = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void update() {
        if (done)
            return;

        z++;
        if (z > maxZ) {
            z = minZ;
            x++;
            if (x > maxX) {
                x = minX;
                y++;
                if (y > maxY) {
                    y = minY;
                    done = true;
                }
            }
        }
    }

    public boolean isDone() {
        return done;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }
}
