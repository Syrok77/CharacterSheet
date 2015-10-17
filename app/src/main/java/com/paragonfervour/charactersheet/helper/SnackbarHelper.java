package com.paragonfervour.charactersheet.helper;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;

/**
 * Helper that shows snackbars. This is only necessary because the text color on the snackbar will
 * be wrong, so pass snackbars through here and this will take care of that kind of formatting.
 */
public final class SnackbarHelper {

    private SnackbarHelper() {
    }

    /**
     * Format and display the given Snackbar.
     *
     * @param context Android context.
     * @param snackbar Snackbar to show.
     */
    public static void showSnackbar(Context context, Snackbar snackbar) {
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        // noinspection deprecation
        tv.setTextColor(context.getResources().getColor(R.color.default_white));
        snackbar.show();
    }

}
