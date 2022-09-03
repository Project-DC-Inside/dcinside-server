package org.deepforest.dcinside.comment

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.comment.repository.CommentRepository
import org.deepforest.dcinside.comment.repository.findByPostWithPaging
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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("회원의 댓글 생성/조회 성공")
    fun testSave() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        val gallery = Gallery(null, GalleryType.MINOR, "galleryName")
        val post = Post(null, "nickname2", "title", "content", "password", member, gallery)
        memberRepository.saveAndFlush(member)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        // when
        val comment = Comment(content = "comment-content", post, member)
        commentRepository.saveAndFlush(comment)

        // then
        val findComments = commentRepository.findAll()
        assertThat(findComments.size).isEqualTo(1)

        val findComment = findComments.first()
        assertThat(findComment).isEqualTo(comment)
        assertThat(findComment.id).isEqualTo(comment.id)
        assertThat(findComment.member).isEqualTo(member)
    }

    @Test
    @DisplayName("비회원의 댓글 생성/조회 성공")
    fun testSaveByNonMember() {
        // given
        val gallery = Gallery(null, GalleryType.MINOR, "galleryName")
        val post = Post(null, "nickname2", "title", "content", "password", member = null, gallery)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        // when
        val comment = Comment(content = "comment-content", post = post, nickname = "non-member-nickname", password = "comment-password")
        commentRepository.saveAndFlush(comment)

        // then
        val findComments = commentRepository.findAll()
        assertThat(findComments.size).isEqualTo(1)

        val findComment = findComments.first()
        assertThat(findComment).isEqualTo(comment)
        assertThat(findComment.id).isEqualTo(comment.id)
        assertThat(findComment.member).isNull()
        assertThat(findComment.password).isEqualTo(comment.password)
    }

    @Test
    @DisplayName("댓글 20개 가져오기 테스트")
    fun testFindTop20() {
        // given
        val gallery = Gallery(null, GalleryType.MINOR, "galleryName")
        val post = Post(null, "nickname2", "title", "content", "password", member = null, gallery)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        repeat (30) {
            val comment = Comment(content = "comment-content-$it", post = post, nickname = "non-member-nickname-$it", password = "comment-password-$it")
            commentRepository.saveAndFlush(comment)
        }

        // when
        val findComments = commentRepository.findByPostWithPaging(post)

        // then
        assertThat(findComments.size).isEqualTo(20)
    }
}
