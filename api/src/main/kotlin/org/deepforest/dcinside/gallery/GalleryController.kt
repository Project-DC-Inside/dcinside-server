package org.deepforest.dcinside.gallery

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.deepforest.dcinside.dto.ResponseDto
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.springframework.web.bind.annotation.*

@Tag(name = "/galleries", description = "갤러리 API")
@RequestMapping("/api/v1/galleries")
@RestController
class GalleryController(
    private val galleryService: GalleryService
) {
    @Operation(
        summary = "갤러리 조회", description = "각 타입에 해당하는 갤러리들을 조회한다",
        responses = [
            ApiResponse(responseCode = "200", description = "List<ResponseGalleryDto>: 갤러리 리스트 반환")
        ]
    )
    @GetMapping
    fun findByGalleryType(
        @RequestParam(required = false) type: GalleryType? = null
    ): ResponseDto<List<ResponseGalleryDto>> = ResponseDto.ok(
        galleryService.findGallery(type)
    )

    @Operation(
        summary = "갤러리 생성", description = "갤러리 타입과 이름을 받아 저장한다",
        responses = [
            ApiResponse(responseCode = "200", description = "별다른 result 는 없음")
        ]
    )
    @PostMapping
    fun save(
        @RequestBody galleryDto: RequestGalleryDto
    ): ResponseDto<ResponseGalleryDto> = ResponseDto.ok(
        galleryService.save(galleryDto)
    )

    @Operation(
        summary = "갤러리 삭제", description = "갤러리 ID 를 받아 삭제한다",
        responses = [
            ApiResponse(responseCode = "200", description = "별다른 result 는 없음")
        ]
    )
    @DeleteMapping("/{galleryId}")
    fun delete(
        @PathVariable("galleryId") id: Long
    ): ResponseDto<Unit> = ResponseDto.ok(
        galleryService.delete(id)
    )
}
