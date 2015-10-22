package com.paragonfervour.charactersheet.settings;

import android.os.Bundle;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;


/**
 * App Settings view.
 */
public class SettingsActivity extends ComponentBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_fragment);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_fragment_container, new SettingsFragment())
                .commit();
    }
}
