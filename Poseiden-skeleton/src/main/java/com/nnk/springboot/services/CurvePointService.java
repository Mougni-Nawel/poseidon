package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService implements ICurvePointService{

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Override
    public List<CurvePoint> findAll(){

        return curvePointRepository.findAll();

    }

    @Transactional
    @Override
    public CurvePoint save(CurvePoint curvePoint){

        if(curvePoint.getId() != 0){

            return curvePointRepository.save(curvePoint);

        }

        // todo: add exception
        return null;

    }

    @Override
    public CurvePoint findOne(int id){

        Optional<CurvePoint> curvePoint = curvePointRepository.findById(id);

        if(curvePoint.isPresent()){

            return curvePoint.get();

        }

        // todo: add exception

        return null;

    }

    @Transactional
    @Override
    public CurvePoint update(int id, CurvePoint curvePoint){

        CurvePoint existCurvePoint = this.findOne(id);

        if(existCurvePoint.getTerm() != curvePoint.getTerm()){
            existCurvePoint.setTerm(curvePoint.getTerm());
        }
        if(existCurvePoint.getValue() != curvePoint.getValue()){
            existCurvePoint.setValue(curvePoint.getValue());
        }

        CurvePoint curvePointUpdated = curvePointRepository.save(existCurvePoint);
        return curvePointUpdated;

    }

    @Override
    public void delete(int id){

        CurvePoint deletedCurvePoint = this.findOne(id);

        curvePointRepository.delete(deletedCurvePoint);

        // todo : see exceptions

    }

}
