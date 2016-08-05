package com.superstudio.app.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.base.ListBaseAdapter;
import com.superstudio.app.bean.News;
import com.superstudio.app.bean.NewsList;
import com.superstudio.app.util.StringUtils;
import com.superstudio.app.util.ThemeSwitchUtils;

import butterknife.ButterKnife;
import butterknife.Bind;

public class NewsAdapter extends ListBaseAdapter<News> {

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_news, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        News news = mDatas.get(position);
        vh.title.setText(news.getTitle());

        if (AppContext.isOnReadedPostList(NewsList.PREF_READED_NEWS_LIST,
                news.getId() + "")) {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        String description = news.getBody();
        vh.description.setVisibility(View.GONE);
        if (description != null && !StringUtils.isEmpty(description)) {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(description.trim());
        }

        vh.source.setText(news.getAuthor());
        if (StringUtils.isToday(news.getPubDate())) {
            vh.tip.setVisibility(View.VISIBLE);
        } else {
            vh.tip.setVisibility(View.GONE);
        }
        vh.time.setText(StringUtils.friendly_time(news.getPubDate()));
        vh.comment_count.setText(news.getCommentCount() + "");

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.tv_description)
        TextView description;
        @Bind(R.id.tv_source)
        TextView source;
        @Bind(R.id.tv_time)
        TextView time;
        @Bind(R.id.tv_comment_count)
        TextView comment_count;
        @Bind(R.id.iv_tip)
        ImageView tip;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
