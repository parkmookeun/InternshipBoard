package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.Post;
import com.example.demo.global.ErrorResponse;
import com.example.demo.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService PostService;


    @PostMapping("/posts")
    @Tag(name = "게시글 등록", description = "게시글을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 등록 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PostIdResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "등록 성공시, 해당 게시글ID를 반환합니다.",
                                            summary = "성공 응답 예시",
                                            value = """
                                                    {
                                                      "id": 1
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터 유효성 실패",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "내용은 빈칸인 경우 실패 응답",
                                            summary = "실패 응답 예시",
                                            value = """
                                                    {
                                                      "httpStatus": "BAD_REQUEST",
                                                      "errorMessage": "내용은 빈칸일 수 없습니다!"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<PostIdResponseDto> postPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "기본 예시",
                                            summary = "기본 게시글 예시",
                                            value = """
                                                    {
                                                      "writer": "작성자",
                                                      "title": "글 제목",
                                                      "contents": "글 내용"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody PostRequestDto dto
    ){
        PostIdResponseDto responseDto = PostService.postPost(dto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/posts/{postId}")
    @Tag(name = "게시글 조회", description = "게시글을 단건 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PostResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "보드ID에 맞는 글을 반환합니다.",
                                            summary = "성공 응답 예시",
                                            value = """
                                                    {
                                                       "id": 1,
                                                       "createdAt": "2025-05-14T14:41:14.460Z",
                                                       "modifiedAt": "2025-05-14T14:41:14.460Z",
                                                       "writer": "작성자",
                                                       "title": "글 제목",
                                                       "contents": "글 내용",
                                                       "views": 1
                                                     }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 게시글",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "게시글이 존재하지 않을 경우",
                                            summary = "실패 응답 예시",
                                            value = """
                                                    {
                                                       "httpStatus" : "NOT_FOUND",
                                                       "errorMessage" : "1에 맞는 게시글이 존재하지 않습니다!"
                                                     }
                                                    """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<PostResponseDto> findPost(
            @PathVariable("postId") Long postId
    ){
       PostResponseDto responseDto = PostService.findPost(postId);

       return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/posts")
    @Tag(name = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PostResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "게시글 목록을 리스트로 조회합니다.",
                                            summary = "성공 응답 예시",
                                            value = """
                                                    {
                                                       "content": [
                                                         {
                                                           "id": 19,
                                                           "createdAt": "2025-05-20T00:44:30",
                                                           "modifiedAt": "2025-05-20T00:44:30",
                                                           "writer": "홍길동",
                                                           "title": "테스트 제목",
                                                           "contents": "테스트 내용입니다.",
                                                           "views": 0
                                                         },
                                                         {
                                                           "id": 20,
                                                           "createdAt": "2025-05-20T00:44:30",
                                                           "modifiedAt": "2025-05-20T00:44:30",
                                                           "writer": "홍길동",
                                                           "title": "테스트 제목",
                                                           "contents": "테스트 내용입니다.",
                                                           "views": 0
                                                         }
                                                       ],
                                                       "pageNumber": 1,
                                                       "pageSize": 10,
                                                       "totalPages": 10,
                                                       "totalElements": 100,
                                                       "first": true,
                                                       "last": false
                                                     }
                                                    """
                                    )
                            }
                    )
            )})
    public ResponseEntity<PageResponseDto<Post>> findPosts(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize

    ){
        Page<Post> PostPage = PostService.findPosts(pageSize, pageNumber);

        PageResponseDto<Post> PostPageResponseDto = PageResponseDto.of(PostPage);

        return new ResponseEntity<>(PostPageResponseDto, HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}")
    @Tag(name = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PostResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "보드ID에 해당하는 게시글을 수정합니다.",
                                            summary = "성공 응답 예시",
                                            value = """
                                                    {
                                                       "id": 1,
                                                       "createdAt": "2025-05-14T14:41:14.460Z",
                                                       "modifiedAt": "2025-05-14T14:41:14.460Z",
                                                       "writer": "작성자",
                                                       "title": "글 제목 수정됨",
                                                       "contents": "글 내용 수정됨",
                                                       "views": 0
                                                     }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 게시글",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "보드ID에 해당하는 게시글이 없는 경우",
                                            summary = "실패 응답 예시",
                                            value = """
                                                    {
                                                       "httpStatus" : "NOT_FOUND",
                                                       "errorMessage" : "1에 맞는 게시글이 존재하지 않습니다!"
                                                     }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터 유효성 실패",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "데이터 유효성 검사에 실패한 경우",
                                            summary = "실패 응답 예시",
                                            value = """
                                                    {
                                                      "httpStatus": "BAD_REQUEST",
                                                      "errorMessage": "내용은 빈칸일 수 없습니다!"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable("postId") Long postId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "기본 예시",
                                            summary = "기본 게시글 예시",
                                            value = """
                                                    {
                                                      "title": "글 제목 수정됨",
                                                      "contents": "글 내용 수정됨"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody PostUpdateRequestDto dto
    ){
        PostResponseDto responseDto = PostService.updatePost(postId, dto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @DeleteMapping("/posts/{postId}")
    @Tag(name = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 삭제 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 게시글",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "보드ID에 해당하는 게시글이 존재하지 않는 경우",
                                            summary = "실패 응답 예시",
                                            value = """
                                                    {
                                                       "httpStatus" : "NOT_FOUND",
                                                       "errorMessage" : "1에 맞는 게시글이 존재하지 않습니다!"
                                                     }
                                                    """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<Void> deletePost(
        @PathVariable("postId") Long postId
    ){

        PostService.deletePost(postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
