package service;

import java.sql.Connection;
import java.util.List;

import dao.Member_tableDAO;
import model.Member;

public class MemberService {
    private final Member_tableDAO memberTableDAO;

    public MemberService(Member_tableDAO memberTableDAO) {
        this.memberTableDAO = memberTableDAO;
    }

    public void manageMembers(Connection conn, int groupId, List<Integer> deletedMemberIds, List<Member> newMembers) throws Exception {
        // 1. 既存メンバーを取得
        List<Member> existingMembers = memberTableDAO.getMembersByArtistGroupId(groupId);
        System.out.println("[manageMembers] Existing members: " + existingMembers);

        // 2. 削除処理
        if (deletedMemberIds != null && !deletedMemberIds.isEmpty()) {
            for (int memberId : deletedMemberIds) {
                boolean exists = existingMembers.stream().anyMatch(member -> member.getId() == memberId);
                if (exists) {
                    memberTableDAO.deleteMemberById(memberId);
                } else {
                    System.out.println("[manageMembers] Member ID not found for deletion: " + memberId);
                }
            }
        }

        // 3. 挿入する新規メンバーをフィルタリング
        List<Member> membersToInsert = newMembers.stream()
            .filter(newMember -> newMember.getId() == 0) // 新規挿入するメンバーのみ
            .filter(newMember -> existingMembers.stream()
                .noneMatch(existingMember -> 
                    existingMember.getMember_name().equals(newMember.getMember_name()) &&
                    existingMember.getMember_position().equals(newMember.getMember_position())
                )
            )
            .distinct() // 重複排除
            .toList();

        System.out.println("[manageMembers] Members to insert: " + membersToInsert);

        // 4. 挿入処理
        if (!membersToInsert.isEmpty()) {
            memberTableDAO.insertMembers(groupId, membersToInsert);
        }
    }
}
