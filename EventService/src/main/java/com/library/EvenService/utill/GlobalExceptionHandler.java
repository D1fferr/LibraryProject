package com.library.EvenService.utill;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final NewsErrorResponse newsErrorResponse;
    private final AnnouncementErrorResponse announcementErrorResponse;
    private final ImageErrorResponse imageErrorResponse;

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
    public ResponseEntity<AnnouncementErrorResponse> handleAnnouncementsNotFoundException(AnnouncementsNotFoundException e) {
        announcementErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(announcementErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(FailedSaveImageException.class)
    public ResponseEntity<ImageErrorResponse> handleFailedSaveImageException(FailedSaveImageException e){
        imageErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(imageErrorResponse, HttpStatus.EXPECTATION_FAILED);
    }




}
