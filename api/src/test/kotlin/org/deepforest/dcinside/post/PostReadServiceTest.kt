package org.deepforest.dcinside.post

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.helper.TimeFormatHelper
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.member.MemberType
import org.deepforest.dcinside.post.dto.PostResponseDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.service.PostReadService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class PostReadServiceTest {

    @Autowired
    private lateinit var postReadService: PostReadService

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

        repeat (10) {
            val post = Post(null, "major-nickname-$it", "major-title-$it", "major-content-$it", "major-password-$it", member, majorGallery)
            postRepository.saveAndFlush(post)
        }
        repeat (5) {
            val post = Post(null, "minor-nickname-$it", "minor-title-$it", "minor-content-$it", "minor-password-$it", member, minorGallery)
            postRepository.saveAndFlush(post)
        }

        // when
        val majorPosts: List<PostResponseDto> = postReadService.findPostsByGalleryId(majorGallery.id!!)
        val minorPosts: List<PostResponseDto> = postReadService.findPostsByGalleryId(minorGallery.id!!)

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

    @Nested
    @DisplayName("게시글 단건 조회 테스트: post.member 가 존재하면 회원, 없으면 비회원")
    inner class FindPost {

        @Test
        @DisplayName("회원이 작성한 글에는 writer.username 존재")
        fun testFindPostByRealMember() {
            // given
            val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(member)

            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            galleryRepository.saveAndFlush(gallery)

            val post = Post(null, "nickname", "major-title", "major-content", "major-password", member, gallery)
            postRepository.saveAndFlush(post)

            // when
            val postDto: PostResponseDto = postReadService.findPostById(post.id!!)

            // then
            assertThat(postDto.id).isEqualTo(post.id)
            assertThat(postDto.nickname).isEqualTo(post.nickname)
            assertThat(postDto.title).isEqualTo(post.title)
            assertThat(postDto.content).isEqualTo(post.content)
            assertThat(postDto.createdAt).isEqualTo(TimeFormatHelper.from(post.createdAt))
            assertThat(postDto.updatedAt).isEqualTo(TimeFormatHelper.from(post.updatedAt))

            assertThat(postDto.writer.memberType).isEqualTo(MemberType.FIXED)
            assertThat(postDto.writer.nickname).isEqualTo(member.nickname)
            assertThat(postDto.writer.username).isEqualTo(member.username)
        }

        @Test
        @DisplayName("비회원이 작성한 글은 writer.username 없음")
        fun testFindPostByNonMember() {
            // given
            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            galleryRepository.saveAndFlush(gallery)

            val post = Post(null, "nickname", "major-title", "major-content", password = null, member = null, gallery)
            postRepository.saveAndFlush(post)

            // when
            val postDto: PostResponseDto = postReadService.findPostById(post.id!!)

            // then
            assertThat(postDto.id).isEqualTo(post.id)
            assertThat(postDto.nickname).isEqualTo(post.nickname)
            assertThat(postDto.title).isEqualTo(post.title)
            assertThat(postDto.content).isEqualTo(post.content)
            assertThat(postDto.createdAt).isEqualTo(TimeFormatHelper.from(post.createdAt))
            assertThat(postDto.updatedAt).isEqualTo(TimeFormatHelper.from(post.updatedAt))

            assertThat(postDto.writer.memberType).isEqualTo(MemberType.NONE)
            assertThat(postDto.writer.nickname).isEqualTo(post.nickname)
            assertThat(postDto.writer.username).isNull()
        }
    }
}
