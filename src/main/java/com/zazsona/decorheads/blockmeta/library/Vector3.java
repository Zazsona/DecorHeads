package com.zazsona.decorheads.blockmeta.library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Vector3 implements Comparable<Vector3>
{
    @Expose
    @SerializedName("X")
    protected int x;
    @Expose
    @SerializedName("Y")
    private int y;
    @Expose
    @SerializedName("Z")
    protected int z;

    public Vector3(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets x
     * @return x
     */
    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * Gets y
     * @return y
     */
    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    /**
     * Gets z
     * @return z
     */
    public int getZ()
    {
        return z;
    }

    public void setZ(int z)
    {
        this.z = z;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return x == vector3.x && y == vector3.y && z == vector3.z;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, z);
    }

    @Override
    public int compareTo(@NotNull Vector3 vector3)
    {
        if (this == vector3) return 0;
        int xCmp = Integer.compare(x, vector3.x);
        if (xCmp != 0)
            return xCmp;
        int yCmp = Integer.compare(y, vector3.y);
        if (yCmp != 0)
            return yCmp;
        int zCmp = Integer.compare(z, vector3.z);
            return zCmp;
    }
}
