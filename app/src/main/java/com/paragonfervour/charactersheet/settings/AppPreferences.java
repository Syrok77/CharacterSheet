package com.paragonfervour.charactersheet.settings;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;

import roboguice.inject.ContextSingleton;

/**
 * Access point for all Settings values!
 */
@ContextSingleton
public class AppPreferences {

    private Context mContext;

    @Inject
    public AppPreferences(Context context, Activity activity) {
        mContext = context;
    }

    /**
     * Get whether or not the user enabled haptic feedback for controls.
     *
     * @return true if haptic feedback is allowed.
     */
    public boolean getEnabledHapticFeedback() {
        String key = mContext.getString(R.string.PREF_HAPTIC_FEEDBACK);
        boolean def = mContext.getResources().getBoolean(R.bool.PREF_HAPTIC_FEEDBACK_DEFAULT);
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(key, def);
    }

}
