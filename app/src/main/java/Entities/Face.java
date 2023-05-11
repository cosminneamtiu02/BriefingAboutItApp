package Entities;

public class Face {
    private String id;
    private String faceCrop;
    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;

    public Face(){}

    public Face(String id, String faceCrop, int xMin, int yMin, int xMax, int yMax) {
        this.id = id;
        this.faceCrop = faceCrop;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFaceCrop(String faceCrop) {
        this.faceCrop = faceCrop;
    }

    public void setXMin(int xMin) {
        this.xMin = xMin;
    }

    public void setYMin(int yMin) {
        this.yMin = yMin;
    }

    public void setXMax(int xMax) {
        this.xMax = xMax;
    }

    public void setYMax(int yMax) {
        this.yMax = yMax;
    }

    public String getId() {
        return id;
    }

    public String getFaceCrop() {
        return faceCrop;
    }

    public int getXMin() {
        return xMin;
    }

    public int getYMin() {
        return yMin;
    }

    public int getXMax() {
        return xMax;
    }

    public int getYMax() {
        return yMax;
    }
}
