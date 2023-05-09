package edu.sjsu.android.finalproject;

public class CategoryItem {

    private final String id;
    private final String name;
    private final String length;

    public CategoryItem(String id, String name, String length) {
        this.id = id;
        this.name = name;
        this.length = length;
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
}
