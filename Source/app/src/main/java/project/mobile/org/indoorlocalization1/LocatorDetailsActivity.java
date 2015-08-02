package project.mobile.org.indoorlocalization1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by ABHISHEK on 14-03-2015.
 */
public class LocatorDetailsActivity extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener{

    private Handler mHandler = new Handler();
    private boolean isBack = false;
    public static int number_of_floors;
    private String mBuildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate_yourself);

        Bundle extras = getIntent().getExtras();
        number_of_floors = Integer.parseInt(extras.getString("number_of_floors"));

        if(savedInstanceState == null)
            getFragmentManager().beginTransaction().add(R.id.frame_container, new Current_Location()).commit();

        else
            isBack = (getFragmentManager().getBackStackEntryCount() > 0);

        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, R.id.action_change, Menu.NONE, isBack ? R.string.current_location : R.string.navigate);
        item.setIcon(isBack ? R.drawable.ic_menu_location : R.drawable.ic_navigation);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_change:
                change_fragment_layout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void change_fragment_layout(){
        if(isBack){
            getFragmentManager().popBackStack();
            return;
        }
        isBack = true;
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.locator_right_in,R.animator.locator_right_out,
                        R.animator.locator_left_in, R.animator.locator_left_out)
                .replace(R.id.frame_container, new Navigate())
                .addToBackStack(null)
                .commit();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });

    }

    @Override
    public void onBackStackChanged() {
        isBack = (getFragmentManager().getBackStackEntryCount() > 0);
        invalidateOptionsMenu();
    }

}
