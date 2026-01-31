package com.library.EvenService.services;


import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.models.News;
import com.library.EvenService.repositories.NewsRepository;
import com.library.EvenService.utill.NewsNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public List<NewsDTO> findAll(Pageable pageable){
        Page<News> allNews = newsRepository.findAll(pageable);
        if (allNews.isEmpty()){
            log.warn("No news found");
            throw new NewsNotFoundException("No news found. ");
        }
        log.info("All news found");
        return allNews.stream().map(this::toDTO).toList();
    }
    @Transactional(readOnly = true)
    public NewsDTO findOneById(UUID id){
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()){
            log.warn("The news not found. ID: '{}'", id);
            throw new NewsNotFoundException("News not found. ");
        }
        log.info("The news found. ID: '{}'", id);
        return toDTO(optionalNews.get());
    }
    @Transactional
    public void save(NewsDTO newsDTO, MultipartFile image){
        News news = toEntity(newsDTO);
        news.setDate(LocalDate.now());
        newsRepository.save(news);
        if (image != null && !image.isEmpty()) {
            log.info("Processing cover image for news ID: {}", news.getId());
            String imageUrl = imageService.storeImage(image, news.getId());
            news.setPhoto(imageUrl);
        }
        newsRepository.save(news);
        log.info("The news saved. ID: '{}'", news.getId());
    }
    @Transactional
    public void update(UUID id, NewsDTO newsDTO, MultipartFile image){
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()){
            log.warn("The news for update not found. ID: '{}'", id);
            throw new NewsNotFoundException("News not found");
        }
        log.info("Starting of updating the news. ID: '{}'", id);
        News news = optionalNews.get();
        if (newsDTO.getBody()!=null)
            news.setBody(newsDTO.getBody());
        if (newsDTO.getName()!=null)
            news.setName(newsDTO.getName());
        if (image != null && !image.isEmpty()) {
            log.info("Processing cover image for news ID: {}", news.getId());
            String imageUrl = imageService.storeImage(image, news.getId());
            news.setPhoto(imageUrl);
        }
        newsRepository.save(news);
        log.info("The news updated. ID: '{}'", id);
    }
    @Transactional
    public void delete(UUID id){
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
