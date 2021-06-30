package com.example.demo.service;

import com.example.demo.dto.SubredditDto;
import com.example.demo.exception.SpringRedditException;
import com.example.demo.mapper.SubredditMapper;
import com.example.demo.model.Subreddit;
import com.example.demo.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
      Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
      subredditDto.setId(save.getId());
      return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
      return subredditRepository.findAll().
              stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }


    public SubredditDto getSubreddit(Long id) throws SpringRedditException {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException(
                "Subreddit not found"
        ));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
