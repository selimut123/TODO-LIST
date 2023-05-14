package edu.sjsu.android.finalproject;

public class CategoryItem {

    private final String id;
    private final String name;
    private final String length;
    private final int imageID;
    private final int colorID;
    private final int backgroundColorID;

    public CategoryItem(String id, String name, String length, int imageID, int colorID, int backgroundColorID) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.imageID = imageID;
        this.colorID = colorID;
        this.backgroundColorID = backgroundColorID;
    }

    public String getName() {
        return this.name;
    }
    public String getId() {
        return this.id;
    }

    public String getLength() {
        return this.length;
    }
    public int getImageID() {
        return this.imageID;
    }
    public int getColorID() {
        return this.colorID;
    }
    public int getBackgroundImageID() {
        return this.backgroundColorID;
    }

}
