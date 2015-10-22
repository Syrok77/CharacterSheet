package com.paragonfervour.charactersheet.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.paragonfervour.charactersheet.R;


/**
 * Fragment that displays/manages user settings.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
    }
}
