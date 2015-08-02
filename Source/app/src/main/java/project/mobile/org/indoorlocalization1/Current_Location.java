package project.mobile.org.indoorlocalization1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ABHISHEK on 15-03-2015.
 */
public class Current_Location extends Fragment {

    Context mContext;
    public String[] mFloorNumber = new String[LocatorDetailsActivity.number_of_floors+1];
    public String[] mListOfSigns;
    private AutoCompleteTextView mSelectSign;
    private TextView mChooseSignInstruc;
    private Spinner mSelectFloor;
    private RelativeLayout relative;
    private String mDebug = Current_Location.class.getName();
    private String floor_selected = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_current_location, container, false);
        init();
        mSelectFloor = (Spinner)view.findViewById(R.id.spinner_select_floor);
        mChooseSignInstruc = (TextView)view.findViewById(R.id.text_choose_sign);
        mSelectSign = (AutoCompleteTextView)view.findViewById(R.id.auto_complete_sign_selection);
        relative = (RelativeLayout)view.findViewById(R.id.relativeLayout_for_select_sign);

        CustomAdapter mAdapter = new CustomAdapter(getActivity(),R.layout.layout_floor_list,R.id.floor_number,mFloorNumber);
        mSelectFloor.setAdapter(mAdapter);

        mSelectFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /* Stores the value of floor selected in a string variable. It then
                compares it with "Select a Floor" to match if the floor selected is a Floor number.
                If the selected floor is a floor number then the relative layout allowing the user
                to select a sign is made visible and the AutoCompleteTextView is populated with
                signs relevant to the floor selected.
                 */
                floor_selected = parent.getItemAtPosition(position).toString();
                if(!floor_selected .equalsIgnoreCase("Select a Floor") && !floor_selected .equalsIgnoreCase("")) {
                    relative.setVisibility(View.VISIBLE);
                    mChooseSignInstruc.setVisibility(View.VISIBLE);
                    DatabaseHelperAdapter mDatabaseHelperAdapter = DatabaseHelperAdapter.getInstance(mContext);
                    mListOfSigns = mDatabaseHelperAdapter.getSigns(floor_selected);
                    Log.d(mDebug, "The value of mListOfSigns inside Current_Location is: "+ mListOfSigns);
                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mListOfSigns);
                    mSelectSign.setAdapter(mAdapter);
                }
                else {
                    relative.setVisibility(View.INVISIBLE);
                    mChooseSignInstruc.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSelectSign.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /* When the user clicks DONE in soft key board, the code checks if the text entered
                by the user has been selected from the populated options available, or the user has
                entered his own text. If the text does not exists in the populated list then the user
                is shown a toast telling him to select a sign from the list.
                 */
                if(actionId == EditorInfo.IME_ACTION_GO){
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    String mSignEntered = mSelectSign.getText().toString();
                    int result = 0;
                    for(int i = 0; i < mListOfSigns.length; i++){
                        if(!mSignEntered.equalsIgnoreCase(mListOfSigns[i])){
                            continue;
                        }
                        else{
                            result = 1;
                            break;
                        }
                    }
                    if(result != 1)
                        Toast.makeText(mContext,"Please select a sign from the list",Toast.LENGTH_LONG).show();
                    else{
                        String mLocationRetrieved = DatabaseHelperAdapter.getInstance(mContext).getLocation(floor_selected,mSignEntered);
                        Intent intent = new Intent(getActivity(),FindOnMap.class);
                        intent.putExtra("coordinates",mLocationRetrieved);
                        startActivity(intent);
                    }

                }
                return false;
            }
        });
        return view;
    }

    private void init(){

        mContext = getActivity().getApplicationContext();
        mFloorNumber[0] = "Select a Floor";
        // initialize the mFloorNumber array with the Floor values.
        for(int i = 1; i < LocatorDetailsActivity.number_of_floors+1; i++)
            mFloorNumber[i] = "Floor "+i;
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private String[] mValuesList;
        Context mContext;
        private int layout;
        private int text_view_id;
        public CustomAdapter(Context mContext, int resource, int textViewResourceId, String[] mSelectSign) {
            super(mContext, resource, textViewResourceId, mSelectSign);
            this.layout = resource;
            this.text_view_id = textViewResourceId;
            this.mValuesList = mSelectSign;
            this.mContext = mContext;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if(row == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layout,parent,false);
            }
            TextView mText = (TextView)row.findViewById(text_view_id);
            mText.setText(mValuesList[position]);
            return row;
        }
    }
}
