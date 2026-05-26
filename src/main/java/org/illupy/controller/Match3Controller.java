package org.illupy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.*;
import org.illupy.service.Match3Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match3")
@RequiredArgsConstructor
public class Match3Controller {

    private final Match3Service match3Service;

    @PostMapping
    public ApiResponse<Match3SetResponse> create(@Valid @RequestBody CreateMatch3SetRequest request) {
        return ApiResponse.success(match3Service.create(request), "Tạo bộ 3 ảnh thành công");
    }

    @GetMapping
    public ApiResponse<List<Match3SetResponse>> getAll() {
        return ApiResponse.success(match3Service.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Match3SetResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(match3Service.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Match3SetResponse> update(@PathVariable Long id, @RequestBody UpdateMatch3SetRequest request) {
        return ApiResponse.success(match3Service.update(id, request), "Cập nhật bộ 3 ảnh thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        match3Service.delete(id);
        return ApiResponse.success(null, "Xóa bộ 3 ảnh thành công");
    }

    @GetMapping("/game")
    public ApiResponse<Match3GameResponse> getRandomGame() {
        return ApiResponse.success(match3Service.getRandomGame());
    }
}
