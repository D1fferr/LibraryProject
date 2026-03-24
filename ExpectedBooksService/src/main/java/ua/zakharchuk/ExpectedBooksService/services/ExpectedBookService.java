package ua.zakharchuk.ExpectedBooksService.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTO;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOCreate;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOForKafka;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDtoWithTotalElements;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBookNotFoundException;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBooksNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;
import ua.zakharchuk.ExpectedBooksService.repositories.ExpectedBookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpectedBookService {
    private final ModelMapper modelMapper;
    private final ExpectedBookRepository expectedBookRepository;
    private final ImageService imageService;

    @Transactional
    public ExpectedBookDTO save(ExpectedBookDTOCreate expectedBookDTO, MultipartFile coverImage) {
        ExpectedBook expectedBook = toExpectedBookEntityCreate(expectedBookDTO);
        expectedBook.setExpectedBookAddedAt(LocalDateTime.now());
        expectedBookRepository.save(expectedBook);
        if (coverImage != null && !coverImage.isEmpty()) {
            log.info("Processing cover image for book ID: {}", expectedBook.getExpectedBookId());
            String imageUrl = imageService.storeImage(coverImage, expectedBook.getExpectedBookId());
            expectedBook.setExpectedBookImage(imageUrl);
        }
        expectedBookRepository.save(expectedBook);
        log.info("Expected book saved. ID: '{}'", expectedBook.getExpectedBookId());
        return toExpectedBookDTO(expectedBook);
    }

    @Transactional
    public void deleteById(UUID id) {
        log.info("The expected book is deleted. ID: '{}'", id);
        expectedBookRepository.deleteByExpectedBookId(id);
    }

    @Transactional
    public ExpectedBookDTO update(UUID id, ExpectedBookDTOCreate expectedBookDTO, MultipartFile coverImage) {
        log.info("Trying to find a book for updates. ID '{}'", id);
        Optional<ExpectedBook> optionalExpectedBook = expectedBookRepository.findByExpectedBookId(id);
        if (optionalExpectedBook.isEmpty()){
            log.info("The book not found. ID: '{}'", id);
            throw new ExpectedBookNotFoundException("The selected book was not found.");
        }
        ExpectedBook expectedBook = optionalExpectedBook.get();
        log.info("Starting of updating the expected book. ID '{}'", id);
        if (expectedBookDTO.getExpectedBookName() != null)
            expectedBook.setExpectedBookName(expectedBookDTO.getExpectedBookName());
        if (expectedBookDTO.getExpectedBookAuthor() != null)
            expectedBook.setExpectedBookAuthor(expectedBookDTO.getExpectedBookAuthor());
        if (expectedBookDTO.getExpectedBookYear() != 0)
            expectedBook.setExpectedBookYear(expectedBookDTO.getExpectedBookYear());
        if (expectedBookDTO.getExpectedBookPublication() != null)
            expectedBook.setExpectedBookPublication(expectedBookDTO.getExpectedBookPublication());
        if (expectedBookDTO.getExpectedBookLanguage() != null)
            expectedBook.setExpectedBookLanguage(expectedBookDTO.getExpectedBookLanguage());
        if (expectedBookDTO.getExpectedBookPieces() != 0)
            expectedBook.setExpectedBookPieces(expectedBookDTO.getExpectedBookPieces());
        if (coverImage != null && !coverImage.isEmpty()) {
            String imageUrl = imageService.storeImage(coverImage, expectedBook.getExpectedBookId());
            expectedBook.setExpectedBookImage(imageUrl);
        }
        if (expectedBookDTO.getExpectedBookGenre() != null)
            expectedBook.setExpectedBookGenre(expectedBookDTO.getExpectedBookGenre());
        expectedBookRepository.save(expectedBook);
        log.info("The updated expected book was saved. ID '{}'", id);
        return toExpectedBookDTO(expectedBook);
    }

    @Transactional(readOnly = true)
    public ExpectedBookDTO findById(UUID id) {
        Optional<ExpectedBook> optionalExpectedBook = expectedBookRepository.findByExpectedBookId(id);
        if (optionalExpectedBook.isEmpty()){
            log.warn("The expected book not found. ID: '{}'", id);
            throw new ExpectedBookNotFoundException("The selected book was not found.");
        }
        log.info("The book were found. ID: '{}'", id);
        return toExpectedBookDTO(optionalExpectedBook.get());
    }

    @Transactional(readOnly = true)
    public ExpectedBookDTOForKafka findOneBookForKafka(UUID id) {
        log.info("Trying to find one book for kafka for adding to current books. ID: '{}'", id);
        Optional<ExpectedBook> optionalExpectedBook = expectedBookRepository.findByExpectedBookId(id);
        if (optionalExpectedBook.isEmpty()){
            log.warn("The expected book for kafka not found. ID: '{}'", id);
            throw new ExpectedBookNotFoundException("The selected book was not found.");
        }
        return toExpectedBookDTOForKafka(optionalExpectedBook.get());
    }

    @Transactional(readOnly = true)
    public ExpectedBookDtoWithTotalElements findAll(Pageable pageable) {
        Page<ExpectedBook> expectedBook = expectedBookRepository.findAll(pageable);

        if (expectedBook.getContent().isEmpty()){
            log.warn("Expected books not found");
            throw new ExpectedBooksNotFoundException("The selected books were not found.");
        }
        ExpectedBookDtoWithTotalElements expectedBookDtoWithTotalElements = new ExpectedBookDtoWithTotalElements();
        expectedBookDtoWithTotalElements.setExpectedBooks(expectedBook.getContent().stream().map(this::toExpectedBookDTO).toList());
        expectedBookDtoWithTotalElements.setBookPage(expectedBook.getTotalPages());
        expectedBookDtoWithTotalElements.setBookCount(expectedBook.getTotalElements());
        log.info("Expected books were found");
        return expectedBookDtoWithTotalElements;
    }


    private ExpectedBookDTO toExpectedBookDTO(ExpectedBook expectedBook) {
        log.info("Mapping entity to expected bookDTO. ID: '{}'", expectedBook.getExpectedBookId());
        return modelMapper.map(expectedBook, ExpectedBookDTO.class);
    }

    private ExpectedBookDTOForKafka toExpectedBookDTOForKafka(ExpectedBook expectedBook) {
        log.info("Mapping entity to expected bookDTO for kafka. ID: '{}'", expectedBook.getExpectedBookId());
        return modelMapper.map(expectedBook, ExpectedBookDTOForKafka.class);

    }
    private ExpectedBook toExpectedBookEntityCreate(ExpectedBookDTOCreate expectedBookDTO) {
        log.info("Mapping expected bookDTO create to entity. Title: '{}'", expectedBookDTO.getExpectedBookName());
        return modelMapper.map(expectedBookDTO, ExpectedBook.class);
    }
}
