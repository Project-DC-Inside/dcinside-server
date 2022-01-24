package org.deepforest.dcinside.gallery

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.ServiceTestBase
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException

class GalleryServiceTest : ServiceTestBase() {

    @Autowired
    private lateinit var galleryService: GalleryService

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("파라미터를 넘기면 해당 타입의 갤러리 조회, 없으면 전체 조회")
    fun testFind() {
        // given
        repeat (5) { galleryRepository.save(Gallery(type = GalleryType.MAJOR, name = "GalleryTest2-MAJOR-Name$it")) }
        repeat (10) { galleryRepository.save(Gallery(type = GalleryType.MINOR, name = "GalleryTest2-MINOR-Name$it")) }
        galleryRepository.flush()

        // when
        val miniGalleries = galleryService.findGallery(GalleryType.MINI)
        val majorGalleries = galleryService.findGallery(GalleryType.MAJOR)
        val minorGalleries = galleryService.findGallery(GalleryType.MINOR)
        val allGalleries = galleryService.findGallery()

        // then
        assertThat(miniGalleries.size).isEqualTo(0)
        assertThat(majorGalleries.size).isEqualTo(5)
        assertThat(minorGalleries.size).isEqualTo(10)
        assertThat(allGalleries.size).isEqualTo(15)
    }

    @DisplayName("생성 테스트")
    @Nested
    inner class Create {

        @Test
        @DisplayName("갤러리 생성 성공")
        fun testCreate() {
            // given
            val galleryName = "gallery-service-test-name-1"
            val galleryDto = RequestGalleryDto(GalleryType.MINI, galleryName)

            // when
            val savedGallery = galleryService.save(galleryDto)

            // then
            assertThat(savedGallery.name).isEqualTo(galleryName)
            assertThat(savedGallery.type).isEqualTo(GalleryType.MINI)
        }


        @ParameterizedTest
        @CsvSource(value = ["MAJOR,MINOR", "MINOR,MAJOR"])
        @DisplayName("MINOR, MAJOR 타입은 같은 이름의 갤러리를 가질 수 없음")
        fun testCreateFailWhenDuplicate(savedType: GalleryType, newType: GalleryType) {
            // given: 동일한 Id
            val galleryName = "testCreateFailWhenDuplicate-galleryName"

            // when
            galleryService.save(RequestGalleryDto(savedType, galleryName))

            // then
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { galleryService.save(RequestGalleryDto(newType, galleryName)) }
                .withMessage("$galleryName 이름과 중복되는 갤러리가 존재합니다.")
        }

        @ParameterizedTest
        @EnumSource(names = ["MAJOR", "MINOR"])
        @DisplayName("MINI 갤러리는 다른 갤러리와 중복된 이름을 가질 수 있음")
        fun testCreateMini(type: GalleryType) {
            // given
            val galleryName = "testCreateMini-galleryName"
            galleryService.save(RequestGalleryDto(type, galleryName))

            // when
            galleryService.save(RequestGalleryDto(GalleryType.MINI, galleryName))

            // then
            assertThat(galleryRepository.findAll().size).isEqualTo(2)
        }
    }

    @DisplayName("삭제 테스트")
    @Nested
    inner class Delete {

        @Test
        @DisplayName("갤러리 아이디 값을 받아서 삭제")
        fun testDelete() {
            // given
            val savedGallery = galleryRepository.saveAndFlush(Gallery(type = GalleryType.MINOR, name = "GalleryTest3"))

            // when
            galleryService.delete(savedGallery.id!!)

            // then
            assertThatExceptionOfType(NoSuchElementException::class.java)
                .isThrownBy { galleryRepository.findByGalleryId(savedGallery.id!!) }
        }

        @Test
        @DisplayName("갤러리 아이디가 없을 때 삭제하려고 하면 에러")
        fun testDeleteFailWhenNoElement() {
            assertThatExceptionOfType(EmptyResultDataAccessException::class.java)
                .isThrownBy { galleryService.delete(0L) }
        }
    }
}
