package com.sociallearn.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;

/**
 * Created by Sarath on 17-07-2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shouldAddCommonMenuItems()) {
            getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        }
        return true;
    }

    /**
     * Return false, if you do not want base class to add menus.
     *
     * @return
     */
    protected boolean shouldAddCommonMenuItems() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chat:
                startChatConversations();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startChatConversations() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

}
