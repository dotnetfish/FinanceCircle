package com.superstudio.app.improve.detail.contract;

import com.superstudio.app.bean.Comment;
import com.superstudio.app.bean.Tweet;
import com.superstudio.app.bean.User;

/**
 * Created by thanatosx
 * on 16/5/28.
 */

public interface TweetDetailContract {

    interface Operator {

        Tweet getTweetDetail();

        void toReply(Comment comment);

        void onScroll();
    }

    interface ICmnView {
        void onCommentSuccess(Comment comment);
    }

    interface IThumbupView {
        void onLikeSuccess(boolean isUp, User user);
    }

    interface IAgencyView {
        void resetLikeCount(int count);

        void resetCmnCount(int count);
    }

}
