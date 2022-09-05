package org.deepforest.dcinside.upload

import org.deepforest.dcinside.dto.ResponseDto
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api/v1/images")
@RestController
class ImageUploadController(
    private val imageUploadService: ImageUploadService
) {

    @PostMapping("/{path}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @PathVariable("path") path: FolderName,
        @RequestParam files: List<MultipartFile>
    ): ResponseDto<List<String>> = ResponseDto.ok(
        files.map { imageUploadService.upload(path, it) }
    )
}
