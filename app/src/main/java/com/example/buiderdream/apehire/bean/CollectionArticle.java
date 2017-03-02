package com.example.buiderdream.apehire.bean;

import java.io.Serializable;

/**
 * Created by 51644 on 2017/2/28.
 */

public class CollectionArticle  implements Serializable{

    /**
     * articleId : 4
     * content : xcvzxcvhsdjgfsjfjagfjagjkfhgakjhfgajksdhfgakjhdsgfkjhagfdkjhagsdkfgaskdjhfgakdsgfkajsgdfkjhagdskfgaskdjfhgaskdjhfgakjhdgfkahgdsuf
     * createTime : 12345678911
     * imgUrl : asasas
     * replies : 12
     * title : ssssss
     * type : qqqqq
     * userName : assss
     * vexID : 6
     */

    private int articleId;
    private String content;
    private String createTime;
    private String imgUrl;
    private String replies;
    private String title;
    private String type;
    private String userName;
    private int vexID;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVexID() {
        return vexID;
    }

    public void setVexID(int vexID) {
        this.vexID = vexID;
    }
}
