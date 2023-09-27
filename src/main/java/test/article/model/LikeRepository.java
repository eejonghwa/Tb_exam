package test.article.model;

import test.util.Util;

import java.util.ArrayList;

public class LikeRepository {
    private ArrayList<Like> likes = new ArrayList<>();

    public void insert(int articleId, int memberId) {
        Like like = new Like(articleId, memberId, Util.getCurrentDate());
        likes.add(like);
    }

    public Like getLikeByArticleIdAndMemberId(int articleId, int memberId) {
        for (Like like : likes) {
            if (like.getArticleId() == articleId && like.getMemberId() == memberId) {
                return like;
            }
        }
        return null;
    }

    public void delete(Like like) {
        likes.remove(like);
    }

    public int getCountOfLikeByArticleId(int articleId){
        int count = 0;
        for(Like like : likes){
            if(like.getArticleId() == articleId){
                count++;
            }
        }
        return count;
    }
}
