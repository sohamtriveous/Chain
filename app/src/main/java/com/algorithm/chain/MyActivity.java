package com.algorithm.chain;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MyActivity extends Activity {
    // UI element references
    private EditText mQuery;
    private ProgressBar mProgress;
    private LinearLayout mResultsLinearLayout;
    private FrameLayout mResultsFrameLayout;
    private TextView mResultsTextView;
    private TextView mResultsLabelTextView;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // assigning UI element references
        mQuery = (EditText) findViewById(R.id.activity_my_enter);
        mProgress = (ProgressBar) findViewById(R.id.activity_my_progress);
        mResultsLinearLayout = (LinearLayout) findViewById(R.id.activity_my_results_linearlayout);
        mResultsFrameLayout = (FrameLayout) findViewById(R.id.activity_my_results_framelayout);
        mResultsTextView = (TextView) findViewById(R.id.activity_my_results_textview);
        mResultsLabelTextView = (TextView) findViewById(R.id.activity_my_results_label);
        mStartButton = (Button) findViewById(R.id.activity_my_start);

        // perform the chain traversal operation on click of the start button
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuery.getText() != null) {
                    // perform the chain traversal on a separate thread
                    // pass the contents of the edit text
                    new chainTraverse().execute(new String[]{mQuery.getText().toString().toLowerCase()});
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class chainTraverse extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // ui initialization
            mResultsFrameLayout.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.VISIBLE);
            mResultsLinearLayout.setVisibility(View.GONE);
            mResultsTextView.setVisibility(View.GONE);
            mResultsLabelTextView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            String text = params[0];
            // this is where we createLookup our lookup, please have a look at the Chain class for more detail
            Chain.createLookup(text);
            // traverse and return the string result (which is the text you see in the result text box)
            return Chain.traverse();
        }

        @Override
        protected void onPostExecute(String result) {
            // display the result post processing
            mResultsTextView.setText(result);
            mProgress.setVisibility(View.GONE);
            mResultsTextView.setVisibility(View.VISIBLE);
            mResultsLabelTextView.setVisibility(View.VISIBLE);
            mResultsLinearLayout.setVisibility(View.VISIBLE);
            super.onPostExecute(result);
        }
    }
}
