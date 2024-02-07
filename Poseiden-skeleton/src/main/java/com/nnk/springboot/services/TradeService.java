package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> findAll(){

        return tradeRepository.findAll();

    }

    public Trade save(Trade trade){

        return tradeRepository.save(trade);
    }

    public Trade findOne(int id){

        Optional<Trade> trade = tradeRepository.findById(id);

        if(trade.isPresent()){

            return trade.get();

        }

        return null;

    }

    public Trade update(int id, Trade trade){

        Trade existTrade = this.findOne(id);

        if(existTrade != trade){

            tradeRepository.save(trade);
            return trade;
        }

        return existTrade;


    }

    public void delete(int id){

        Trade deletedTrade = this.findOne(id);

        tradeRepository.delete(deletedTrade);

    }


}
