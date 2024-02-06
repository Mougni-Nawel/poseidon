package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.RatingService;
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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating1;
    private Rating rating2;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        rating1 = new Rating();
        rating1.setId(1);
        rating1.setSandPRating("testSand");
        rating1.setFitchRating("testFitch");
        rating1.setOrderNumber(1);
        rating2 = new Rating();
        rating2.setId(2);
        rating2.setSandPRating("testSand2");
        rating2.setFitchRating("testFitch2");
        rating2.setOrderNumber(2);


    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindAll(){

        List<Rating> ratingList = Arrays.asList(rating1,rating2);

        when(ratingRepository.findAll()).thenReturn(ratingList);

        List<Rating> result = ratingService.findAll();

        Assert.assertEquals(ratingList, result);

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testSave() {

        when(ratingRepository.save(rating1)).thenReturn(rating1);

        Rating result = ratingService.save(new Rating());

        assertEquals(rating1, result);

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindOne(){


        Rating rating3 = new Rating();
        rating3.setId(1);

        when(ratingRepository.findById(rating3.getId())).thenReturn(Optional.ofNullable(rating3));

        Rating result = ratingService.findOne(rating3.getId());

        Assert.assertEquals(rating3, result);

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdate() {

        Rating existingRating = new Rating();
        existingRating.setId(4);
        existingRating.setFitchRating("TestFitch");
        Rating newRating = new Rating();
        newRating.setFitchRating("NewValueFitch");

        when(ratingRepository.findById(existingRating.getId())).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any())).thenReturn(newRating);

        Rating result = ratingService.update(existingRating.getId(), newRating);

        assertEquals(newRating.getFitchRating(), result.getFitchRating());

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDelete() {


        Rating rating3 = new Rating();
        rating3.setId(1);

        when(ratingRepository.findById(rating3.getId())).thenReturn(Optional.ofNullable(rating3));

        ratingService.delete(rating3.getId());

        verify(ratingRepository, times(1)).findById(rating3.getId());
        verify(ratingRepository, times(1)).delete(rating3);

    }

}
