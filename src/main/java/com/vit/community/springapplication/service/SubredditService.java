package com.vit.community.springapplication.service;

import com.vit.community.springapplication.dto.SubredditDto;
import com.vit.community.springapplication.exceptions.SpringCommunityException;
import com.vit.community.springapplication.mapper.SubredditMapper;
import com.vit.community.springapplication.model.Subreddit;
import com.vit.community.springapplication.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

/*
* This class is responsible for saving and retrieving the subreddits
* to and from the database using the subredditRepository.
*
* The Lombok library generates the all arguments constructor, and the fields
* are automatically autowired (initialised) by Spring at runtime.
* */

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    /*
    * This method saves the subreddit into the database using the subredditRepository after
    * mapping the subredditDto received to the subreddit using the subredditMapper interface.
    * */

    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    /*
    * This method retrieves all the existing subreddits from the database using the subredditRepository,
    * maps them to subredditDto and returns them to the controller as a list of subredditDto-s.
    * */

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    /*
    * This method retrieves the subreddit with the given id from the database using the subredditRepository,
    * maps it to a subredditDto and returns it back to the controller.
    * */

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringCommunityException("No subreddit found with ID - " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
