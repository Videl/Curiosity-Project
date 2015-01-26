package org.fslhome.videl.curiosityapplication.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by videl on 26/01/15.
 * This class is about adapting an array of String[2] to a ListView.
 */
public class ThreeLineListAdapter extends ArrayAdapter<String[]>{
    /** To cache views of item */
    private static class ViewHolder {
        private TextView text1;
        private TextView text2;

        /**
         * General constructor
         */
        ViewHolder() {
            // nothing to do here
        }
    }
    private final LayoutInflater inflater;

    public ThreeLineListAdapter(Context context, int resource) {
        super(context, resource);

        this.inflater = LayoutInflater.from(context);

    }

    /**
     * This method is called every time a line is going to be displayed.
     * @param position Internal position that I need to use to get the correct data displayed
     *                 by the line.
     * @param convertView The view I am going to use. This class is only going to be used by
     *                    CuriositiesActivity so I know which layout I have, but I should
     *                    find a solution for this in case it gets changed one day.
     * @param parent Used when inflating the current view.
     * @return The view to get displayed. (TextEdit with 2 rows)
     */
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;
        final String[] item = getItem(position);

        if(null == itemView) {
            itemView = this.inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);

            holder = new ViewHolder();

            holder.text1 = (TextView)itemView.findViewById(android.R.id.text1);
            holder.text2 = (TextView)itemView.findViewById(android.R.id.text2);

            itemView.setTag(holder);
        } else {
            holder = (ViewHolder)itemView.getTag();
        }

        holder.text1.setText("[" + position + "]" + item[0]);
        holder.text2.setText(item[1]);

        return itemView;
    }
}
