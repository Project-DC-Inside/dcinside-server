package org.deepforest.dcinside.post

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByGalleryWithPaging
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PostRepositoryTest {

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("회원의 게시글 생성 간단하게 테스트")
    fun testSave() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        val gallery = Gallery(null, GalleryType.MINOR, "galleryName")
        val post = Post(null, "nickname", "title", "content", "password", member, gallery)

        // when
        memberRepository.saveAndFlush(member)
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        // then
        val findPosts = postRepository.findByGalleryWithPaging(gallery)
        assertThat(findPosts.size).isEqualTo(1)

        val findPost = findPosts.first()
        assertThat(findPost).isEqualTo(post)
        assertThat(findPost.id).isEqualTo(post.id)
    }

    @Test
    @DisplayName("비회원이 작성한 (Member 가 없는) 게시글 생성 테스트")
    fun testSaveByNonMember() {
        // given
        val gallery = Gallery(null, GalleryType.MINOR, "galleryName")
        val post = Post(null, "nickname", "title", "content", password = null, member = null, gallery)

        // when
        galleryRepository.saveAndFlush(gallery)
        postRepository.saveAndFlush(post)

        // then
        val findPosts = postRepository.findByGalleryWithPaging(gallery)
        assertThat(findPosts.size).isEqualTo(1)

        val findPost = findPosts.first()
        assertThat(findPost).isEqualTo(post)
        assertThat(findPost.id).isEqualTo(post.id)
        assertThat(findPost.member).isNull()
    }


    @Test
    @DisplayName("게시글 20개 가져오기 테스트")
    fun testFind() {
        // given
        val gallery = Gallery(null, GalleryType.MINOR, "galleryName")
        galleryRepository.saveAndFlush(gallery)

        repeat (30) {
            val post = Post(null, "nickname-$it", "title-$it", "content-$it", "password-$it", null, gallery)
            postRepository.saveAndFlush(post)
        }

        // when
        val findPosts = postRepository.findByGalleryWithPaging(gallery, Long.MAX_VALUE)

        // then
        assertThat(findPosts.size).isEqualTo(20)
    }
}
