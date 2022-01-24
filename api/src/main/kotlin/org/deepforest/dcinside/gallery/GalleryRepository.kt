package org.deepforest.dcinside.gallery

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.springframework.data.jpa.repository.JpaRepository

fun GalleryRepository.findByGalleryId(id: Long): Gallery =
    findById(id).orElseThrow { NoSuchElementException("id: $id 갤러리 정보가 없습니다") }

interface GalleryRepository : JpaRepository<Gallery, Long> {
    fun findByType(type: GalleryType): List<Gallery>
    fun findByName(name: String): List<Gallery>
}
