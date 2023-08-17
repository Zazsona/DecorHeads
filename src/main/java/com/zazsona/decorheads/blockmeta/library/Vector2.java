package com.zazsona.decorheads.blockmeta.library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Vector2 implements Comparable<Vector2>
{
    @Expose
    @SerializedName("X")
    protected int x;
    @Expose
    @SerializedName("Z")
    protected int z;

    public Vector2(int x, int z)
    {
        this.x = x;
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
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && z == vector2.z;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, z);
    }

    @Override
    public int compareTo(@NotNull Vector2 vector2)
    {
        if (this == vector2) return 0;
        int xCmp = Integer.compare(x, vector2.x);
        if (xCmp != 0)
            return xCmp;
        else
            return Integer.compare(z, vector2.z);
    }
}
