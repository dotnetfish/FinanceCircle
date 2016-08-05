package com.superstudio.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.base.ListBaseAdapter;
import com.superstudio.app.bean.Blog;
import com.superstudio.app.bean.BlogList;
import com.superstudio.app.util.StringUtils;
import com.superstudio.app.util.ThemeSwitchUtils;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * @author HuangWenwei
 * 
 * @date 2014年9月29日
 */
public class BlogAdapter extends ListBaseAdapter<Blog> {

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

        Blog blog = mDatas.get(position);

        vh.tip.setVisibility(View.VISIBLE);
        if (blog.getDocumenttype() == Blog.DOC_TYPE_ORIGINAL) {
            vh.tip.setImageResource(R.drawable.widget_original_icon);
        } else {
            vh.tip.setImageResource(R.drawable.widget_repaste_icon);
        }

        vh.title.setText(blog.getTitle());

        if (AppContext.isOnReadedPostList(BlogList.PREF_READED_BLOG_LIST,
                blog.getId() + "")) {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        vh.description.setVisibility(View.GONE);
        String description = blog.getBody();
        if (null != description && !StringUtils.isEmpty(description)) {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(description.trim());
        }

        vh.source.setText(blog.getAuthor());
        vh.time.setText(StringUtils.friendly_time(blog.getPubDate()));
        vh.comment_count.setText(blog.getCommentCount() + "");
        return convertView;
    }
}
