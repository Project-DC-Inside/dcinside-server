package org.deepforest.dcinside.comment

import org.assertj.core.api.Assertions.assertThat
import org.deepforest.dcinside.IntegrationTest
import org.deepforest.dcinside.comment.dto.CommentResponseDto
import org.deepforest.dcinside.comment.repository.CommentRepository
import org.deepforest.dcinside.comment.service.CommentReadService
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
import org.junit.jupiter.api.Test

@IntegrationTest
class CommentReadServiceTest(
    private val commentReadService: CommentReadService,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val galleryRepository: GalleryRepository,
) {
    @Test
    @DisplayName("특정 게시글의 댓글 리스트 가져오기. 오래된 순으로 20개 전달")
    fun testFindList() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname", "title", "content", null, member, gallery)
        memberRepository.saveAndFlush(member)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        repeat(30) {
            val comment = Comment(content = "comment-content-$it", post, member)
            commentRepository.saveAndFlush(comment)
        }

        // when
        val commentDtos: List<CommentResponseDto> = commentReadService.findCommentsByPostId(post.id!!)

        // then
        assertThat(commentDtos.size).isEqualTo(20)
        commentDtos.forEachIndexed { index, dto ->
            assertThat(dto.content).isEqualTo("comment-content-$index")
        }
    }

    @Test
    @DisplayName("게시글의 답글까지 조회")
    fun testFindNestedList() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname", "title", "content", null, member, gallery)
        memberRepository.saveAndFlush(member)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        repeat(30) {
            val comment = Comment(content = "comment-content-$it", post, member)
            commentRepository.saveAndFlush(comment)
            val nestedComment = Comment(content = "nested-content-$it", post, member, comment)
            commentRepository.saveAndFlush(nestedComment)
        }

        // when
        val commentDtos: List<CommentResponseDto> = commentReadService.findCommentsByPostId(post.id!!)

        // then
        assertThat(commentDtos.size).isEqualTo(20)
        commentDtos.forEachIndexed { index, dto ->
            assertThat(dto.content).isEqualTo("comment-content-$index")
            assertThat(dto.replies).isNotEmpty
        }
    }

    @Test
    @DisplayName("회원이 작성한 댓글은 writer.username 존재")
    fun testFindCommentByRealMember() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname", "title", "content", null, member, gallery)
        memberRepository.saveAndFlush(member)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        repeat(30) {
            val comment = Comment(content = "comment-content-$it", post, member)
            commentRepository.saveAndFlush(comment)
        }

        // when
        val commentDtos: List<CommentResponseDto> = commentReadService.findCommentsByPostId(post.id!!)

        // then
        assertThat(commentDtos.size).isEqualTo(20)
        commentDtos.forEachIndexed { index, dto ->
            assertThat(dto.content).isEqualTo("comment-content-$index")
            assertThat(dto.writer.username).isEqualTo(member.username)
        }
    }

    @Test
    @DisplayName("비회원이 작성한 댓글은 writer.username 없음")
    fun testFindCommentByNonMember() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val post = Post(null, "nickname", "title", "content", "password", member = null, gallery)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        repeat(30) {
            val comment = Comment(
                content = "comment-content-$it",
                nickname = "comment-nickname-$it",
                password = "comment-password-$it",
                post = post
            )
            commentRepository.saveAndFlush(comment)
        }

        // when
        val commentDtos: List<CommentResponseDto> = commentReadService.findCommentsByPostId(post.id!!)

        // then
        assertThat(commentDtos.size).isEqualTo(20)
        commentDtos.forEachIndexed { index, dto ->
            assertThat(dto.content).isEqualTo("comment-content-$index")
            assertThat(dto.writer.username).isNull()
            assertThat(dto.writer.nickname).isEqualTo("comment-nickname-$index")
        }
    }
}
