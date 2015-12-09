package com.paragonfervour.charactersheet.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.stats.helper.StatHelper;

/**
 * Control that display a skill. Shows the name of the skill and the current skill modifier. Tapping
 * the control opens a dialog that allows the value to be edited.
 */
public class SkillValueViewComponent extends LinearLayout {

    private Button mSkillButton;
    private OnClickListener mClickListener;

    private String mSkillName = "";
    private int mModifier = 0;

    public SkillValueViewComponent(Context context) {
        super(context);
        init(null);
    }

    public SkillValueViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SkillValueViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.skill_value_component, this);

        mSkillButton = (Button) findViewById(R.id.component_skill_button);

        mSkillButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClick(v);
            }
        });

        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.SkillValueViewComponent);

            if (attributes != null) {
                setSkillName(attributes.getString(R.styleable.SkillValueViewComponent_skillName));
                int color = attributes.getColor(R.styleable.SkillValueViewComponent_skillBackground, 0);
                setComponentColor(color);

                attributes.recycle();
            }
        }
    }

    /**
     * Set background color of this component.
     *
     * @param color Color for the background.
     */
    public void setComponentColor(@ColorInt int color) {
        if (color != 0) {
            ViewCompat.setBackgroundTintList(mSkillButton, ColorStateList.valueOf(color));
        }
    }

    /**
     * Set the name of this skill.
     *
     * @param name skill name.
     */
    public void setSkillName(String name) {
        mSkillName = name;
        mSkillButton.setText(getButtonText());
    }

    public String getSkillName() {
        return mSkillName;
    }

    /**
     * Set the skill's modifier value.
     *
     * @param modifier new mod value.
     */
    public void setSkillModifier(int modifier) {
        mModifier = modifier;
        mSkillButton.setText(getButtonText());
    }

    /**
     * Receive click events for this component.
     *
     * @param onClickListener click listener for this component.
     */
    public void setSkillClickListener(OnClickListener onClickListener) {
        mClickListener = onClickListener;
    }

    /**
     * Get formatted button text in the form "{NAME}: {+/-}{MOD}"
     *
     * @return Formatted string for button text.
     */
    private String getButtonText() {
        String modifierIndicator = StatHelper.getStatIndicator(mModifier);
        String formatString = getContext().getString(R.string.skill_button_text_format);
        return String.format(formatString, mSkillName, modifierIndicator, mModifier);
    }
}
