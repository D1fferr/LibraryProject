package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.config.ExternalConfig;
import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.ReservationException;
import com.library.FrontendMicroservice.exceptions.UserExeption;
import com.library.FrontendMicroservice.models.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveService {
    private final ExternalConfig config;
    private final RestTemplate authorizedRestTemplate;


    public void reserveBook(Reservation reservation) {
        String apiGateway = config.getServices().getApiGateway();
        try {
            authorizedRestTemplate.postForObject(
                    apiGateway + "/api/reservation/auth/create",
                    reservation,
                    String.class
            );
        } catch (Exception e) {
            log.info(e.getClass().getName());
            log.info("Failed to reserve a book  {}", e.getMessage());
            throw e;
        }
    }

    public ReservationsPageDto getAllReservations(int page, int pageSize, String search) {
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder;
            if (search != null) {
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/reservation/auth/view_all")
                        .queryParam("page", page)
                        .queryParam("reservationPerPage", pageSize)
                        .queryParam("search", search);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        ReservationsPageDto.class
                );
            } else {
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/reservation/auth/view_all")
                        .queryParam("page", page)
                        .queryParam("reservationPerPage", pageSize);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        ReservationsPageDto.class
                );
            }

        } catch (Exception e) {
            log.info("Failed to get all reservations  {}", e.getMessage());
            throw e;
        }
    }

    public ReservationsPageDto getUserReservations(String id, int page, int pageSize) {
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/reservation/auth/view_for_the_user/" + id)
                    .queryParam("page", page)
                    .queryParam("reservationPerPage", pageSize);

            String url = builder.toUriString();
            System.out.println("Start connecting");
            return authorizedRestTemplate.getForObject(
                    url,
                    ReservationsPageDto.class
            );
        } catch (Exception e) {
            log.info("Failed to get user reservations  {}", e.getMessage());
            throw e;
        }
    }

    public ReservationsPageDto getReservationsByBook(UUID id, int page, int pageSize) {
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/reservation/auth/view_for_the_book/" + id.toString())
                    .queryParam("page", page)
                    .queryParam("reservationPerPage", pageSize);

            String url = builder.toUriString();
            return authorizedRestTemplate.getForObject(
                    url,
                    ReservationsPageDto.class
            );
        } catch (Exception e) {
            log.info("Failed to get book reservations  {}", e.getMessage());
            throw e;
        }
    }

    public void cancelReservation(UUID id) {
        String apiGateway = config.getServices().getApiGateway();
        String url = apiGateway + "/api/reservation/auth/delete/" + id.toString();
        try {
            authorizedRestTemplate.delete(url);
        } catch (Exception e) {
            log.info("Failed to cancel a reservation  {}", e.getMessage());
            throw e;
        }
    }

    public JoinDTOForCancelledReservations getCancelledReservationDetails(UUID id) {
        String apiGateway = config.getServices().getApiGateway();
        try {
            return authorizedRestTemplate.getForObject(
                    apiGateway + "/api/cancel-reservation/auth/user/get-one/" + id.toString(),
                    JoinDTOForCancelledReservations.class
            );
        } catch (Exception e) {
            log.info("Failed to get all cancelled reservations details  {}", e.getMessage());
            throw e;
        }
    }

    public void confirmReservation(UUID id) {
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/reservation/auth/change_status/" + id.toString());

            String url = builder.toUriString();
            authorizedRestTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    null,
                    String.class
            );
        } catch (Exception e) {
            log.info("Failed to confirm  reservation {}", e.getMessage());
            throw e;
        }
    }

    public void cancelReservationForBook(UUID id, ReservationCancellationNotificationDTO dto) {
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/cancel-reservation/auth/cancel/" + id.toString());

            String url = builder.toUriString();
            authorizedRestTemplate.postForObject(
                    url,
                    dto,
                    ReservationCancellationNotificationDTO.class
            );
        } catch (Exception e) {
            log.info("Failed to cancel a reservation for book  {}", e.getMessage());
            throw e;
        }
    }

}
