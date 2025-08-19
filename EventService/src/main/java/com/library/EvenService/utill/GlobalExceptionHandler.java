package com.library.EvenService.utill;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final NewsErrorResponse newsErrorResponse;
    private final AnnouncementErrorResponse announcementErrorResponse;

    public GlobalExceptionHandler(NewsErrorResponse newsErrorResponse, AnnouncementErrorResponse announcementErrorResponse) {
        this.newsErrorResponse = newsErrorResponse;
        this.announcementErrorResponse = announcementErrorResponse;
    }
    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<NewsErrorResponse> handleNewsNotFoundException(NewsNotFoundException e){
        newsErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(newsErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NewsNotCreatedException.class)
    public ResponseEntity<NewsErrorResponse> handleNewsNotCreatedException(NewsNotCreatedException e){
        newsErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(newsErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AnnouncementNotFoundException.class)
    public ResponseEntity<AnnouncementErrorResponse> handleAnnouncementNotFoundException(AnnouncementNotFoundException e){
        announcementErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(announcementErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AnnouncementsNotFoundException.class)
    public ResponseEntity<AnnouncementErrorResponse> handleAnnouncementsNotFoundException(AnnouncementsNotFoundException e){
        announcementErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(announcementErrorResponse, HttpStatus.NOT_FOUND);
    }



}
