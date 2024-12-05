package jp.co.interplan.bookkeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<ListItem> {

    public CustomAdapter(Context context, List<ListItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ListItem item = getItem(position);

        ImageView imageView = convertView.findViewById(R.id.item_image);
        TextView topTextView = convertView.findViewById(R.id.item_text_top);
        TextView bottomTextView = convertView.findViewById(R.id.item_text_bottom);
        Button button = convertView.findViewById(R.id.item_button);

        if (item != null) {
            // Load image using Glide or any other image loading library
            Glide.with(getContext()).load(item.getImageUrl()).into(imageView);

            topTextView.setText(item.getTopText());
            bottomTextView.setText(item.getBottomText());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click
                }
            });
        }

        return convertView;
    }
}
