package com.paragonfervour.charactersheet.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.OffenseActivity;
import com.paragonfervour.charactersheet.model.CharacterInfo;
import com.paragonfervour.charactersheet.model.GameCharacter;
import com.paragonfervour.charactersheet.widget.RecycleViewHolder;

import java.util.ArrayList;
import java.util.List;


public class DrawerRecyclerAdapter extends RecyclerView.Adapter<RecycleViewHolder> {

    private static final int ViewTypeHeader = R.layout.drawer_header_view;
    private static final int ViewTypeNavTarget = R.layout.drawer_nav_target_view;
    private static final int ViewTypeDivider = 1;

    private Context mContext;
    private GameCharacter mCharacter;
    private List<NavTarget> mNavTargets = new ArrayList<>();
    private Class<? extends Activity> mCurrentTarget;

    public DrawerRecyclerAdapter(GameCharacter character, Activity currentTarget) {
        mContext = currentTarget;
        mCharacter = character;
        mCurrentTarget = currentTarget.getClass();
        initializeTargets();
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ViewTypeHeader) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
            return new HeaderViewHolder(itemView);
        }
        else if (viewType == ViewTypeNavTarget) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
            return new NavTargetViewHolder(itemView);
        }
        else if (viewType == ViewTypeDivider) {
            View itemView = new View(viewGroup.getContext());
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = viewGroup.getResources().getDimensionPixelSize(R.dimen.divider_height);
            itemView.setLayoutParams(params);
            itemView.setBackgroundColor(viewGroup.getContext().getResources().getColor(R.color.default_background_gray));

            return new DividerViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder viewHolder, int position) {
        if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder header = (HeaderViewHolder)viewHolder;
            header.bindToView(mCharacter.getInfo());
        }
        else if (viewHolder instanceof NavTargetViewHolder) {
            NavTargetViewHolder holder = (NavTargetViewHolder)viewHolder;
            NavTarget target = mNavTargets.get(position - 1);
            holder.bindToView(target, mCurrentTarget.equals(target.mTarget));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ViewTypeHeader;
        }
        else if(position <= mNavTargets.size()) {
            return ViewTypeNavTarget;
        }
        else {
            return ViewTypeDivider;
        }
    }

    @Override
    public int getItemCount() {
        // HEADER + navTargets + DIVIDER
        return 2 + mNavTargets.size();
    }

    private void initializeTargets() {
        mNavTargets.add(new NavTarget(R.drawable.abc_ic_menu_cut_mtrl_alpha, mContext.getString(R.string.nav_offense_activity), OffenseActivity.class));
    }

    private class NavTarget {
        private int mTargetIcon;
        private String mTargetTitle;
        private Class<? extends Activity> mTarget;

        public NavTarget(int targetIcon, String targetTitle, Class<? extends Activity> target) {
            mTargetIcon = targetIcon;
            mTargetTitle = targetTitle;
            mTarget = target;
        }
    }

    private class NavTargetClickListener implements View.OnClickListener {
        private NavTarget mTarget;
        public NavTargetClickListener(NavTarget target) {
            mTarget = target;
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(mContext, mTarget.mTarget);
            mContext.startActivity(i);
        }
    }

    // region ViewHolders

    private class HeaderViewHolder extends RecycleViewHolder {
        private TextView mClassDescription;
        private TextView mCharacterName;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            mClassDescription = (TextView)itemView.findViewById(R.id.drawer_header_class_desc);
            mCharacterName = (TextView)itemView.findViewById(R.id.drawer_header_character_name);
        }

        public void bindToView(CharacterInfo info) {
            mClassDescription.setText(String.format(itemView.getContext().getString(R.string.character_info_class_format), info.getLevel(), info.getCharacterClass()));
            mCharacterName.setText(info.getName());
        }

        public void recycle() {
            mClassDescription.setText(null);
            mCharacterName.setText(null);
        }
    }

    private class DividerViewHolder extends RecycleViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
        }
        @Override
        public void recycle() { }
    }

    private class NavTargetViewHolder extends RecycleViewHolder {
        private ImageView mIcon;
        private TextView mText;
        public NavTargetViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView)itemView.findViewById(R.id.drawer_nav_target_icon);
            mText = (TextView) itemView.findViewById(R.id.drawer_nav_target_text);
        }

        public void bindToView(NavTarget target, boolean isCurrent) {
            mIcon.setImageResource(target.mTargetIcon);
            mText.setText(target.mTargetTitle);
            if (isCurrent) {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.default_background_gray));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Close drawer. Might have to forward these click events into a
                        // TODO: listener in the fragment in order to get this to work cleanly.
                    }
                });
            }
            else {
                // Only allow actual navigation for pages we aren't currently on.
                itemView.setOnClickListener(new NavTargetClickListener(target));
            }
        }

        @Override
        public void recycle() {
            itemView.setBackgroundResource(0);
            itemView.setOnClickListener(null);
        }
    }

    // endregion

}
