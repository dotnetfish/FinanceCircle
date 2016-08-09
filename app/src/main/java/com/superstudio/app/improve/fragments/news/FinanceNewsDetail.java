package com.superstudio.app.improve.fragments.news;

/**
 * Created by T440P on 2016-8-8.
 */
public class FinanceNewsDetail {

    private  String title;
   private String catname;
   private String commentnum;

           private String from;
            private String content;

                    private String date;

    private  String webaddr;

    public String getWebaddr(){
        return  this.webaddr;
    }

    public  void  setWebaddr(String value){
        this.webaddr=value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
