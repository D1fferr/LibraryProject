package ua.zakharchuk.ExpectedBooksService.services;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTO;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOCreate;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOForKafka;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBookNotFoundException;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBooksNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;
import ua.zakharchuk.ExpectedBooksService.repositories.ExpectedBookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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
            String imageUrl = imageService.storeImage(coverImage, expectedBook.getExpectedBookId());
            expectedBook.setExpectedBookImage(imageUrl);
            expectedBookRepository.save(expectedBook);
        }
        return toExpectedBookDTO(expectedBook);
    }

    @Transactional
    public void deleteById(UUID id) {
        expectedBookRepository.deleteByExpectedBookId(id);
    }

    @Transactional
    public ExpectedBookDTO update(UUID id, ExpectedBookDTOCreate expectedBookDTO, MultipartFile coverImage) {
        ExpectedBook expectedBook = expectedBookRepository.findByExpectedBookId(id)
                .orElseThrow(() -> new ExpectedBookNotFoundException("The selected book was not found."));
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
        return toExpectedBookDTO(expectedBook);
    }

    @Transactional(readOnly = true)
    public ExpectedBookDTO findOneBook(UUID id) {
        ExpectedBook expectedBook = expectedBookRepository.findByExpectedBookId(id)
                .orElseThrow(() -> new ExpectedBookNotFoundException("The selected book was not found."));
        return toExpectedBookDTO(expectedBook);
    }

    @Transactional(readOnly = true)
    public ExpectedBookDTOForKafka findOneBookForKafka(UUID id) {
        ExpectedBook expectedBook = expectedBookRepository.findByExpectedBookId(id)
                .orElseThrow(() -> new ExpectedBookNotFoundException("The selected book was not found."));
        return toExpectedBookDTOForKafka(expectedBook);
    }

    @Transactional(readOnly = true)
    public List<ExpectedBookDTO> findAll(Pageable pageable) {
        List<ExpectedBookDTO> expectedBookDTOs = expectedBookRepository
                .findAll(pageable).map(this::toExpectedBookDTO).toList();
        if (expectedBookDTOs.isEmpty())
            throw new ExpectedBooksNotFoundException("The selected books were not found.");
        return expectedBookDTOs;
    }

    private ExpectedBook toExpectedBookEntity(ExpectedBookDTO expectedBookDTO) {
        return modelMapper.map(expectedBookDTO, ExpectedBook.class);
    }

    private ExpectedBookDTO toExpectedBookDTO(ExpectedBook expectedBook) {
        return modelMapper.map(expectedBook, ExpectedBookDTO.class);
    }

    private ExpectedBookDTOForKafka toExpectedBookDTOForKafka(ExpectedBook expectedBook) {
        return modelMapper.map(expectedBook, ExpectedBookDTOForKafka.class);

    }
    private ExpectedBook toExpectedBookEntityCreate(ExpectedBookDTOCreate expectedBookDTO) {
        return modelMapper.map(expectedBookDTO, ExpectedBook.class);
    }
}
