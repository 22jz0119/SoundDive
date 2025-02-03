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

        // 削除処理
        if (deletedMemberIds != null && !deletedMemberIds.isEmpty()) {
            for (int memberId : deletedMemberIds) {
                boolean exists = existingMembers.stream().anyMatch(member -> member.getId() == memberId);
                if (exists) {
                    memberTableDAO.deleteMemberById(memberId);
                }
            }
        }

        // 既存メンバーの更新
        for (Member existingMember : existingMembers) {
            for (Member newMember : newMembers) {
                if (existingMember.getId() == newMember.getId() && newMember.getId() > 0) {
                    if (!existingMember.getMember_name().equals(newMember.getMember_name()) || 
                        !existingMember.getMember_position().equals(newMember.getMember_position())) {

                        System.out.println("[manageMembers] Updating member ID: " + newMember.getId());
                        memberTableDAO.updateMember(newMember);
                    }
                }
            }
        }

        // 新規メンバーの追加
        List<Member> membersToInsert = newMembers.stream()
            .filter(newMember -> newMember.getId() == 0 && !existingMemberIds.contains(newMember.getId()))
            .distinct()
            .toList();

        if (!membersToInsert.isEmpty()) {
            memberTableDAO.insertMembers(groupId, membersToInsert);
        }
    }
}