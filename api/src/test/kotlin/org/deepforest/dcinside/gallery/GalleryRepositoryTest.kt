package org.deepforest.dcinside.gallery

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class GalleryRepositoryTest {

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("Unique(type, name) 키값으로는 중복 불가능")
    fun testSaveFailWhenUniqueKey() {
        // type, name 은 각각은 중복 가능
        val gallery = Gallery(type = GalleryType.MAJOR, name = "major1")
        val majorGallery = Gallery(type = GalleryType.MAJOR, name = "major2")
        val minorGallery = Gallery(type = GalleryType.MINOR, name = "major1")

        galleryRepository.save(gallery)
        galleryRepository.save(majorGallery)
        galleryRepository.save(minorGallery)

        assertThat(galleryRepository.findAll().size).isEqualTo(3)

        // (type, name) 합쳐서는 중복 불가능
        val duplicatedGallery = Gallery(type = GalleryType.MAJOR, name = "major1")

        assertThatExceptionOfType(DataIntegrityViolationException::class.java)
            .isThrownBy { galleryRepository.save(duplicatedGallery) }
    }
}
