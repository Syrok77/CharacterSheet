package com.paragonfervour.charactersheet.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.stats.helper.StatHelper;

/**
 * Control that display a skill. Shows the name of the skill and the current skill modifier. Tapping
 * the control opens a dialog that allows the value to be edited.
 */
public class SkillValueComponent extends LinearLayout {

    private Button mSkillButton;

    private String mSkillName = "";
    private int mModifier = 2;

    public SkillValueComponent(Context context) {
        super(context);
        init(null);
    }

    public SkillValueComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SkillValueComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.component_skill_value, this);

        mSkillButton = (Button) findViewById(R.id.component_skill_button);

        mSkillButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Not yet implemented.", Toast.LENGTH_SHORT).show();
            }
        });

        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.SkillValueComponent);

            if (attributes != null) {
                mSkillName = attributes.getString(R.styleable.SkillValueComponent_skillName);
                int color = attributes.getColor(R.styleable.SkillValueComponent_skillBackground, 0);
                if (color != 0) {
//                    mSkillButton.setBackgroundTintList(ColorStateList.valueOf(color));
                    ViewCompat.setBackgroundTintList(mSkillButton, ColorStateList.valueOf(color));
                }

                mSkillButton.setText(getButtonText());

                attributes.recycle();
            }
        }
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
