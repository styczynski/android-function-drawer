package com.example.extremetic_tac_toe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.extremetic_tac_toe.FunctionPanelActivity.ListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
 
public class FunctionPanelChooseFunctOpActivity extends Activity implements OnChildClickListener {
 
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_panel_select_funct_op);
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.functionOpSelectionList);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(this);
        
        Button button = (Button) findViewById(R.id.function_panel_select_funct_op_back_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        /*
        * Function properties
			- function properties
			- zero-places finding
			- find value range
		* Function integrating/differentials
			- derivating
			- integration
		* Behaviour predicting
			- find asympthotes
			- find limits
		* Geometrical characteristics
			- graph field
			- graph length
			- intersections
			- average value
        */
        
        listDataHeader.add("Function properties");
        listDataHeader.add("Function integrating/differentials");
        listDataHeader.add("Behaviour predicting");
        listDataHeader.add("Geometrical characteristics");
 
        List<String> catA = new ArrayList<String>();
        catA.add("Function properties");
        catA.add("Zero-places finding");
        catA.add("Finding value range");
 
        List<String> catB = new ArrayList<String>();
        catB.add("Derivating");
        catB.add("Integration");
 
        List<String> catC = new ArrayList<String>();
        catC.add("Finding asympthotes");
        catC.add("Finding function limits");
        
        List<String> catD = new ArrayList<String>();
        catD.add("Graph field");
        catD.add("Graph length");
        catD.add("Intersections");
        catD.add("Average value");
        
 
        listDataChild.put(listDataHeader.get(0), catA);
        listDataChild.put(listDataHeader.get(1), catB);
        listDataChild.put(listDataHeader.get(2), catC);
        listDataChild.put(listDataHeader.get(3), catD);
    }

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		String str = listAdapter.getChild(groupPosition, childPosition).toString();
		if(str.equals("Function properties")) {
			Intent intent = new Intent(FunctionPanelChooseFunctOpActivity.this, FunctionPanelFunctionPropsActivity.class);
  	    	startActivity(intent);
		}
		
		return false;
	}
}