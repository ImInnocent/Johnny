package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hedge.johnny.R;

/**
 * Created by Administrator on 2015-07-21.
 */
public class FriendActivity extends Activity implements AdapterView.OnItemClickListener {

    FriendAdapter adapter;
    ArrayList<String> array;
    ListView listView;
    //ImageButton btn_add, btn_del, btn_modi;
    int selectPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        init();
    }

    void init()
    {
        selectPos = -1; // 변수 초기화
//        btn_add = (ImageButton)findViewById(R.id.btn_add);
//        btn_del = (ImageButton)findViewById(R.id.btn_del);
//        btn_modi = (ImageButton)findViewById(R.id.btn_modify);

        listView = (ListView)findViewById(R.id.friendList);
        listView.setDivider(new ColorDrawable(Color.WHITE)); // 리스트내 아이템간 경계선
        listView.setDividerHeight(5);

        // 데이터 삽입
        array = new ArrayList<String>();
        array.add("친구 000");        array.add("친구 001");    array.add("친구 002");

        // 배열 연결
        adapter = new FriendAdapter(this, R.layout.friend_row, array);
        listView.setAdapter(adapter);

        //listView.setOnItemClickListener(this);      // 클릭 리스너

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), array.get(position), Toast.LENGTH_SHORT).show();
        selectPos = position;
    }



    public void btnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_add:
                Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                // 친구 추가 다이얼로그 띄우기
                Intent intent = new Intent(FriendActivity.this, AddFriendActivity.class);
                startActivity(intent);
                //array.add("");
                adapter.notifyDataSetChanged(); // array의 데이터가 변경되었으니 다시 불러오기
                break;
            case R.id.btn_del:
                Toast.makeText(getApplicationContext(), (Integer)v.getTag() + " del", Toast.LENGTH_SHORT).show();
//                Intent intent2 = new Intent(FriendActivity.this, DeleteFriendActivity.class);
//                startActivity(intent2);
//                //array.remove();
//                adapter.notifyDataSetChanged(); // array의 데이터가 변경되었으니 다시 불러오기
                break;
            case R.id.btn_modify:
                Toast.makeText(getApplicationContext(), (Integer)v.getTag() + " mod", Toast.LENGTH_SHORT).show();
//                Intent intent3 = new Intent(FriendActivity.this, DeleteFriendActivity.class);
//                startActivity(intent3);

                break;
            default:
                break;
        }
    }
}