package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public interface ICurvePointService {

    List<CurvePoint> findAll();

    CurvePoint save(CurvePoint curvePoint);

    CurvePoint findOne(int id);

    CurvePoint update(int id, CurvePoint curvePoint);

    void delete(int id);

}
