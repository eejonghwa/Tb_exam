package test.article.model;

public class Like {
    private  int articleId;
    private int memberId;
    private String regDate;

    public Like(int articleId, int memberId, String regDate) {
        this.articleId = articleId;
        this.memberId = memberId;
        this.regDate = regDate;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
