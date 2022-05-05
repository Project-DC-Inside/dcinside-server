package org.deepforest.dcinside.comment.service

import org.deepforest.dcinside.comment.dto.CommentWrittenByMemberDto
import org.deepforest.dcinside.comment.repository.CommentRepository
import org.deepforest.dcinside.comment.repository.findByCommentId
import org.deepforest.dcinside.entity.comment.Comment
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.member.findByMemberId
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentForMemberService(
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository
) {
    fun saveComment(dto: CommentWrittenByMemberDto, postId: Long, memberId: Long): Long {
        val member = memberRepository.findByMemberId(memberId)
        val post = postRepository.findByPostId(postId)

        val baseComment = dto.baseCommentId
            ?.let { commentRepository.findByCommentId(it) }
            ?.also { baseComment ->
                check(baseComment.writtenIn(post)) {
                    "답글을 작성하려는 게시글이 일치하지 않습니다. baseCommentId: ${baseComment.id}, baseComment.post: ${baseComment.post.id}, post: ${post.id}"
                }
            }

        val comment = dto.toEntity(post, member, baseComment)
        return commentRepository.save(comment).id!!
    }

    fun deleteComment(commentId: Long, memberId: Long) {
        val member = memberRepository.findByMemberId(memberId)
        val comment = commentRepository.findByCommentId(commentId)

        check(comment.writtenBy(member)) {
            "댓글 작성자가 일치하지 않습니다. member: $memberId, comment.member: ${comment.member?.id}, comment: $commentId"
        }

        commentRepository.delete(comment)
    }
}
