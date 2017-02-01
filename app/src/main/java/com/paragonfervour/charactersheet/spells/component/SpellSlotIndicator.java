package com.paragonfervour.charactersheet.spells.component;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.paragonfervour.charactersheet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class SpellSlotIndicator extends FrameLayout {

    private static final int MAX_SPELL_SLOTS = 5;

    @BindView(R.id.spell_slot_indicator_remove)
    View mSpellSlotRemove;

    @BindView(R.id.spell_slot_indicator_add)
    View mSpellSlotAdd;

    @BindView(R.id.spell_slot_indicator_checkbox_container)
    ViewGroup mCheckBoxContainer;

    public SpellSlotIndicator(Context context) {
        super(context);
        init();
    }

    public SpellSlotIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpellSlotIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.spells_spell_slot_component, this);
        ButterKnife.bind(this);

        updateControl();
    }

    @OnClick(R.id.spell_slot_indicator_remove)
    void onRemoveSpellSlot() {
        int childCount = mCheckBoxContainer.getChildCount();
        if (childCount > 0) {
            mCheckBoxContainer.removeViewAt(childCount - 1);
        }

        updateControl();
    }

    @OnClick(R.id.spell_slot_indicator_add)
    void onAddSpellSlotClick() {
        if (mCheckBoxContainer.getChildCount() < MAX_SPELL_SLOTS) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setBackgroundResource(0);
            checkBox.setClickable(false);
            mCheckBoxContainer.addView(checkBox, mCheckBoxContainer.getChildCount());
        }

        updateControl();
    }

    /**
     * Clicking on this component will "use up" the next spell slot, showing one of the unused spell
     * slots as used. If all spell slots are used, this will reset to zero used slots.
     */
    @OnClick(R.id.spell_slot_indicator_checkbox_container)
    void onSpellSlotClick() {
        boolean didCheck = false;
        for (int i = 0; i < mCheckBoxContainer.getChildCount(); ++i) {
            CheckBox checkBox = (CheckBox) mCheckBoxContainer.getChildAt(i);
            if (!didCheck && !checkBox.isChecked()) {
                checkBox.setChecked(true);
                didCheck = true;
            }
        }

        if (!didCheck) {
            // If no unchecked boxes were found, reset all
            onSpellSlotLongPress();
        }
    }

    /**
     * A long press on this component will clear any spent spell slots.
     */
    @OnLongClick(R.id.spell_slot_indicator_checkbox_container)
    boolean onSpellSlotLongPress() {
        for (int i = 0; i < mCheckBoxContainer.getChildCount(); ++i) {
            CheckBox checkBox = (CheckBox) mCheckBoxContainer.getChildAt(i);
            checkBox.setChecked(false);
        }
        return true;
    }

    /**
     * Set the spell slots to a specified value. This does not animate and should typically be called
     * when this View is first inflated.
     *
     * @param spellSlots number of spell slots to display
     */
    public void setSpellSlots(int spellSlots) {
    }

    private void updateControl() {
        final int childCount = mCheckBoxContainer.getChildCount();
        if (childCount == 0) {
            // If there are no spell slots left, remove the '-' button.
            mSpellSlotRemove.setVisibility(View.GONE);
        } else {
            mSpellSlotRemove.setVisibility(View.VISIBLE);
        }
    }
}
