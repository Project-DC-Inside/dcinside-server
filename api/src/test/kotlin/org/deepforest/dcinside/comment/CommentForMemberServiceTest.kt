package org.deepforest.dcinside.comment

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.comment.dto.CommentWrittenByMemberDto
import org.deepforest.dcinside.comment.repository.CommentRepository
import org.deepforest.dcinside.comment.repository.findByCommentId
import org.deepforest.dcinside.comment.service.CommentForMemberService
import org.deepforest.dcinside.entity.comment.Comment
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.post.repository.PostRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

@SpringBootTest
@Transactional
class CommentForMemberServiceTest {

    @Autowired
    private lateinit var commentForMemberService: CommentForMemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Test
    @DisplayName("회원이 작성한 댓글 저장")
    fun testSaveCommentByRealMember() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname2", "title", "content", password = null, member, gallery)
        memberRepository.saveAndFlush(member)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        // when
        val dto = CommentWrittenByMemberDto("comment-content")
        val savedCommentId = commentForMemberService.saveComment(dto, post.id!!, member.id!!)

        // then
        val findComment = commentRepository.findByCommentId(savedCommentId)
        assertThat(findComment.member).isEqualTo(member)
        assertThat(findComment.nickname).isEqualTo(member.nickname)
        assertThat(findComment.password).isNull()
        assertThat(findComment.content).isEqualTo(dto.content)
        assertThat(findComment.post).isEqualTo(post)
    }

    @Nested
    @DisplayName("댓글 삭제 관련 성공/실패 테스트")
    inner class DeleteComment {

        @Test
        @DisplayName("댓글 삭제 성공")
        fun deleteSuccess() {
            // given
            val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            val post = Post(null, "nickname", "title", "content", null, member, gallery)
            val comment = Comment(content = "comment-content", post, member)
            memberRepository.saveAndFlush(member)
            galleryRepository.saveAndFlush(gallery)
            postRepository.saveAndFlush(post)
            commentRepository.saveAndFlush(comment)

            // when
            commentForMemberService.deleteComment(comment.id!!, member.id!!)

            // then
            assertThatExceptionOfType(NoSuchElementException::class.java)
                .isThrownBy {
                    commentRepository.findByCommentId(comment.id!!)
                }
                .withMessage("id: ${comment.id} 댓글 정보가 없습니다")
        }

        @Test
        @DisplayName("게시글과 댓글은 매칭되지만 작성자가 일치하지 않아서 실패")
        fun deleteFailWhenMemberIsDifferent() {
            // given
            val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            val otherMember = Member(null, "username-2", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            val post = Post(null, "nickname2", "title", "content", password = null, member, gallery)
            val comment = Comment(content = "before-comment-content", post, member)
            memberRepository.saveAndFlush(member)
            memberRepository.saveAndFlush(otherMember)
            galleryRepository.saveAndFlush(gallery)
            postRepository.saveAndFlush(post)
            commentRepository.saveAndFlush(comment)

            // then
            assertThatExceptionOfType(IllegalStateException::class.java)
                .isThrownBy {
                    commentForMemberService.deleteComment(comment.id!!, otherMember.id!!)
                }.withMessage("댓글 작성자가 일치하지 않습니다. member: ${otherMember.id}, comment.member: ${comment.member?.id}, comment: ${comment.id}")
        }
    }
}
