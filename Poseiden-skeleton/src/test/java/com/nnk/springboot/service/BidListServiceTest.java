package com.nnk.springboot.service;

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;


//@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    private BidList bidList1;
    private BidList bidList2;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        bidList1 = new BidList("Account1", "Type1", 100.0);
        bidList1.setBidListId(1);
        bidList2 = new BidList("Account2", "Type2", 200.0);


    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindAll(){

        List<BidList> bidLists = Arrays.asList(bidList1,bidList2);

        when(bidListRepository.findAll()).thenReturn(bidLists);

        List<BidList> result = bidListService.findAll();

        assertEquals(bidLists, result);

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testSave() {

        when(bidListRepository.save(any())).thenReturn(bidList1);

        BidList result = bidListService.save(bidList1);

        assertEquals(bidList1, result);

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindOne(){


        BidList bidList3 = new BidList("Account3", "Type3", 300.0);
        bidList3.setBidListId(3);

        when(bidListRepository.findById(bidList3.getBidListId())).thenReturn(Optional.ofNullable(bidList3));

        BidList result = bidListService.findOne(bidList3.getBidListId());

        Assert.assertEquals(bidList3, result);

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdate() {

        BidList existingBidList = new BidList("existingAccount", "existingType", 300.0);
        existingBidList.setBidListId(4);
        BidList newBidList = new BidList("newAccount", "newType", 300.0);

        when(bidListRepository.findById(existingBidList.getBidListId())).thenReturn(Optional.of(existingBidList));
        when(bidListRepository.save(any())).thenReturn(newBidList);

        BidList result = bidListService.update(existingBidList.getBidListId(), newBidList);

        assertEquals(newBidList.getAccount(), result.getAccount());

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDelete() {


        BidList bidList3 = new BidList("Account3", "Type3", 300.0);
        bidList3.setBidListId(3);

        when(bidListRepository.findById(bidList3.getBidListId())).thenReturn(Optional.ofNullable(bidList3));

        bidListService.delete(bidList3.getBidListId());

        verify(bidListRepository, times(1)).findById(bidList3.getBidListId());
        verify(bidListRepository, times(1)).delete(bidList3);

    }
}
