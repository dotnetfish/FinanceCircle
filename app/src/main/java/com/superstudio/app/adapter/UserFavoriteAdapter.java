package com.superstudio.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superstudio.app.R;
import com.superstudio.app.base.ListBaseAdapter;
import com.superstudio.app.bean.Favorite;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserFavoriteAdapter extends ListBaseAdapter<Favorite> {

    static class ViewHolder {

        @Bind(R.id.tv_favorite_title)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_favorite, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Favorite favorite = (Favorite) mDatas.get(position);

        vh.title.setText(favorite.getTitle());
        return convertView;
    }

}
