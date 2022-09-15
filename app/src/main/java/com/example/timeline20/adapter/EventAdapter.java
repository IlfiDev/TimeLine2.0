
package com.example.timeline20.adapter;

        import android.content.Context;
        import android.os.Build;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.EditText;
        import android.widget.TextView;

        import androidx.annotation.RequiresApi;

        import com.example.timeline20.Note;
        import com.example.timeline20.R;

        import org.w3c.dom.Text;

        import java.util.LinkedList;
        import java.util.List;

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

        text.setText(list.get(i).GetText());
        time.setText(list.get(i).GetTime());
        return newView;
    }
}
