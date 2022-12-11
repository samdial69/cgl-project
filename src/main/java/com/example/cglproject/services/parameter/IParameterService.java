package com.example.cglproject.services.parameter;

import com.example.cglproject.models.Parameter;

import java.util.List;
import java.util.Optional;


public interface IParameterService {

    Optional<Parameter> findById(Long id);
    List<Parameter> findAll();

    Parameter save(Parameter parameter);
    Parameter update(Long id , Parameter parameter);
    boolean delete(Long id);

}
