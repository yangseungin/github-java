import org.kohsuke.github.*;

import java.io.IOException;
import java.util.*;

public class DashboardApp {
    public static void main(String[] args) throws IOException {
//        깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크 할 것.
//        참여율을 계산하세요. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여줄 것.

        String repoName = "whiteship/live-study";
        String oauthToken = "input your token"; // personal token을 발급하여 사용함
        GitHub gitHub = new GitHubBuilder().withOAuthToken(oauthToken).build();
        GHRepository repository = gitHub.getRepository(repoName);   //repo 가져오기

        List<GHIssue> issues = repository.getIssues(GHIssueState.ALL);  // 모든 이슈 가져오기
        StringBuilder sb = new StringBuilder();

        Map<String, Integer> info = new HashMap<>();
        for (GHIssue issue : issues) {
//            시작한이슈만
            if (!startCheck(issue.getLabels()))
                continue;

            Set<String> idSet = new HashSet<>();
            //이슈별 회원 저장
            for (GHIssueComment comment : issue.getComments())
                idSet.add(comment.getUser().getLogin());

            //이슈별 comment한 계정 출석일 추가
            for (String userId : idSet)
                info.put(userId, info.getOrDefault(userId, 0) + 1);

        }
        for (String userId : info.keySet())
            sb.append("계졍: " + userId + " / 총참여횟수: " + info.get(userId) + " / 참석율: " + Math.round(((double) info.get(userId) / issues.size() * 10000)) / 100.0).append("\n");
        sb.append("총 참여자: " + info.size());
        System.out.println(sb);
    }

    private static boolean startCheck(Collection<GHLabel> labels) {
        for (GHLabel label : labels) {
            if (label.getName().equals("draft"))
                return false;

        }
        return true;
    }
}
