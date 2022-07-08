package me.zuif.hw2.service;


import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.repository.pen.PenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PenServiceTest {
    private PenService target;
    private PenRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PenRepository.class);
        target = new PenService(repository);
    }

    @Test
    void createAndSavePens_negativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSavePens(-1));
    }

    @Test
    void createAndSavePens_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSavePens(0));
    }

    @Test
    void createAndSavePens() {
        target.createAndSavePens(2);
        Mockito.verify(repository).saveAll(Mockito.anyList());
    }

    @Test
    void getAll() {
        target.getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void getAll_Argument_Matcher() {
        target.getAll();
        PenService target = mock(PenService.class);
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(target.getAll()).thenReturn(Arrays.asList(pen));
        Assertions.assertEquals(pen.getId(), target.getAll().stream().findFirst().get().getId());

    }

    @Test
    void printAll() {
        target.printAll();
        Mockito.verify(repository).getAll();
    }


    @Test
    void savePen() {
        final Pen pen = new Pen("Title", 100, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.savePen(pen);

        ArgumentCaptor<Pen> argument = ArgumentCaptor.forClass(Pen.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void savePen_verifyTimes() {
        final Pen pen = new Pen("Title", 100, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.savePen(pen);

        ArgumentCaptor<Pen> argument = ArgumentCaptor.forClass(Pen.class);
        Mockito.verify(repository, Mockito.times(1)).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void savePen_zeroCount() {
        final Pen pen = new Pen("Title", 0, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.savePen(pen);

        ArgumentCaptor<Pen> argument = ArgumentCaptor.forClass(Pen.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
        Assertions.assertEquals(-1, argument.getValue().getCount());
    }


}