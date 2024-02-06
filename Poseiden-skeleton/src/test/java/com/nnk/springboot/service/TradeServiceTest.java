package com.nnk.springboot.service;

import com.nnk.springboot.domain.*;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.TradeService;
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
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    private Trade trade1;
    private Trade trade2;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        trade1 = new Trade();
        trade1.setTradeId(1);
        trade1.setBook("testSand");
        trade1.setBenchmark("testFitch");
        trade1.setAccount("test");
        trade2 = new Trade();
        trade2.setTradeId(2);
        trade2.setBook("testSand2");
        trade2.setBenchmark("testFitch2");
        trade2.setAccount("test");

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindAll(){

        List<Trade> tradeList = Arrays.asList(trade1,trade2);

        when(tradeRepository.findAll()).thenReturn(tradeList);

        List<Trade> result = tradeService.findAll();

        Assert.assertEquals(tradeList, result);

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testSave() {

        when(tradeRepository.save(trade1)).thenReturn(trade1);

        Trade result = tradeService.save(new Trade());

        assertEquals(trade1, result);

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFindOne(){


        Trade trade3 = new Trade();
        trade3.setTradeId(3);

        when(tradeRepository.findById(trade3.getTradeId())).thenReturn(Optional.ofNullable(trade3));

        Trade result = tradeService.findOne(trade3.getTradeId());

        Assert.assertEquals(trade3, result);

    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdate() {

        Trade existingTrade = new Trade();
        existingTrade.setTradeId(4);
        existingTrade.setBook("TestBook");
        Trade newTrade = new Trade();
        newTrade.setBook("NewValueBook");

        when(tradeRepository.findById(existingTrade.getTradeId())).thenReturn(Optional.of(existingTrade));
        when(tradeRepository.save(any())).thenReturn(newTrade);

        Trade result = tradeService.update(existingTrade.getTradeId(), newTrade);

        assertEquals(newTrade.getBook(), result.getBook());

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDelete() {


        Trade trade3 = new Trade();
        trade3.setTradeId(3);

        when(tradeRepository.findById(trade3.getTradeId())).thenReturn(Optional.ofNullable(trade3));

        tradeService.delete(trade3.getTradeId());

        verify(tradeRepository, times(1)).findById(trade3.getTradeId());
        verify(tradeRepository, times(1)).delete(trade3);

    }


}
