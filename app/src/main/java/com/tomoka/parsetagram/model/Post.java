package com.tomoka.parsetagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_FAVCOUNT = "favcount";
    private static final String KEY_PROFILE_PIC = "profilepicture";
    private static final String KEY_CREATED_AT = "createdAt";

    //private static final String KEY_OBJECTID = "objectId";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseFile getUserProfileImage() {
        return getUser().getParseFile(KEY_PROFILE_PIC);
    }

    //public Date getCreatedAt() {return getDate(KEY_CREATED_AT);}

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public int getFavcount() {
        return getInt(KEY_FAVCOUNT);
    }

    public void setFavcount(int i) {
        put(KEY_FAVCOUNT, i);
    }

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}
