package org.qii.weiciyuan.ui.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import org.qii.weiciyuan.bean.CommentListBean;
import org.qii.weiciyuan.bean.android.AsyncTaskLoaderResult;
import org.qii.weiciyuan.dao.maintimeline.MentionsCommentTimeLineDao;
import org.qii.weiciyuan.support.error.WeiboException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: qii
 * Date: 13-4-18
 */
public class MentionsCommentMsgLoader extends AsyncTaskLoader<AsyncTaskLoaderResult<CommentListBean>> {

    private static Lock lock = new ReentrantLock();

    private String token;
    private String sinceId;
    private String maxId;
    private String accountId;

    public MentionsCommentMsgLoader(Context context, String accountId, String token, String sinceId, String maxId) {
        super(context);
        this.token = token;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.accountId = accountId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public AsyncTaskLoaderResult<CommentListBean> loadInBackground() {
        MentionsCommentTimeLineDao dao = new MentionsCommentTimeLineDao(token);
        dao.setSince_id(sinceId);
        dao.setMax_id(maxId);
        CommentListBean result = null;
        WeiboException exception = null;
        lock.lock();

        try {
            result = dao.getGSONMsgList();
        } catch (WeiboException e) {
            exception = e;
        } finally {
            lock.unlock();
        }

        AsyncTaskLoaderResult<CommentListBean> data = new AsyncTaskLoaderResult<CommentListBean>();
        data.data = result;
        data.exception = exception;
        return data;
    }

}
