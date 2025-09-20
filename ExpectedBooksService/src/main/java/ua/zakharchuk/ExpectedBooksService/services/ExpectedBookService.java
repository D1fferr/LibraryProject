package ua.zakharchuk.ExpectedBooksService.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTO;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBookNotFoundException;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBooksNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;
import ua.zakharchuk.ExpectedBooksService.repositories.ExpectedBookRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExpectedBookService {
    private final ModelMapper modelMapper;
    private final ExpectedBookRepositories expectedBookRepositories;

    @Transactional
    public void save(ExpectedBookDTO expectedBookDTO){
        expectedBookRepositories.save(toExpectedBookEntity(expectedBookDTO));
    }
    @Transactional
    public void deleteById(UUID id){
        expectedBookRepositories.deleteByExpectedBookId(id);
    }
    @Transactional
    public void update(UUID id, ExpectedBookDTO expectedBookDTO){
        ExpectedBook expectedBook = expectedBookRepositories.findByExpectedBookId(id)
                .orElseThrow(()->new ExpectedBookNotFoundException("The selected book was not found."));
        if (expectedBookDTO.getExpectedBookName()!=null)
            expectedBook.setExpectedBookName(expectedBookDTO.getExpectedBookName());
        if (expectedBookDTO.getExpectedBookAuthor()!=null)
            expectedBook.setExpectedBookAuthor(expectedBookDTO.getExpectedBookAuthor());
        if (expectedBookDTO.getExpectedBookYear()!=0)
            expectedBook.setExpectedBookYear(expectedBookDTO.getExpectedBookYear());
        if (expectedBookDTO.getExpectedBookPublication()!=null)
            expectedBook.setExpectedBookPublication(expectedBookDTO.getExpectedBookPublication());
        if (expectedBookDTO.getExpectedBookLanguage()!=null)
            expectedBook.setExpectedBookLanguage(expectedBookDTO.getExpectedBookLanguage());
        if (expectedBookDTO.getExpectedBookPieces()!=0)
            expectedBook.setExpectedBookPieces(expectedBookDTO.getExpectedBookPieces());
        if (expectedBookDTO.getExpectedBookImage()!=null)
            expectedBook.setExpectedBookImage(expectedBookDTO.getExpectedBookImage());
        if (expectedBookDTO.getExpectedBookGenre()!=null)
            expectedBook.setExpectedBookGenre(expectedBookDTO.getExpectedBookGenre());
        expectedBookRepositories.save(expectedBook);
    }
    @Transactional(readOnly = true)
    public ExpectedBookDTO findOneBook(UUID id){
        ExpectedBook expectedBook = expectedBookRepositories.findByExpectedBookId(id)
                .orElseThrow(()->new ExpectedBookNotFoundException("The selected book was not found."));
        return toExpectedBookDTO(expectedBook);
    }
    @Transactional(readOnly = true)
    public List<ExpectedBookDTO> findAll(Pageable pageable){
        List<ExpectedBookDTO> expectedBookDTOs = expectedBookRepositories
                .findAll(pageable).map(this::toExpectedBookDTO).toList();
        if (expectedBookDTOs.isEmpty())
            throw new ExpectedBooksNotFoundException("The selected books were not found.");
        return expectedBookDTOs;
    }

    private ExpectedBook toExpectedBookEntity(ExpectedBookDTO expectedBookDTO){
        return modelMapper.map(expectedBookDTO, ExpectedBook.class);
    }
    private ExpectedBookDTO toExpectedBookDTO(ExpectedBook expectedBook){
        return modelMapper.map(expectedBook, ExpectedBookDTO.class);
    }

}
