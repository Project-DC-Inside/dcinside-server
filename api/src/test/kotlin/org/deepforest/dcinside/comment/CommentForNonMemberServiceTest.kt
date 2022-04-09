package org.deepforest.dcinside.comment

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.comment.dto.CommentAccessDto
import org.deepforest.dcinside.comment.dto.CommentWrittenByNonMemberDto
import org.deepforest.dcinside.comment.repository.CommentRepository
import org.deepforest.dcinside.comment.repository.findByCommentId
import org.deepforest.dcinside.comment.service.CommentForNonMemberService
import org.deepforest.dcinside.entity.comment.Comment
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.post.repository.PostRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class CommentForNonMemberServiceTest {

    @Autowired
    private lateinit var commentForNonMemberService: CommentForNonMemberService

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("비회원이 작성한 댓글 저장")
    fun testSaveComment() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname2", "title", "content", "password", member = null, gallery)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        // when
        val dto = CommentWrittenByNonMemberDto("comment-content", "c-nickname", "c-password")
        val savedCommentId = commentForNonMemberService.saveComment(dto, post.id!!)

        // then
        val findComment = commentRepository.findByCommentId(savedCommentId)
        assertThat(findComment.member).isNull()
        assertThat(findComment.nickname).isEqualTo(dto.nickname)
        assertThat(findComment.password).isEqualTo(dto.password)
        assertThat(findComment.post).isEqualTo(post)
    }

    @Test
    @DisplayName("비회원 댓글 삭제")
    fun testDeleteComment() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname2", "title", "content", "password", member = null, gallery)
        val comment = Comment(content = "comment-content", post, "comment-nickname", "comment-password")
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)
        commentRepository.saveAndFlush(comment)

        // when
        commentForNonMemberService.deleteComment(comment.id!!)

        // then
        assertThatExceptionOfType(NoSuchElementException::class.java)
            .isThrownBy {
                commentRepository.findByCommentId(comment.id!!)
            }
            .withMessage("id: ${comment.id} 댓글 정보가 없습니다")
    }

    @Test
    @DisplayName("비회원 댓글 비밀번호 확인 테스트")
    fun testCheckAccess() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname2", "title", "content", "password", member = null, gallery)
        val comment = Comment(content = "comment-content", post, "comment-nickname", "comment-password")
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)
        commentRepository.saveAndFlush(comment)

        // when
        val canAccessWithSamePassword = commentForNonMemberService.checkAccess(CommentAccessDto(comment.id!!, "comment-password"))
        val canAccessWithDifferentPassword = commentForNonMemberService.checkAccess(CommentAccessDto(comment.id!!, "comment-password2"))

        // then
        assertThat(canAccessWithSamePassword).isTrue
        assertThat(canAccessWithDifferentPassword).isFalse
    }
}
