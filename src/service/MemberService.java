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

    public void manageMembers(Connection conn, int groupId, List<Integer> deletedMemberIds, List<Member> newMembers, List<Integer> existingMemberIds) throws Exception {
        List<Member> existingMembers = memberTableDAO.getMembersByArtistGroupId(groupId);

        // **削除処理**
        if (deletedMemberIds != null && !deletedMemberIds.isEmpty()) {
            for (int memberId : deletedMemberIds) {
                boolean exists = existingMembers.stream().anyMatch(member -> member.getId() == memberId);
                if (exists) {
                    memberTableDAO.deleteMemberById(memberId);
                }
            }
            // 削除後のメンバーリストを更新
            existingMembers = memberTableDAO.getMembersByArtistGroupId(groupId);
        }

        // **既存メンバーの更新**
        for (Member existingMember : existingMembers) {
            for (Member newMember : newMembers) {
                if (existingMember.getId() == newMember.getId() && newMember.getId() > 0) {
                    if (!existingMember.getMember_name().equals(newMember.getMember_name()) || 
                        !existingMember.getMember_position().equals(newMember.getMember_position())) {

                        System.out.println("[manageMembers] Updating member ID: " + newMember.getId());
                        memberTableDAO.updateMember(newMember);  // 更新処理
                    }
                }
                System.out.println("[manageMembers] Checking existing member: " + existingMember.getId() + " - " + existingMember.getMember_name());
                System.out.println("[manageMembers] Checking new member: " + newMember.getId() + " - " + newMember.getMember_name());
            }
        }

        // **新規メンバーの追加**
        List<Member> membersToInsert = newMembers.stream()
            .filter(newMember -> newMember.getId() == 0) // ID 0 のメンバーのみ追加
            .distinct()
            .toList();

        if (!membersToInsert.isEmpty()) {
            memberTableDAO.insertMembers(groupId, membersToInsert);
        }
    }
}