package com.dev4u.ntc.generalnews.load;

import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.model.Comment;
import com.dev4u.ntc.generalnews.model.Post;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.load
 * Name project: GeneralNews
 * Date: 2/12/2017
 * Time: 16:48
 */

public interface LoadNewsCallBack {
    void updateDataListNews(Post post);

    void updateDataListCa(Category category);

    void updateDataListComment(Comment comment);

    void setRefreshingLayout(boolean b);
}
