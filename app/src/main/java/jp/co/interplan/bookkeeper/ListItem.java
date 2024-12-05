package jp.co.interplan.bookkeeper;

public class ListItem {
    private String imageUrl;
    private String topText;
    private String bottomText;
    private String isbn;

    public ListItem(String isbn,String imageUrl, String topText, String bottomText) {
        this.isbn = isbn;
        this.imageUrl = imageUrl;
        this.topText = topText;
        this.bottomText = bottomText;
    }
    public ListItem(String imageUrl, String topText, String bottomText) {
        this.imageUrl = imageUrl;
        this.topText = topText;
        this.bottomText = bottomText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTopText() {
        return topText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public String getIsbn() {
        return isbn;
    }
}
