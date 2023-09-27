package test.article.controller;

import test.article.model.*;
import test.article.view.ArticleView;
import test.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class ArticleController {
    private ArticleRepository articleRepository = new ArticleRepository();
    private ArticleView articleView = new ArticleView();
    private ReplyRepository replyRepository = new ReplyRepository();
    private MemberRepository memberRepository = new MemberRepository();
    private LikeRepository likeRepository = new LikeRepository();
    private Scanner scan = new Scanner(System.in);
    private Member loginedMember = null;


    public void add() {
        if (isNotLogin()) return;
        System.out.print("추가할 게시물 제목 입력 : ");
        String title = scan.nextLine();
        System.out.print("추가할 게시물 내용 입력 : ");
        String content = scan.nextLine();


        articleRepository.insert(title, content, loginedMember.getId());

        System.out.println("게시물 등록 완료");
    }

    public void list() {
        ArrayList<Article> articles = articleRepository.findAllArticles();
        articleView.printArticles(articles);
    }

    public void update() {
        System.out.print("수정할 게시물 번호 입력 : ");
        int targetId = getParamInt(scan.nextLine(), -1);
        if (targetId == -1) {
            return;
        }

        Article article = articleRepository.findById(targetId);

        if (article == null) {
            System.out.println("없는 게시물 번호 입니다.");
        } else {
            System.out.print("수정할 게시물 제목 :");
            String newTitle = scan.nextLine();
            System.out.print("수정할 게시물 제목 :");
            String newContent = scan.nextLine();

            article.setTitle(newTitle);
            article.setContent(newContent);
            System.out.println("수정 완료");
        }
    }

    public void delete() {
        System.out.print("삭제할 게시물 번호 입력 : ");
        int targetId = getParamInt(scan.nextLine(), -1);
        if (targetId == -1) {
            return;
        }
        Article article = articleRepository.findById(targetId);

        if (article == null) {
            System.out.println("없는 게시물 번호입니다");
        } else {
            articleRepository.delete(article);
        }
    }

    public void detail() {

        if (isNotLogin()) return;

        System.out.print("상세보기할 게시물 번호 입력 : ");
        int targetId = getParamInt(scan.nextLine(), -1);
        if (targetId == -1) {
            return;
        }
        Article article = articleRepository.findById(targetId);

        if (article == null) {
            System.out.println("없는 게시물 번호입니다");
        } else {
            article.setHit(article.getHit() + 1);
            ArrayList<Reply> replies = replyRepository.getRepliesByArticleId(article.getId());
            Member member = memberRepository.getMemberById(article.getMemberId());
            Like like = likeRepository.getLikeByArticleIdAndMemberId(article.getId(), loginedMember.getId());
            int likeCount = likeRepository.getCountOfLikeByArticleId(article.getId());

            articleView.printArticleDetail(article, member, replies, likeCount, like);
            doDetailProccess(article, member, replies);
        }
    }

    public void doDetailProccess(Article article, Member member, ArrayList<Reply> replies) {
        while (true) {
            System.out.print("상세보기 기능을 선택해주세요(1. 댓글 등록, 2. 추천, 3. 수정, 4. 삭제, 5. 목록으로) : ");
            int cmd = getParamInt(scan.nextLine(), -1);

            switch (cmd) {
                case 1:
                    addReply(article, member);
                    break;
                case 2:
                    checkLike(article, member, replies);
                    break;
                case 3:
                    updateMyArticle(article, member, replies);
                    break;
                case 4:
                    deleteMyArticle(article);
                    break;
                case 5:
                    System.out.println("목록으로");
                    break;
            }
            if (cmd == 5) {
                break;
            }
        }
    }

    private void checkLike(Article article, Member member, ArrayList<Reply> replies) {
        // 하나의 게시물에 한명의 유저가 체크 가능 -> 어떤 게시물에 어떤 회원이 좋아요 체크 했는지 기억해야 한다.
        // 좋아요 => 어떤 게시물, 어떤 회원, 언제
        // 좋아요 여러개 -> 1번 게시물에 1번 유저, 1번 게시물에 2번유저, 1번 게시물에 3번 유저, 2번 게시물에 1번 유저...
        if (isNotLogin()) return;

        Like like = likeRepository.getLikeByArticleIdAndMemberId(article.getId(), loginedMember.getId());

        if (like == null) {
            likeRepository.insert(article.getId(), loginedMember.getId());
            System.out.println("해당 게시물을 좋아합니다.");
        } else {
            likeRepository.delete(like);
            System.out.println("해당 게시물의 좋아요를 해제합니다.");

        }
        int likeCount = likeRepository.getCountOfLikeByArticleId(article.getId());
        like = likeRepository.getLikeByArticleIdAndMemberId(article.getId(), loginedMember.getId());
        articleView.printArticleDetail(article, member, replies, likeCount, like);

    }

    private void deleteMyArticle(Article article) {
        if (isNotLogin()) return;
        System.out.print("정말 게시물을 삭제하시겠습니까? (y/n) : ");
        String isAgree = scan.nextLine();
        if (isAgree.equals("y")) {
            articleRepository.delete(article);
            System.out.printf("홍길동님의 %d번 게시물을 삭제했습니다.\n", article.getId());
            list();
        }
    }

    private void updateMyArticle(Article article, Member member, ArrayList<Reply> replies) {
        if (isNotLogin()) return;
        System.out.println("새로운 제목 : ");
        String title = scan.nextLine();
        System.out.println("새로운 내용 : ");
        String body = scan.nextLine();

        articleRepository.update(article.getId(), title, body);
        Like like = likeRepository.getLikeByArticleIdAndMemberId(article.getId(), loginedMember.getId());
        int likeCount = likeRepository.getCountOfLikeByArticleId(article.getId());
        articleView.printArticleDetail(article, member, replies, likeCount, like);
    }

    public void addReply(Article article, Member member) {
        if (isNotLogin()) return;
        System.out.print("댓글 내용 : ");
        String body = scan.nextLine();
        String regDate = Util.getCurrentDate();
        int articleId = article.getId();

        replyRepository.insert(articleId, body, regDate);


        System.out.println("댓글이 성공적으로 등록되었습니다.");
        ArrayList<Reply> replies = replyRepository.getRepliesByArticleId(article.getId());
        Like like = likeRepository.getLikeByArticleIdAndMemberId(article.getId(), loginedMember.getId());
        int likeCount = likeRepository.getCountOfLikeByArticleId(article.getId());
        articleView.printArticleDetail(article, member, replies, likeCount, like);

    }


    public void search() {
        System.out.print("검색 키워드를 입력하세요 : ");
        String keyword = scan.nextLine();
        ArrayList<Article> searchedArticles = articleRepository.findByTitle(keyword);
        articleView.printArticles(searchedArticles);
    }

    public boolean isNotLogin() {
        if (loginedMember == null) {
            System.out.println("로그인을 해주세요.");
            return true;
        }
        return false;
    }


    public int getParamInt(String input, int defaulValue) {
        try {
            int num = Integer.parseInt(input);
            return num;
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력 가능합니다.");
        }
        return defaulValue;
    }

    public Member getLoginedMember() {
        return loginedMember;
    }

    public void setLoginedMember(Member loginedMember) {
        this.loginedMember = loginedMember;
    }

    public void sort() {
        System.out.println("정렬 대상을 선택해주세요. (1. 번호, 2. 조회수) : ");
        int sortTarget = getParamInt(scan.nextLine(), -1);
        System.out.println("정렬 방법을 선택해주세요. (1. 오름차순, 2. 내림차순) :");
        int sortType = getParamInt(scan.nextLine(), -1);
        ArrayList<Article> allArticles = articleRepository.findAllArticles();
        Collections.sort(allArticles, new ArticleComparator());
        articleView.printArticles(allArticles);

    }
}

class ArticleComparator implements Comparator<Article> {

    @Override
    public int compare(Article o1, Article o2) {
        if (o1.getId() > o2.getId()) {
            return 1;
        }
        return -1;
    }
}