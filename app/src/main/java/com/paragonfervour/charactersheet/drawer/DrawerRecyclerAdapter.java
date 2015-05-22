package com.paragonfervour.charactersheet.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.model.CharacterInfo;
import com.paragonfervour.charactersheet.widget.RecycleViewHolder;


public class DrawerRecyclerAdapter extends RecyclerView.Adapter<RecycleViewHolder> {

    private static final int ViewTypeHeader = R.layout.drawer_header_view;

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ViewTypeHeader) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
            return new HeaderViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ViewTypeHeader) {
            HeaderViewHolder header = (HeaderViewHolder)viewHolder;

            // todo: supply this information
            CharacterInfo info = new CharacterInfo();
            info.setLevel(4);
            info.setCharacterClass("Rogue");
            info.setName("Maldalair");

            header.bindToView(info);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ViewTypeHeader;
        }
        else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }


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

}
