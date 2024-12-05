package jp.co.interplan.bookkeeper;

public interface UrlResponseCallback {
    void onResponse(String result);
    void onError(Exception e);
}
