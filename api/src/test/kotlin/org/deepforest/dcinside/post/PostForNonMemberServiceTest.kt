package org.deepforest.dcinside.post

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.post.dto.PostAccessDto
import org.deepforest.dcinside.post.dto.PostUpdateDto
import org.deepforest.dcinside.post.dto.PostWrittenByNonMemberDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.deepforest.dcinside.post.service.PostForNonMemberService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class PostForNonMemberServiceTest {

    @Autowired
    private lateinit var postForNonMemberService: PostForNonMemberService

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("비회원이 작성한 글 저장")
    fun testSavePostByRealMember() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        galleryRepository.saveAndFlush(gallery)

        // when
        val dto = PostWrittenByNonMemberDto(gallery.id!!, "title", "content", "nickname", "password", mutableListOf())
        val savedPostId = postForNonMemberService.savePost(dto)

        // then
        val findPost = postRepository.findByPostId(savedPostId)
        assertThat(findPost.member).isNull()
        assertThat(findPost.nickname).isEqualTo(dto.nickname)
        assertThat(findPost.password).isEqualTo(dto.password)
        assertThat(findPost.writtenByNonMember).isTrue

        assertThat(findPost.gallery).isEqualTo(gallery)
        assertThat(findPost.title).isEqualTo(dto.title)
        assertThat(findPost.content).isEqualTo(dto.content)

        assertThat(findPost.statistics.id).isNotNull
        assertThat(findPost.statistics.viewCount).isEqualTo(0L)
        assertThat(findPost.statistics.likeCount).isEqualTo(0L)
        assertThat(findPost.statistics.dislikeCount).isEqualTo(0L)
        assertThat(findPost.statistics.commentCount).isEqualTo(0L)
    }

    @Test
    @DisplayName("비회원이 게시글 수정")
    fun testUpdatePost() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        galleryRepository.saveAndFlush(gallery)

        val post = Post(null, "nickname", "title", "content", "password", null, gallery)
        postRepository.saveAndFlush(post)

        // when
        val dto = PostUpdateDto("new-title", "new-content")
        postForNonMemberService.updatePost(post.id!!, dto)

        // then
        val findPost = postRepository.findByPostId(post.id!!)
        assertThat(findPost.member).isNull()
        assertThat(findPost.title).isEqualTo(dto.title)
        assertThat(findPost.content).isEqualTo(dto.content)
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    fun deleteSuccess() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        galleryRepository.saveAndFlush(gallery)

        val post = Post(null, "nickname", "title", "content", "password", null, gallery)
        postRepository.saveAndFlush(post)

        // when
        postForNonMemberService.deletePost(post.id!!)

        // then
        assertThatExceptionOfType(NoSuchElementException::class.java)
            .isThrownBy {
                postRepository.findByPostId(post.id!!)
            }
            .withMessage("id: ${post.id} 게시글 정보가 없습니다")
    }

    @Test
    @DisplayName("비회원 비밀번호 확인 테스트")
    fun testCheckAccess() {
        // given
        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        galleryRepository.saveAndFlush(gallery)

        val post = Post(null, "nickname", "title", "content", "password", null, gallery)
        postRepository.saveAndFlush(post)

        // when
        val canAccessWithSamePassword = postForNonMemberService.checkAccess(PostAccessDto(post.id!!, "password"))
        val canAccessWithDifferentPassword = postForNonMemberService.checkAccess(PostAccessDto(post.id!!, "password2"))

        // then
        assertThat(canAccessWithSamePassword).isTrue
        assertThat(canAccessWithDifferentPassword).isFalse
    }
}
