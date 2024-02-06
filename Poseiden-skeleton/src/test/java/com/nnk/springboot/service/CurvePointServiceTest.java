package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.services.ICurvePointService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;

    private CurvePoint curvePoint1;
    private CurvePoint curvePoint2;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        curvePoint1 = new CurvePoint(1, 2.2, 100.0);
        curvePoint1.setId(1);
        curvePoint2 = new CurvePoint(2, 4.2, 200.0);
        curvePoint2.setId(2);


    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindAll(){

        List<CurvePoint> curvePointList = Arrays.asList(curvePoint1,curvePoint2);

        when(curvePointRepository.findAll()).thenReturn(curvePointList);

        List<CurvePoint> result = curvePointService.findAll();

        Assert.assertEquals(curvePointList, result);

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testSave() {

        when(curvePointRepository.save(curvePoint1)).thenReturn(curvePoint1);

        CurvePoint result = curvePointService.save(new CurvePoint());

        assertEquals(curvePoint1, result);

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindOne(){

        //List<CurvePoint> curvePointList = Arrays.asList(curvePoint1,curvePoint2);
        CurvePoint curvePoint3 = new CurvePoint(3, 4.2, 200.0);
        curvePoint3.setId(3);

        System.out.println("ID : "+curvePoint3.getId());
        when(curvePointRepository.findById(curvePoint3.getId())).thenReturn(Optional.ofNullable(curvePoint3));

        CurvePoint result = curvePointService.findOne(curvePoint3.getId());

        Assert.assertEquals(curvePoint3, result);

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdate() {

        CurvePoint existingCurvePoint = new CurvePoint(4, 2.2, 200.0);
        existingCurvePoint.setId(4);
        CurvePoint newCurvePoint = new CurvePoint(2, 4.2, 900.0);

        when(curvePointRepository.findById(existingCurvePoint.getId())).thenReturn(Optional.of(existingCurvePoint));
        when(curvePointRepository.save(any())).thenReturn(newCurvePoint);

        CurvePoint result = curvePointService.update(existingCurvePoint.getId(), newCurvePoint);

        assertEquals(newCurvePoint.getTerm(), result.getTerm(), 0.001);

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDelete() {

        CurvePoint curvePoint3 = new CurvePoint(3, 4.2, 200.0);
        curvePoint3.setId(3);

        System.out.println("ID : "+curvePoint3.getId());
        when(curvePointRepository.findById(curvePoint3.getId())).thenReturn(Optional.ofNullable(curvePoint3));

        curvePointService.delete(curvePoint3.getId());

        verify(curvePointRepository, times(1)).findById(curvePoint3.getId());
        verify(curvePointRepository, times(1)).delete(curvePoint3);

    }

}
