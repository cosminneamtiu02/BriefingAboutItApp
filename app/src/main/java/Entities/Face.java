package Entities;

public class Face {
    private final String id;
    private final String faceCrop;
    private final float xMin;
    private final float yMin;
    private final float xMax;
    private final float yMax;

    public Face(String id, String faceCrop, float xMin, float yMin, float xMax, float yMax) {
        this.id = id;
        this.faceCrop = faceCrop;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public String getId() {
        return id;
    }

    public String getFaceCrop() {
        return faceCrop;
    }

    public float getXMin() {
        return xMin;
    }

    public float getYMin() {
        return yMin;
    }

    public float getXMax() {
        return xMax;
    }

    public float getYMax() {
        return yMax;
    }
}
