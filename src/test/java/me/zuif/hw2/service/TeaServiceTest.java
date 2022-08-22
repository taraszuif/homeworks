package me.zuif.hw2.service;


import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.model.tea.TeaBrand;
import me.zuif.hw2.model.tea.TeaType;
import me.zuif.hw2.repository.cache.TeaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeaServiceTest {
    private TeaService target;
    private TeaRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TeaRepository.class);
        target = TeaService.getInstance();
    }

    @Test
    void createAndSaveTeas_negativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveProducts(-1));
    }

    @Test
    void createAndSaveTeas_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveProducts(0));
    }

    @Test
    void createAndSaveTeas() {
        target.createAndSaveProducts(2);
        Mockito.verify(repository).saveAll(Mockito.anyList());
    }

    @Test
    void getAll() {
        target.findAll();
        Mockito.verify(repository).findAll();
    }

    @Test
    void getAll_Argument_Matcher() {
        target.findAll();
        TeaService target = mock(TeaService.class);
        Tea tea = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        when(target.findAll()).thenReturn(List.of(tea));
        Assertions.assertEquals(tea.getId(), target.findAll().stream().findFirst().get().getId());

    }

    @Test
    void printAll() {
        target.printAll();
        Mockito.verify(repository).findAll();
    }


    @Test
    void saveTea() {
        final Tea tea = new Tea("Title", 100, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        target.save(tea);

        ArgumentCaptor<Tea> argument = ArgumentCaptor.forClass(Tea.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void saveTea_verifyTimes() {
        final Tea tea = new Tea("Title", 100, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        target.save(tea);

        ArgumentCaptor<Tea> argument = ArgumentCaptor.forClass(Tea.class);
        Mockito.verify(repository, Mockito.times(1)).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void saveTea_zeroCount() {
        final Tea tea = new Tea("Title", 0, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        target.save(tea);

        ArgumentCaptor<Tea> argument = ArgumentCaptor.forClass(Tea.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
        Assertions.assertEquals(-1, argument.getValue().getCount());
    }


}