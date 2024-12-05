package jp.co.interplan.bookkeeper;

import static jp.co.interplan.bookkeeper.DLog.dlog;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    CustomAdapter adapter;
    List<ListItem> items;
    Scanner scanner;
    IScanner iScanner;

    public static final String iss = "https://iss.ndl.go.jp/api/sru?operation=searchRetrieve&version=1.2&recordSchema=dcndl&onlyBib=true&recordPacking=xml&query=isbn=\"{ISBN}\"&dpid=iss-ndl-opac\n";
    public static final String ndl = "https://ndlsearch.ndl.go.jp/thumbnail/{ISBN}.jpg";
    public static final String google = "https://www.googleapis.com/books/v1/volumes?q=isbn:{ISBN}&key=AIzaSyD6RQmvi8Gh1mDJg8_pekCVvo2BosFXiyM";
    public static final String openBD = "https://api.openbd.jp/v1/get?isbn={ISBN}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iScanner = this::foundIsbn;
        scanner = new Scanner(this, iScanner);
        Button btn = findViewById(R.id.btn_start);
        btn.setOnClickListener((s) -> {
            scanner.start();
        });
        listView = findViewById(R.id.book_list);
        items = new ArrayList<>();
        adapter = new CustomAdapter(this, items);
        listView.setAdapter(adapter);

    }

    public String toOpenBd(String isbn) {
        return openBD.replace("{ISBN}", isbn);
    }

    public JSONObject OpenBdJson(String s) {
        JSONObject o = null;
        try {
            s = s.trim();
            if (s.startsWith("[")) {
                JSONArray a = new JSONArray(s);
                o = a.getJSONObject(0);
            } else {
                o = new JSONObject(s);
            }
        } catch (Exception e) {
            dlog("" + e);
        }
        return o;
    }

    public void addItem(String isbn, String imageUrl, String title, String author) {
        boolean exist = false;
        for (ListItem item : items) {
            if (item == null || item.getIsbn() == null) continue;
            if (item.getIsbn().equals(isbn)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            items.add(new ListItem(isbn, imageUrl, title, author));
            adapter.notifyDataSetChanged();
        }
    }

    public void OpenBd(String isbn) {
        String url = toOpenBd(isbn);
        dlog("url:" + url);
        NetworkUtils.fetchUrl(url, new UrlResponseCallback() {
            @Override
            public void onResponse(String result) {
                JSONObject obj = OpenBdJson(result);
                if (obj != null) {
                    try {
                        JSONObject summery = obj.getJSONObject("summary");
                        String title = summery.getString("title");
                        String author = summery.getString("author");
                        addItem(isbn, null, title, author);
                        dlog("added " + title);
                    } catch (Exception e) {
                        dlog("" + e);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                dlog("" + e);
            }
        });
    }

    public void foundIsbn(String isbn) {
        OpenBd(isbn);
    }
}