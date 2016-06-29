package com.paragonfervour.charactersheet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;

/**
 * View Component that displays a Material Design styled sub-header for use inside of lists. Available
 * options include setting the title as well as turning on a key-line divider on the top of this view.
 */
public class ListSubHeaderComponent extends FrameLayout {

    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    View mDividerView;

    TextView mHeaderText;

    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------
    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------
    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------
    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public ListSubHeaderComponent(Context context) {
        super(context);
        init(null);
    }

    public ListSubHeaderComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ListSubHeaderComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // This component should always be the size of the parent
        LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        inflate(getContext(), R.layout.list_sub_header_component, this);

        // TODO: bind
        mDividerView = findViewById(R.id.list_sub_header_component_divider);
        mHeaderText = (TextView) findViewById(R.id.list_sub_header_component_text);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.ListSubHeaderComponent);

            boolean showDivider = a.getBoolean(R.styleable.ListSubHeaderComponent_showDivider, false);
            String headerText = a.getString(R.styleable.ListSubHeaderComponent_headerText);

            setIsTopDividerVisible(showDivider);
            setHeaderText(headerText);

            a.recycle();
        }
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------
    //endregion

    //region WIDGET --------------------------------------------------------------------------------
    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------

    public void setHeaderText(@StringRes int headerText) {
        mHeaderText.setText(getResources().getString(headerText));
    }

    /**
     * Change the displayed text.
     *
     * @param headerText text to display in the header.
     */
    public void setHeaderText(String headerText) {
        mHeaderText.setText(headerText);
    }

    /**
     * Show or hide the top divider on this view. The default value is false.
     *
     * @param showTopDivider true to show divider, false to hide divider.
     */
    public void setIsTopDividerVisible(boolean showTopDivider) {
        mDividerView.setVisibility(showTopDivider ? View.VISIBLE : View.GONE);
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}