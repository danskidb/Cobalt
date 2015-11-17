package danski.dotareader.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import danski.dotareader.R;

/**
 * Created by danny on 17-11-15.
 */
public class Adapter_match_me extends BaseAdapter{

    //TODO: http://codingdiscovery.blogspot.nl/2015/02/android-listviews-with-headers.html

    ArrayList<String> text;
    ArrayList<String> imgurl;
    Context context;
    private static LayoutInflater inflater = null;

    public Adapter_match_me(Context context, ArrayList<String> _text, ArrayList<String> _imgurl) {
        this.context = context;
        text = _text;
        imgurl = _imgurl;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int position) {
        return text.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            if(position == 0 || position == 7){
                vi = inflater.inflate(R.layout.item_header, null);
                TextView txt = (TextView) vi.findViewById(R.id.header_text);
                txt.setText(text.get(position));

                Log.i("AMM", "Created header");

            } else {
                vi = inflater.inflate(R.layout.item_match_me, null);

                TextView txt = (TextView) vi.findViewById(R.id.imm_text);
                txt.setText(text.get(position));

                TextView stat = (TextView) vi.findViewById(R.id.imm_stat);
                stat.setVisibility(View.GONE);

                ImageView img = (ImageView) vi.findViewById(R.id.imm_img);
                if(imgurl.get(position).equals("0")){
                    img.setVisibility(View.GONE);
                } else {
                    img.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(imgurl.get(position)).into(img);
                }

                Log.i("AMM", "Created content");

            }
        }



        return vi;
    }

}
