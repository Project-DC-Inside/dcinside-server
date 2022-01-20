package org.deepforest.dcinside.gallery

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.springframework.data.jpa.repository.JpaRepository

fun GalleryRepository.findByGalleryName(name: String) =
    findByName(name) ?: throw NoSuchElementException("$name 갤러리 정보가 없습니다")

interface GalleryRepository : JpaRepository<Gallery, Long> {
    fun findByName(name: String): Gallery?
    fun findByType(type: GalleryType): List<Gallery>
}
