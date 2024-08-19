package com.steve_md.smartmkulima.utils.services;

public class SpinnerItem {
    private String id;
    private String title;
    private int image;

    /**
     * Instantiates a new Spinner item.
     *
     * @param id    the id
     * @param title the title
     * @param image the image
     */
    public SpinnerItem(String id, String title, int image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    /**
     * Instantiates a new Spinner item.
     *
     * @param title the title
     * @param image the image
     */
    public SpinnerItem(String title, int image) {
        this.title = title;
        this.image = image;

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public int getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(int image) {
        this.image = image;
    }


}


