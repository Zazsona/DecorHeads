package com.zazsona.decorheads.crafting;

/**
 * Defines the position of a matrix within a matrix (E.g, a 2x2 grid within a 3x3 grid)
 */
public class CraftSubmatrix
{
    private int startRow;
    private int startColumn;
    private int axisLength;

    public CraftSubmatrix(int startRow, int startColumn, int axisLength)
    {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.axisLength = axisLength;
    }

    public int getStartRow()
    {
        return startRow;
    }

    public void setStartRow(int startRow)
    {
        this.startRow = startRow;
    }

    public int getStartColumn()
    {
        return startColumn;
    }

    public void setStartColumn(int startColumn)
    {
        this.startColumn = startColumn;
    }

    public int getAxisLength()
    {
        return axisLength;
    }

    public void setAxisLength(int axisLength)
    {
        this.axisLength = axisLength;
    }
}
