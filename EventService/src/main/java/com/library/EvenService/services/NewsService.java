package com.library.EvenService.services;


import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.models.News;
import com.library.EvenService.repositories.NewsRepository;
import com.library.EvenService.utill.NewsNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final ModelMapper modelMapper;

    public NewsService(NewsRepository newsRepository, ModelMapper modelMapper) {
        this.newsRepository = newsRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional(readOnly = true)
    public List<NewsDTO> findAll(Pageable pageable){
        List<NewsDTO> allNews = newsRepository.findAll().stream().map(this::toDTO).toList();
        if (allNews.isEmpty())
            throw new NewsNotFoundException("No news found. ");
        return newsRepository.findAll(pageable).stream().map(this::toDTO).toList();
    }
    @Transactional(readOnly = true)
    public NewsDTO findOneById(int id){
        return newsRepository.findById(id).map(this::toDTO)
                .orElseThrow(()->new NewsNotFoundException("News not found. "));
    }
    @Transactional
    public void save(NewsDTO newsDTO){
        News news = toEntity(newsDTO);
        news.setDate(LocalDate.now());
        newsRepository.save(news);
    }
    @Transactional
    public void update(int id, NewsDTO newsDTO){
        News news = newsRepository.findById(id)
                .orElseThrow(()-> new NewsNotFoundException("News not found"));
        if (newsDTO.getBody()!=null)
            news.setBody(newsDTO.getBody());
        if (newsDTO.getName()!=null)
            news.setName(newsDTO.getName());
        if (newsDTO.getPhoto()!=null)
            news.setPhoto(newsDTO.getPhoto());
        newsRepository.save(news);
    }
    @Transactional
    public void delete(int id){
        newsRepository.deleteById(id);
    }



    public News toEntity(NewsDTO newsDTO){
        return modelMapper.map(newsDTO, News.class);
    }
    public NewsDTO toDTO(News news){
        return modelMapper.map(news, NewsDTO.class);
    }
}
