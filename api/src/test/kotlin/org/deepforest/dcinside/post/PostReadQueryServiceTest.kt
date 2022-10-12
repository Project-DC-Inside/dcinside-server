package org.deepforest.dcinside.post

import org.assertj.core.api.Assertions.assertThat
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.post.dto.PostResponseDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.service.PostReadQueryService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class PostReadQueryServiceTest {

    @Autowired
    private lateinit var postReadQueryService: PostReadQueryService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("특정 갤러리의 게시글 리스트 가져오기. 리스트가 제공되기 때문에 사이즈가 클 수도 있는 content 는 null 로 내려줌")
    fun testFindLists() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        memberRepository.saveAndFlush(member)

        val majorGallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        val minorGallery = Gallery(null, GalleryType.MINOR, "minor-gallery")
        galleryRepository.saveAndFlush(minorGallery)
        galleryRepository.saveAndFlush(majorGallery)

        repeat(10) {
            val post = Post(
                null,
                "major-nickname-$it",
                "major-title-$it",
                "major-content-$it",
                "major-password-$it",
                member,
                majorGallery
            )
            postRepository.saveAndFlush(post)
        }
        repeat(5) {
            val post = Post(
                null,
                "minor-nickname-$it",
                "minor-title-$it",
                "minor-content-$it",
                "minor-password-$it",
                member,
                minorGallery
            )
            postRepository.saveAndFlush(post)
        }

        // when
        val majorPosts: List<PostResponseDto> = postReadQueryService.findPostsByGalleryId(majorGallery.id!!)
        val minorPosts: List<PostResponseDto> = postReadQueryService.findPostsByGalleryId(minorGallery.id!!)

        // then
        assertThat(majorPosts.size).isEqualTo(10)
        assertThat(minorPosts.size).isEqualTo(5)

        majorPosts.reversed().forEachIndexed { index, res ->
            assertThat(res.id).isNotNull
            assertThat(res.nickname).isEqualTo("major-nickname-$index")
            assertThat(res.title).isEqualTo("major-title-$index")
            assertThat(res.content).isNull()
        }

        minorPosts.reversed().forEachIndexed { index, res ->
            assertThat(res.id).isNotNull
            assertThat(res.nickname).isEqualTo("minor-nickname-$index")
            assertThat(res.title).isEqualTo("minor-title-$index")
            assertThat(res.content).isNull()
        }
    }
}
