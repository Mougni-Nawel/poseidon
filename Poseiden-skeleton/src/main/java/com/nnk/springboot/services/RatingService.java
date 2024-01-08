package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> findAll(){

        return ratingRepository.findAll();

    }

    public Rating save(Rating rating){

        if(rating.getId() != 0){

            return ratingRepository.save(rating);

        }

        return null;

    }

    public Rating findOne(int id){

        Optional<Rating> rating = ratingRepository.findById(id);

        if(rating.isPresent()){

            return rating.get();

        }

        return null;

    }

    public Rating update(int id, Rating rating){

        Rating existRating = this.findOne(id);

        if(existRating.getMoodysRating() != rating.getFitchRating()){
            existRating.setMoodysRating(rating.getMoodysRating());
        }
        if(existRating.getFitchRating() != rating.getFitchRating()){
            existRating.setFitchRating(rating.getFitchRating());
        }
        if(existRating.getSandPRating() != rating.getSandPRating()){
            existRating.setSandPRating(rating.getSandPRating());
        }
        if(existRating.getOrderNumber() != rating.getOrderNumber()){
            existRating.setOrderNumber(rating.getOrderNumber());
        }

        Rating ratingUpdated = ratingRepository.save(existRating);
        return ratingUpdated;

    }

    public void delete(int id) {

        Rating deletedRating = this.findOne(id);

        ratingRepository.delete(deletedRating);

    }

}
