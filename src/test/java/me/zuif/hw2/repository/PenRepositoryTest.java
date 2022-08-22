package me.zuif.hw2.repository;


import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.repository.cache.PenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;

class PenRepositoryTest {

    private PenRepository target;

    private Pen pen;

    @BeforeEach
    void setUp() {
        final Random random = new Random();
        target = PenRepository.getInstance();
        pen = new Pen(
                "Title-" + random.nextInt(1000),
                random.nextInt(500),
                random.nextDouble(1000.0),
                PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE
        );
    }

    @Test
    void save() {
        target.save(pen);
        final List<Pen> pens = target.findAll();
        Assertions.assertEquals(1, pens.size());
        Assertions.assertEquals(pens.get(0).getId(), pen.getId());
    }

    @Test
    void save_putNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void saveAll_singlePen() {
        target.saveAll(Collections.singletonList(pen));
        final List<Pen> pens = target.findAll();
        Assertions.assertEquals(1, pens.size());
        Assertions.assertEquals(pens.get(0).getId(), pen.getId());
    }


    @Test
    void saveAll_noPen() {
        target.saveAll(Collections.emptyList());
        final List<Pen> pens = target.findAll();
        Assertions.assertEquals(0, pens.size());
    }

    @Test
    void saveAll_manyPens() {
        final Pen otherPen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.saveAll(List.of(pen, otherPen));
        final List<Pen> pens = target.findAll();
        Assertions.assertEquals(2, pens.size());
        Assertions.assertEquals(pens.get(0).getId(), pen.getId());
        Assertions.assertEquals(pens.get(1).getId(), otherPen.getId());
    }

    @Test
    void saveAll_hasDuplicates() {
        final List<Pen> pens = new ArrayList<>();
        pens.add(pen);
        pens.add(pen);
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.saveAll(pens));
    }

    @Test
    void saveAll_hasNull() {
        final List<Pen> pens = new ArrayList<>();
        pens.add(pen);
        pens.add(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.saveAll(pens));
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void update() {
        final String newTitle = "New title";
        target.save(pen);
        pen.setTitle(newTitle);

        final boolean result = target.update(pen);

        Assertions.assertTrue(result);
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(newTitle, actualResult.get(0).getTitle());
        Assertions.assertEquals(pen.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(pen.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void update_noPen() {
        target.save(pen);
        final Pen noPen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        final boolean result = target.update(noPen);

        Assertions.assertFalse(result);
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(pen.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(pen.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void update_Argument_Captor() {
        PenRepository target = mock(PenRepository.class);
        final Pen pen2 = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.update(pen2);
        ArgumentCaptor<Pen> penArgumentCaptor = ArgumentCaptor.forClass(Pen.class);
        Mockito.verify(target, times(1)).update(penArgumentCaptor.capture());

        Assertions.assertEquals(penArgumentCaptor.getAllValues().size(), 1);
        Pen capturedArgument = penArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedArgument.getId());
        Assertions.assertEquals(capturedArgument.getId(), pen2.getId());

    }

    @Test
    void delete() {
        target.save(pen);
        final boolean result = target.delete(pen.getId());
        Assertions.assertTrue(result);
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void delete_noPen() {
        target.save(pen);
        final Pen noPen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        final boolean result = target.delete(noPen.getId());
        Assertions.assertFalse(result);
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll() {
        target.save(pen);
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll_noPens() {
        final List<Pen> actualResult = target.findAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void findById() {
        target.save(pen);
        final Optional<Pen> optionalPen = target.findById(pen.getId());
        Assertions.assertTrue(optionalPen.isPresent());
        final Pen actualPen = optionalPen.get();
        Assertions.assertEquals(pen.getId(), actualPen.getId());
    }


    @Test
    void findById_Argument_Matcher() {
        PenRepository target = mock(PenRepository.class);
        when(target.findById(Mockito.anyString())).thenReturn(Optional.of(pen));
        Assertions.assertEquals(pen.getId(), target.findById("1").get().getId());
        Assertions.assertEquals(pen.getId(), target.findById("2").get().getId());
    }

    @Test
    void findById_noPen() {
        target.save(pen);
        final Pen noPen = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        final Optional<Pen> optionalPen = target.findById(noPen.getId());
        Assertions.assertFalse(optionalPen.isPresent());


    }

    @Test
    void findById_verify() {
        PenRepository target = mock(PenRepository.class);
        final Pen pen2 = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.findById(pen2.getId());
        Mockito.verify(target).findById(pen2.getId());
    }

    @Test
    void findById_verifyTimes() {
        PenRepository target = mock(PenRepository.class);
        final Pen pen2 = new Pen("Title", 500, 1000.0, PenBrand.PARKER,
                PenType.BALLPOINT,
                PenColor.DARK_BLUE);
        target.findById(pen2.getId());
        Mockito.verify(target, times(1)).findById(pen2.getId());
    }
}