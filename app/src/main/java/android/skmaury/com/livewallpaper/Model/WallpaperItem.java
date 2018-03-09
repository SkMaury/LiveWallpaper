package android.skmaury.com.livewallpaper.Model;

/**
 * Created by kurosaki on 09/03/18.
 */

public class WallpaperItem {
    private String imageLink;
    private String categoryId;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public WallpaperItem(String imageLink, String categoryId) {

        this.imageLink = imageLink;
        this.categoryId = categoryId;
    }


    public WallpaperItem() {
    }
}
