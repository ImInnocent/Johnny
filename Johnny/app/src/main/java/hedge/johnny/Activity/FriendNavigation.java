package hedge.johnny.Activity;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by EDGE01 on 2015-08-03.
 */
public class FriendNavigation extends ActivityGroup {

    private LocalActivityManager manager;

    private View mFriend;
    private View mAddFriend;

    private int mNowView;
//    private View mDeleteFriend;

//    private static FriendNavigation instance = new FriendNavigation();
//
//    public static FriendNavigation getInstance(){
//        return instance;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        manager = getLocalActivityManager();

//        mDeleteFriend = manager.startActivity("친구 삭제", new Intent(this, DeleteFriendActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
        mAddFriend = manager.startActivity("친구 추가", new Intent(this, AddFriendActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
        mFriend = manager.startActivity("친구", new Intent(this, FriendActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

        setContentView(mFriend);

        mNowView = 0;
    }

    public void setContentView(String childId)
    {
        if(childId.equals("친구"))
        {
            setContentView(mFriend);
        }
        else if(childId.equals("친구 추가"))
        {
            setContentView(mAddFriend);
        }
//        else if(childId.equals("친구 삭제"))
//        {
//            setContentView(mDeleteFriend);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(mNowView == 0)
            {
                finish();
            }
            else{
                setContentView("친구");
            }
        }

        return true;
    }
}
