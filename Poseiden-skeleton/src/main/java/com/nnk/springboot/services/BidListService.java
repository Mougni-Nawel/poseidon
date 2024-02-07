package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BidListService {

    @Autowired
    BidListRepository bidListRepository;

    public List<BidList> findAll(){

        return bidListRepository.findAll();

    }

    @Transactional
    public BidList save(BidList bid){

        return bidListRepository.save(bid);

    }

    public BidList findOne(int id){

        Optional<BidList> bidList = bidListRepository.findById(id);

        if(bidList.isPresent()){
            return bidList.get();
        }

        return null;
    }

    @Transactional
    public BidList update(int id, BidList bidList){

        BidList existBidList = this.findOne(id);

        if(existBidList.getAccount() != bidList.getAccount()){
            existBidList.setAccount(bidList.getAccount());
        }
        if(existBidList.getType() != bidList.getType()){
            existBidList.setType(bidList.getType());
        }
        if(existBidList.getBidQuantity() != bidList.getBidQuantity()){
            existBidList.setBidQuantity(bidList.getBidQuantity());
        }

        BidList updatedBidList = bidListRepository.save(existBidList);
        return updatedBidList;
    }

    public void delete(int id){

        BidList deletedBidList = this.findOne(id);

        bidListRepository.delete(deletedBidList);

    }




}
