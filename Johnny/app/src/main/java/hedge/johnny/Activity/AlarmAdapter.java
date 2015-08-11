package hedge.johnny.Activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import hedge.johnny.R;

/**
 * Created by Administrator on 2015-08-03.
 */
public class AlarmAdapter extends ArrayAdapter<Alarm>{
    ArrayList<Alarm> items;
    Context context;

    public AlarmAdapter(Context context, int resource, ArrayList<Alarm> objects) {
        super(context, resource, objects);

        this.context = context;     // 외부에서 사용하려면 따로 필요해서..
        items = objects;            // 따로 필요해서..
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;   // 각 아이템이 늘어날때, 계속 뷰로 늘리지 않고 swap해준다.
        if(v == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.alarm_row, null);
        }

        Alarm p = items.get(position);
        if(p != null)
        {
            // 오전 오후
            TextView tx_AmPm = (TextView) v.findViewById(R.id.tx_AmPm);
            if( tx_AmPm != null){
                if(p.getAmPm() == false)        tx_AmPm.setText("오전");
                else                            tx_AmPm.setText("오후");
            }

            // 알람 시간
            TextView tx_AlarmTime = (TextView) v.findViewById(R.id.tx_AlarmTime);
            if( tx_AlarmTime != null)           tx_AlarmTime.setText(p.getTime());

            boolean[] days = p.getDays();
            // 월
            TextView tx_Mon = (TextView) v.findViewById(R.id.tx_Mon);
            if( tx_Mon != null){
                if(days[0] == true)     tx_Mon.setTextColor(Color.parseColor("#bd3fff"));
                else                    tx_Mon.setTextColor(Color.parseColor("#000000"));
            }

            // 화
            TextView tx_Tue = (TextView) v.findViewById(R.id.tx_Tue);
            if( tx_Tue != null){
                if(days[1] == true)     tx_Tue.setTextColor(Color.parseColor("#bd3fff"));
                else                    tx_Tue.setTextColor(Color.parseColor("#000000"));
            }

            // 수
            TextView tx_Wed = (TextView) v.findViewById(R.id.tx_Wed);
            if( tx_Wed != null){
                if(days[2] == true)     tx_Wed.setTextColor(Color.parseColor("#bd3fff"));
                else                    tx_Wed.setTextColor(Color.parseColor("#000000"));
            }

            // 목
            TextView tx_Thu = (TextView) v.findViewById(R.id.tx_Thu);
            if( tx_Thu != null){
                if(days[3] == true)     tx_Thu.setTextColor(Color.parseColor("#bd3fff"));
                else                    tx_Thu.setTextColor(Color.parseColor("#000000"));
            }

            // 금
            TextView tx_Fri = (TextView) v.findViewById(R.id.tx_Fri);
            if( tx_Fri != null){
                if(days[4] == true)     tx_Fri.setTextColor(Color.parseColor("#bd3fff"));
                else                    tx_Fri.setTextColor(Color.parseColor("#000000"));
            }

            // 토
            TextView tx_Sat = (TextView) v.findViewById(R.id.tx_Sat);
            if( tx_Sat != null){
                if(days[5] == true)     tx_Sat.setTextColor(Color.parseColor("#bd3fff"));
                else                    tx_Sat.setTextColor(Color.parseColor("#000000"));
            }

            // 일
            TextView tx_Sun = (TextView) v.findViewById(R.id.tx_Sun);
            if( tx_Sun != null){
                if(days[6] == true)     tx_Sun.setTextColor(Color.parseColor("#bd3fff"));
                else                    tx_Sun.setTextColor(Color.parseColor("#000000"));
            }
        }

        ImageButton btnModi = (ImageButton) v.findViewById(R.id.btn_modify);
        ImageButton btnDel = (ImageButton) v.findViewById(R.id.btn_del);

        btnModi.setTag(position);
        btnDel.setTag(position);

        return v;
    }
}
