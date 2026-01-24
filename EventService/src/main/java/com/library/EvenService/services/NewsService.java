package com.library.EvenService.services;


import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.models.News;
import com.library.EvenService.repositories.NewsRepository;
import com.library.EvenService.utill.NewsNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<NewsDTO> findAll(Pageable pageable){
        log.info("Trying to find all news");
        List<News> allNews = newsRepository.findAll();
        if (allNews.isEmpty()){
            log.info("No news found");
            throw new NewsNotFoundException("No news found. ");
        }
        log.info("All news found");
        return allNews.stream().map(this::toDTO).toList();
    }
    @Transactional(readOnly = true)
    public NewsDTO findOneById(int id){
        log.info("Trying to find one news. ID: '{}'", id);
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()){
            log.info("The news not found. ID: '{}'", id);
            throw new NewsNotFoundException("News not found. ");
        }
        log.info("The news found. ID: '{}'", id);
        return toDTO(optionalNews.get());
    }
    @Transactional
    public void save(NewsDTO newsDTO){
        News news = toEntity(newsDTO);
        news.setDate(LocalDate.now());
        newsRepository.save(news);
        log.info("The news saved. ID: '{}'", news.getId());
    }
    @Transactional
    public void update(int id, NewsDTO newsDTO){
        log.info("Trying to find a news for update. ID: '{}'", id);
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()){
            log.info("The news for update not found. ID: '{}'", id);
            throw new NewsNotFoundException("News not found");
        }
        log.info("Starting of updating the news. ID: '{}'", id);
        News news = optionalNews.get();
        if (newsDTO.getBody()!=null)
            news.setBody(newsDTO.getBody());
        if (newsDTO.getName()!=null)
            news.setName(newsDTO.getName());
        if (newsDTO.getPhoto()!=null)
            news.setPhoto(newsDTO.getPhoto());
        newsRepository.save(news);
        log.info("The news updated. ID: '{}'", id);
    }
    @Transactional
    public void delete(int id){
        newsRepository.deleteById(id);
        log.info("The news deleted. ID: '{}'", id);
    }



    public News toEntity(NewsDTO newsDTO){
        log.info("Mapping newsDTO to entity. Name: '{}'", newsDTO.getName());
        return modelMapper.map(newsDTO, News.class);
    }
    public NewsDTO toDTO(News news){
        log.info("Mapping news to dto. ID: '{}'", news.getId());
        return modelMapper.map(news, NewsDTO.class);
    }
}
