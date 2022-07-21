package me.zuif.hw2.repository;


import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.model.tea.TeaBrand;
import me.zuif.hw2.model.tea.TeaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;

class TeaRepositoryTest {

    private TeaRepository target;

    private Tea tea;

    @BeforeEach
    void setUp() {
        final Random random = new Random();
        target = new TeaRepository();
        tea = new Tea(
                "Title-" + random.nextInt(1000),
                random.nextInt(500),
                random.nextDouble(1000.0),
                TeaBrand.LIPTON, TeaType.BLACK
        );
    }

    @Test
    void save() {
        target.save(tea);
        final List<Tea> teas = target.findAll();
        Assertions.assertEquals(1, teas.size());
        Assertions.assertEquals(teas.get(0).getId(), tea.getId());
    }

    @Test
    void save_putNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void saveAll_singleTea() {
        target.saveAll(Collections.singletonList(tea));
        final List<Tea> teas = target.findAll();
        Assertions.assertEquals(1, teas.size());
        Assertions.assertEquals(teas.get(0).getId(), tea.getId());
    }


    @Test
    void saveAll_noTea() {
        target.saveAll(Collections.emptyList());
        final List<Tea> teas = target.findAll();
        Assertions.assertEquals(0, teas.size());
    }

    @Test
    void saveAll_manyTeas() {
        final Tea otherTea = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        target.saveAll(List.of(tea, otherTea));
        final List<Tea> teas = target.findAll();
        Assertions.assertEquals(2, teas.size());
        Assertions.assertEquals(teas.get(0).getId(), tea.getId());
        Assertions.assertEquals(teas.get(1).getId(), otherTea.getId());
    }

    @Test
    void saveAll_hasDuplicates() {
        final List<Tea> teas = new ArrayList<>();
        teas.add(tea);
        teas.add(tea);
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.saveAll(teas));
    }

    @Test
    void saveAll_hasNull() {
        final List<Tea> teas = new ArrayList<>();
        teas.add(tea);
        teas.add(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.saveAll(teas));
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void update() {
        final String newTitle = "New title";
        target.save(tea);
        tea.setTitle(newTitle);

        final boolean result = target.update(tea);

        Assertions.assertTrue(result);
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(newTitle, actualResult.get(0).getTitle());
        Assertions.assertEquals(tea.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(tea.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void update_noTea() {
        target.save(tea);
        final Tea noTea = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        final boolean result = target.update(noTea);

        Assertions.assertFalse(result);
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(tea.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(tea.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void update_Argument_Captor() {
        TeaRepository target = mock(TeaRepository.class);
        final Tea tea2 = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        target.update(tea2);
        ArgumentCaptor<Tea> teaArgumentCaptor = ArgumentCaptor.forClass(Tea.class);
        Mockito.verify(target, times(1)).update(teaArgumentCaptor.capture());

        Assertions.assertEquals(teaArgumentCaptor.getAllValues().size(), 1);
        Tea capturedArgument = teaArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedArgument.getId());
        Assertions.assertEquals(capturedArgument.getId(), tea2.getId());

    }

    @Test
    void delete() {
        target.save(tea);
        final boolean result = target.delete(tea.getId());
        Assertions.assertTrue(result);
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void delete_noTea() {
        target.save(tea);
        final Tea noTea = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        final boolean result = target.delete(noTea.getId());
        Assertions.assertFalse(result);
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll() {
        target.save(tea);
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll_noTeas() {
        final List<Tea> actualResult = target.findAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void findById() {
        target.save(tea);
        final Optional<Tea> optionalTea = target.findById(tea.getId());
        Assertions.assertTrue(optionalTea.isPresent());
        final Tea actualTea = optionalTea.get();
        Assertions.assertEquals(tea.getId(), actualTea.getId());
    }


    @Test
    void findById_Argument_Matcher() {
        TeaRepository target = mock(TeaRepository.class);
        when(target.findById(Mockito.anyString())).thenReturn(Optional.of(tea));
        Assertions.assertEquals(tea.getId(), target.findById("1").get().getId());
        Assertions.assertEquals(tea.getId(), target.findById("2").get().getId());
    }

    @Test
    void findById_noTea() {
        target.save(tea);
        final Tea noTea = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        final Optional<Tea> optionalTea = target.findById(noTea.getId());
        Assertions.assertFalse(optionalTea.isPresent());


    }

    @Test
    void findById_verify() {
        TeaRepository target = mock(TeaRepository.class);
        final Tea tea2 = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        target.findById(tea2.getId());
        Mockito.verify(target).findById(tea2.getId());
    }

    @Test
    void findById_verifyTimes() {
        TeaRepository target = mock(TeaRepository.class);
        final Tea tea2 = new Tea("Title", 500, 1000.0, TeaBrand.LIPTON, TeaType.BLACK);
        target.findById(tea2.getId());
        Mockito.verify(target, times(1)).findById(tea2.getId());
    }
}