package danski.cobalt.Adaptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import danski.cobalt.R;
import danski.cobalt.StatsActivity.Stat;

/**
 * Created by danny on 30-9-15.
 */
public class Adapter_Preferences_Graphs extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    Stat[] stats;
    SharedPreferences.Editor editor;

    public Adapter_Preferences_Graphs(Context context, Stat[] stats) {
        this.context = context;
        this.stats = stats;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stats.length;
    }

    @Override
    public Object getItem(int position) {
        return stats[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.item_checklist, null);

        //name
        TextView name = (TextView) vi.findViewById(R.id.checklist_name);
        name.setText(stats[position].title);

        //Checkbox logic
        final int pos = position;
        final CheckBox enabled = (CheckBox) vi.findViewById(R.id.checklist_checkbox);
        editor = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit();

        enabled.setChecked(stats[pos].enabled);
        enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) enabled).isChecked()) {
                    editor.putBoolean(stats[pos].title, true);
                    editor.apply();
                } else {
                    editor.putBoolean(stats[pos].title, false);
                    editor.apply();
                }
            }
        });

        return vi;
    }
}
