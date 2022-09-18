
package com.example.timeline20.adapter;

        import android.content.Context;
        import android.os.Build;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import androidx.annotation.RequiresApi;

        import com.example.timeline20.Note;
        import com.example.timeline20.R;

        import java.util.Calendar;
        import java.util.LinkedList;

public class EventAdapter extends BaseAdapter {
    private LinkedList<Note> list;
    private LayoutInflater layoutInflater;
    private int resourceLayout;

    public EventAdapter(Context context, int resource, LinkedList<Note> list){
        this.list = (LinkedList<Note>) list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resourceLayout = resource;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        if(newView == null){
            newView = layoutInflater.inflate(resourceLayout, null);


        }
        TextView text = (TextView) newView.findViewById(R.id.event_content_textview);
        TextView time = (TextView) newView.findViewById(R.id.event_time_textview);
        //EditText edit_content = (EditText) newView.findViewById((R.id.note))
        Note newNote = (Note) getItem(i);
        if(isOld(i)) {
            newView.setBackgroundResource(R.drawable.shape_old_event);
        }


        text.setText(list.get(i).GetLabel());
        time.setText(list.get(i).GetTime());
        return newView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isOld(int i) {
        Calendar temp_calendar = Calendar.getInstance();
        int now_year = temp_calendar.get(Calendar.YEAR);
        int now_month = temp_calendar.get(Calendar.MONTH);
        int now_dayOfMonth = temp_calendar.get(Calendar.DAY_OF_MONTH);

        int now_hourOfDay = temp_calendar.get(Calendar.HOUR_OF_DAY);
        int now_minites = temp_calendar.get(Calendar.MINUTE);

        Calendar calendar = Calendar.getInstance();

        String temp_date = list.get(i).GetFullFullDate();
        String[] temp_arr_date = temp_date.split("\\.");
        calendar.set(Calendar.YEAR, Integer.parseInt(temp_arr_date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(temp_arr_date[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp_arr_date[0]));

        String temp_time = list.get(i).GetTime();
        String[] temp_timp_arr = temp_time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp_timp_arr[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(temp_timp_arr[1]));

        if ((calendar.get(Calendar.YEAR) == now_year) && (calendar.get(Calendar.MONTH) == now_month) &&
                (calendar.get(Calendar.DAY_OF_MONTH) == now_dayOfMonth)) {

            if (calendar.get(Calendar.HOUR_OF_DAY) < now_hourOfDay) {
                return true;
            } else {
                if ((calendar.get(Calendar.HOUR_OF_DAY) == now_hourOfDay) &&
                        (calendar.get(Calendar.MINUTE) < now_minites)) {
                    return true;
                }
            }

        }

        if ((calendar.get(Calendar.YEAR) < now_year) || (calendar.get(Calendar.MONTH) < now_month) ||
                (calendar.get(Calendar.DAY_OF_MONTH) < now_dayOfMonth)) {
            return true;
        }

        return false;
    }
}
