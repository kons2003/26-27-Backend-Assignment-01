package com.gdg.restapi.controller;

import com.gdg.restapi.dto.ScoreRequest;
import com.gdg.restapi.dto.ScoreResponse;
import com.gdg.restapi.service.PlayService;
import com.gdg.restapi.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scores")
public class ScoreController {
    private final ScoreService scoreService;
    private final PlayService playService;

    // 악보 등록
    @PostMapping
    public ResponseEntity<ScoreResponse> create(@Validated(ScoreRequest.CreateGroup.class)
                                                @RequestBody ScoreRequest request) {
        ScoreResponse response = scoreService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // 클라이언트가 요청을 보낸 주소
                .path("/{id}") // 주소 맨 뒷줄에 /{id} 추가
                .buildAndExpand(response.id()) // id 값 넣음
                .toUri(); // URI로 객체 변환
        return ResponseEntity.created(location).body(response);
    }

    // 모든 악보 찾기
    @GetMapping
    public ResponseEntity<List<ScoreResponse>> findAll() {
        return ResponseEntity.ok(scoreService.findAll());
    }

    // id로 악보 찾기
    @GetMapping("/{id}")
    public ResponseEntity<ScoreResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(scoreService.findById(id));
    }

    // id로 악보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ScoreResponse> update(@PathVariable Long id,
                                                @Validated(ScoreRequest.UpdateGroup.class)
                                                @RequestBody ScoreRequest request) {
        return ResponseEntity.ok(scoreService.update(id, request));
    }

    // id로 악보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scoreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // id로 악보 재생
    @PostMapping("/{id}/play") // 서버의 상태 변화 및 자원 소모를 해서 Post 사용
    public ResponseEntity<ScoreResponse> playById(@PathVariable Long id) {
        ScoreResponse response = scoreService.findById(id); // id가 없을 시 throw
        playService.play(response.instrument(), response.melody());
        return ResponseEntity.ok(response);
    }
}
