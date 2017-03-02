package com.example.buiderdream.apehire.entity;

import java.io.Serializable;

/**
 * Created by Admin on 2017/2/27.
 */

public class Technology implements Serializable {

    /**
     * id : 343624
     * title : 没有绑卡、没有任何消费记录的 google play 帐号如何设置地区和货币？？
     * url : http://www.v2ex.com/t/343624
     * content : 账户一直是以一个香港 IP 登陆的，手机上打开 google 页面会给我自动跑到 google.com.ph ， play 里面要花钱的 app 都是 PHP 的价格，看样子是给我定位到菲律宾去了，不喜欢这样，希望能定位在美国，最起码价钱要以美元结算，请问怎么设置呢？？
     * content_rendered : <p>账户一直是以一个香港 IP 登陆的，手机上打开 google 页面会给我自动跑到 <a href="http://google.com.ph" rel="nofollow">google.com.ph</a> ， play 里面要花钱的 app 都是 PHP 的价格，看样子是给我定位到菲律宾去了，不喜欢这样，希望能定位在美国，最起码价钱要以美元结算，请问怎么设置呢？？</p>

     * replies : 0
     * member : {"id":167965,"username":"qceytzn","tagline":"None","avatar_mini":"//v2ex.assets.uxengine.net/gravatar/0cebe45672ac5665db79c823ecaf1bab?s=24&d=retro","avatar_normal":"//v2ex.assets.uxengine.net/gravatar/0cebe45672ac5665db79c823ecaf1bab?s=48&d=retro","avatar_large":"//v2ex.assets.uxengine.net/gravatar/0cebe45672ac5665db79c823ecaf1bab?s=73&d=retro"}
     * node : {"id":39,"name":"android","title":"Android","title_alternative":"Android","url":"http://www.v2ex.com/go/android","topics":3655,"avatar_mini":"//v2ex.assets.uxengine.net/navatar/d67d/8ab4/39_mini.png?m=1484191305","avatar_normal":"//v2ex.assets.uxengine.net/navatar/d67d/8ab4/39_normal.png?m=1484191305","avatar_large":"//v2ex.assets.uxengine.net/navatar/d67d/8ab4/39_large.png?m=1484191305"}
     * created : 1488197217
     * last_modified : 1488197217
     * last_touched : 1488182637
     */

    private int id;
    private String title;
    private String url;
    private String content;
    private String content_rendered;
    private int replies;
    /**
     * id : 167965
     * username : qceytzn
     * tagline : None
     * avatar_mini : //v2ex.assets.uxengine.net/gravatar/0cebe45672ac5665db79c823ecaf1bab?s=24&d=retro
     * avatar_normal : //v2ex.assets.uxengine.net/gravatar/0cebe45672ac5665db79c823ecaf1bab?s=48&d=retro
     * avatar_large : //v2ex.assets.uxengine.net/gravatar/0cebe45672ac5665db79c823ecaf1bab?s=73&d=retro
     */

    private MemberBean member;
    /**
     * id : 39
     * name : android
     * title : Android
     * title_alternative : Android
     * url : http://www.v2ex.com/go/android
     * topics : 3655
     * avatar_mini : //v2ex.assets.uxengine.net/navatar/d67d/8ab4/39_mini.png?m=1484191305
     * avatar_normal : //v2ex.assets.uxengine.net/navatar/d67d/8ab4/39_normal.png?m=1484191305
     * avatar_large : //v2ex.assets.uxengine.net/navatar/d67d/8ab4/39_large.png?m=1484191305
     */

    private NodeBean node ;
    private int created;
    private int last_modified;
    private int last_touched;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_rendered() {
        return content_rendered;
    }

    public void setContent_rendered(String content_rendered) {
        this.content_rendered = content_rendered;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public NodeBean getNode() {
        return node;
    }

    public void setNode(NodeBean node) {
        this.node = node;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(int last_modified) {
        this.last_modified = last_modified;
    }

    public int getLast_touched() {
        return last_touched;
    }

    public void setLast_touched(int last_touched) {
        this.last_touched = last_touched;
    }

    public static class MemberBean implements  Serializable{
        private int id;
        private String username;
        private String tagline;
        private String avatar_mini;
        private String avatar_normal;
        private String avatar_large;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getAvatar_normal() {
            return avatar_normal;
        }

        public void setAvatar_normal(String avatar_normal) {
            this.avatar_normal = avatar_normal;
        }

        public String getAvatar_large() {
            return avatar_large;
        }

        public void setAvatar_large(String avatar_large) {
            this.avatar_large = avatar_large;
        }
    }

    public static class NodeBean implements  Serializable {
        private int id;
        private String name;
        private String title;
        private String title_alternative;
        private String url;
        private int topics;
        private String avatar_mini;
        private String avatar_normal;
        private String avatar_large;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle_alternative() {
            return title_alternative;
        }

        public void setTitle_alternative(String title_alternative) {
            this.title_alternative = title_alternative;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getTopics() {
            return topics;
        }

        public void setTopics(int topics) {
            this.topics = topics;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getAvatar_normal() {
            return avatar_normal;
        }

        public void setAvatar_normal(String avatar_normal) {
            this.avatar_normal = avatar_normal;
        }

        public String getAvatar_large() {
            return avatar_large;
        }

        public void setAvatar_large(String avatar_large) {
            this.avatar_large = avatar_large;
        }
    }
}
