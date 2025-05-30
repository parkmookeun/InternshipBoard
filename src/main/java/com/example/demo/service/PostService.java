package com.example.demo.service;

import com.example.demo.dto.PostIdResponseDto;
import com.example.demo.dto.PostRequestDto;
import com.example.demo.dto.PostResponseDto;
import com.example.demo.dto.PostUpdateRequestDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostFile;
import com.example.demo.repository.PostFileRepository;
import com.example.demo.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository PostRepository;
    private final PostFileRepository postFileRepository;
    private final S3Service s3Service;

    // 게시글 등록
    public PostIdResponseDto createPost(PostRequestDto dto, List<MultipartFile> files) {
        Post post = new Post(dto.getWriter(), dto.getTitle(), dto.getContents());

        Post savedPost = PostRepository.save(post);

        Post foundPost = PostRepository.findByIdOrElseThrow(savedPost.getId());

        // 2. 파일 업로드 및 연결
        if (files != null && !files.isEmpty()) {
            List<PostFile> postFiles = s3Service.uploadFilesForPost(files, post);

            // PostFile들을 Post에 추가
            for (PostFile postFile : postFiles) {
                post.getPostFiles().add(postFile);
            }

            // PostFile들 저장
            postFileRepository.saveAll(postFiles);
        }

        return new PostIdResponseDto(foundPost.getId());
    }

    //게시글 단건 조회 -> 상세 조회 느낌으로 조회 시마다, 조회 수 + 1
    @Transactional
    public PostResponseDto findPost(Long PostId) {

        Post foundPost = PostRepository.findByIdOrElseThrow(PostId);

        foundPost.increaseViews();

        return new PostResponseDto(foundPost);
    }

    //게시글 다건 조회 -> 모든 게시글 조회 -> 추후에, 페이징 추가
//    public List<PostResponseDto> findPosts() {
//
//        List<Post> PostList = PostRepository.findAll();
//
//        return PostList.stream().map(PostResponseDto::new).toList();
//
//    }
    public Page<Post> findPosts(int pageSize, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by("createdAt").descending());

        return PostRepository.findAll(pageable);
    }

    //게시글 단건 수정 -> 게시글의 제목과 내용 수정
    @Transactional
    public PostResponseDto updatePost(Long PostId, PostUpdateRequestDto dto) {

        Post foundPost = PostRepository.findByIdOrElseThrow(PostId);

        foundPost.updateTitle(dto.getTitle());
        foundPost.updateContents(dto.getContents());

        return new PostResponseDto(foundPost);
    }

    public void deletePost(Long PostId) {

        Post foundPost = PostRepository.findByIdOrElseThrow(PostId);

        PostRepository.delete(foundPost);

    }
}
