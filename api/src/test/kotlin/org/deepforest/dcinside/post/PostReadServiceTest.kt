package org.deepforest.dcinside.post

import org.assertj.core.api.Assertions.assertThat
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.helper.TimeFormatHelper
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.member.MemberType
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.service.PostReadService
import org.junit.jupiter.api.BeforeEach
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

    @Nested
    @DisplayName("게시글 단건 조회 테스트: post.member 가 존재하면 회원, 없으면 비회원")
    inner class FindPost {

        private lateinit var post: Post
        private lateinit var member: Member

        @BeforeEach
        internal fun setUp() {
            member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(member)

            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            galleryRepository.saveAndFlush(gallery)


            post = Post(null, "nickname", "major-title", "major-content", "major-password", member, gallery)
            postRepository.saveAndFlush(post)
        }

        @DisplayName("회원이 작성한 글에는 writer.username 이 존재한다.")
        @Test
        fun testFindPostByRealMember() {

            // when
            val response = postReadService.findPostByIdAndIncreaseViewCount(post.id!!)

            // then
            assertThat(response.id).isEqualTo(post.id)
            assertThat(response.nickname).isEqualTo(post.nickname)
            assertThat(response.title).isEqualTo(post.title)
            assertThat(response.content).isEqualTo(post.content)
            assertThat(response.createdAt).isEqualTo(TimeFormatHelper.from(post.createdAt))
            assertThat(response.updatedAt).isEqualTo(TimeFormatHelper.from(post.updatedAt))

            assertThat(response.writer.memberType).isEqualTo(MemberType.FIXED)
            assertThat(response.writer.nickname).isEqualTo(member.nickname)
            assertThat(response.writer.username).isEqualTo(member.username)
        }

        @Test
        @DisplayName("비회원이 작성한 글은 writer.username 이 존재하지 않는다.")
        fun testFindPostByNonMember() {
            // when
            val response = postReadService.findPostByIdAndIncreaseViewCount(post.id!!)

            // then
            assertThat(response.id).isEqualTo(post.id)
            assertThat(response.nickname).isEqualTo(post.nickname)
            assertThat(response.title).isEqualTo(post.title)
            assertThat(response.content).isEqualTo(post.content)
            assertThat(response.createdAt).isEqualTo(TimeFormatHelper.from(post.createdAt))
            assertThat(response.updatedAt).isEqualTo(TimeFormatHelper.from(post.updatedAt))

            assertThat(response.writer.memberType).isEqualTo(MemberType.NONE)
            assertThat(response.writer.nickname).isEqualTo(post.nickname)
            assertThat(response.writer.username).isNull()
        }

        @DisplayName("게시글이 조회되면, 조회 카운트가 1회 증가한다.")
        @Test
        fun increaseViewCountTest() {
            // when
            val response = postReadService.findPostByIdAndIncreaseViewCount(post.id!!)

            // then
            assertThat(response.postStatistics.viewCount).isEqualTo(1)
        }
    }
}
