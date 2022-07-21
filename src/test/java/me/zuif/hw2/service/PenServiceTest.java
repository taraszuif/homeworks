package me.zuif.hw2.service;


import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.repository.PenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

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
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveProducts(-1));
    }

    @Test
    void createAndSavePens_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveProducts(0));
    }

    @Test
    void createAndSavePens() {
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
        PenService target = mock(PenService.class);
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(target.findAll()).thenReturn(Arrays.asList(pen));
        Assertions.assertEquals(pen.getId(), target.findAll().stream().findFirst().get().getId());

    }

    //filter
    @Test
    void getAllByBrand_HasOtherBrand() {
        Pen parkerPen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        Pen noParkerPen = new Pen("Title", 500, 1000.0, PenBrand.PILOT,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findAll()).thenReturn(List.of(parkerPen, noParkerPen));
        Assertions.assertTrue(target.getAllByBrand(PenBrand.PARKER).
                stream().filter(pen -> pen.getBrand() != PenBrand.PARKER).collect(Collectors.toList()).size() == 0);
    }

    @Test
    void getAllByBrand_HasTargetBrand() {
        Pen parkerPen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        Pen noParkerPen = new Pen("Title", 500, 1000.0, PenBrand.PILOT,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findAll()).thenReturn(List.of(parkerPen, noParkerPen));
        Assertions.assertFalse(target.getAllByBrand(PenBrand.PARKER).size() == 0);
    }

    @Test
    void mapToString_Equals() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        Assertions.assertEquals(target.mapToString(pen.getId()), pen.toString());
    }

    @Test
    void mapToString_NonEquals() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        Pen otherPen = new Pen("Title1", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        Assertions.assertNotEquals(target.mapToString(pen.getId()), otherPen.toString());
    }

    @Test
    void deleteIfPresent_present() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        target.deleteIfPresent(pen.getId());
        Mockito.verify(repository).delete(pen.getId());
    }

    @Test
    void deleteIfPresent_noPresent() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);

        when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        target.deleteIfPresent("1");
        Mockito.verify(repository, never()).delete(pen.getId());
    }

    @Test
    void findByIdOrGetFirst() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        Assertions.assertEquals(target.findByIdOrGetFirst(pen.getId()), Optional.of(pen));
    }

    @Test
    void findByIdOrGetFirst_noPen() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        Pen otherPen = new Pen("Title1", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        when(repository.findAll()).thenReturn(List.of(otherPen, pen));
        Assertions.assertEquals(target.findByIdOrGetFirst("1"), Optional.of(otherPen));
    }

    @Test
    void findByIdOrGetAny() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));

        Assertions.assertEquals(target.findByIdOrGetAny(pen.getId()), pen);

    }

    @Test
    void findByIdOrGetAny_noPen() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        Pen otherPen = new Pen("Title1", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        when(repository.findAll()).thenReturn(List.of(otherPen, pen));
        Assertions.assertTrue(target.findByIdOrGetAny("1") != null);

    }

    @Test
    void findByIdOrThrow() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        Assertions.assertEquals(target.findByIdOrThrow(pen.getId()), pen);
    }

    @Test
    void findByIdOrThrow_noPen() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        Pen otherPen = new Pen("Title1", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        when(repository.findAll()).thenReturn(List.of(otherPen, pen));
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.findByIdOrThrow("1"));

    }

    @Test
    void findByIdOrGetRandom() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        Assertions.assertEquals(target.findByIdOrGetRandom(pen.getId()), pen);
    }

    @Test
    void findByIdOrGetRandom_noPen() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);

        when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertNotEquals(target.findByIdOrGetRandom(pen.getId()), pen);

    }

    @Test
    void updateOrSave_present() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        target.updateOrSave(pen);
        verify(repository).update(pen);

    }

    @Test
    void updateOrSave_noPresent() {
        Pen pen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        target.updateOrSave(pen);
        verify(repository).save(pen);

    }

    @Test
    void printAll() {
        target.printAll();
        Mockito.verify(repository).findAll();
    }


    @Test
    void savePen() {
        final Pen pen = new Pen("Title", 100, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.save(pen);

        ArgumentCaptor<Pen> argument = ArgumentCaptor.forClass(Pen.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void savePen_verifyTimes() {
        final Pen pen = new Pen("Title", 100, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.save(pen);

        ArgumentCaptor<Pen> argument = ArgumentCaptor.forClass(Pen.class);
        Mockito.verify(repository, Mockito.times(1)).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void savePen_zeroCount() {
        final Pen pen = new Pen("Title", 0, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.save(pen);

        ArgumentCaptor<Pen> argument = ArgumentCaptor.forClass(Pen.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
        Assertions.assertEquals(-1, argument.getValue().getCount());
    }


}