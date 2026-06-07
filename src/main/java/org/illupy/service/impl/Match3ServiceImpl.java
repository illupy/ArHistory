package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.*;
import org.illupy.entity.Match3Set;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.Match3SetRepository;
import org.illupy.service.Match3Service;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class Match3ServiceImpl implements Match3Service {

    private final Match3SetRepository match3SetRepository;

    @Override
    public Match3SetResponse create(CreateMatch3SetRequest request) {
        Match3Set set = Match3Set.builder()
                .imageUrl1(request.getImageUrl1())
                .imageUrl2(request.getImageUrl2())
                .imageUrl3(request.getImageUrl3())
                .note(request.getNote())
                .noteType(request.getNoteType() != null ? request.getNoteType() : "TEXT")
                .noteMediaUrl(request.getNoteMediaUrl())
                .noteModelCode(request.getNoteModelCode())
                .createdAt(LocalDateTime.now())
                .build();

        set = match3SetRepository.save(set);
        return toResponse(set);
    }

    @Override
    public List<Match3SetResponse> getAll() {
        return match3SetRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Match3SetResponse getById(Long id) {
        Match3Set set = match3SetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match3 set not found"));
        return toResponse(set);
    }

    @Override
    public Match3SetResponse update(Long id, UpdateMatch3SetRequest request) {
        Match3Set set = match3SetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match3 set not found"));

        if (request.getImageUrl1() != null) set.setImageUrl1(request.getImageUrl1());
        if (request.getImageUrl2() != null) set.setImageUrl2(request.getImageUrl2());
        if (request.getImageUrl3() != null) set.setImageUrl3(request.getImageUrl3());
        if (request.getNote() != null) set.setNote(request.getNote());
        if (request.getNoteType() != null) {
            set.setNoteType(request.getNoteType());
            // Clear fields not related to the new type
            if ("TEXT".equals(request.getNoteType())) {
                set.setNoteMediaUrl(null);
                set.setNoteModelCode(null);
            } else if ("IMAGE".equals(request.getNoteType())) {
                set.setNoteModelCode(null);
            } else if ("MODEL".equals(request.getNoteType())) {
                set.setNoteMediaUrl(null);
            }
        }
        if (request.getNoteMediaUrl() != null) set.setNoteMediaUrl(request.getNoteMediaUrl().isEmpty() ? null : request.getNoteMediaUrl());
        if (request.getNoteModelCode() != null) set.setNoteModelCode(request.getNoteModelCode().isEmpty() ? null : request.getNoteModelCode());

        set = match3SetRepository.save(set);
        return toResponse(set);
    }

    @Override
    public void delete(Long id) {
        if (!match3SetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Match3 set not found");
        }
        match3SetRepository.deleteById(id);
    }

    @Override
    public Match3GameResponse getRandomGame() {
        List<Match3Set> all = match3SetRepository.findAll();
        Collections.shuffle(all);

        List<Match3Set> selected = all.stream().limit(8).toList();

        List<Match3SetResponse> sets = selected.stream()
                .map(this::toResponse)
                .toList();

        // Collect all images and shuffle them for the game
        List<String> allImages = new ArrayList<>();
        for (Match3Set s : selected) {
            allImages.add(s.getImageUrl1());
            allImages.add(s.getImageUrl2());
            allImages.add(s.getImageUrl3());
        }
        Collections.shuffle(allImages);

        return Match3GameResponse.builder()
                .sets(sets)
                .allImages(allImages)
                .build();
    }

    private Match3SetResponse toResponse(Match3Set set) {
        return Match3SetResponse.builder()
                .id(set.getId())
                .imageUrl1(set.getImageUrl1())
                .imageUrl2(set.getImageUrl2())
                .imageUrl3(set.getImageUrl3())
                .note(set.getNote())
                .noteType(set.getNoteType())
                .noteMediaUrl(set.getNoteMediaUrl())
                .noteModelCode(set.getNoteModelCode())
                .build();
    }
}
