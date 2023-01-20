package com.example.cglproject.services.parameter;

import com.example.cglproject.models.Parameter;
import com.example.cglproject.repositories.ParameterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ParameterServiceImpl implements IParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterServiceImpl(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
//        List<Parameter> allParameters = this.parameterRepository.findAll();
//
//        if (allParameters.isEmpty()) {
//            this.currentParameter = new Parameter(0L, 5,5,50,1,3);
//            this.parameterRepository.save(currentParameter);
//        }
    }

    @Override
    public Parameter getApplicationParameters() {
        Optional<Parameter> parameter = this.parameterRepository.findById(1L);
        if (parameter.isEmpty()) {
            log.error("No parameter found");
            return null;
        }
        return parameter.get();
//        if (this.currentParameter != null) {
//            return this.currentParameter;
//        }
//        List<Parameter> allParameters = this.parameterRepository.findAll();
//
//        /*if (!allParameters.isEmpty()) {
//            //return new Parameter(0L, 5,5,50,1,3);
//            //return new Parameter();
//        } */
//
//        if(!allParameters.isEmpty()){
//            Parameter best = allParameters.get(0);
//            for (Parameter param: allParameters) {
//                if (param.getId() > best.getId()) {
//                    best = param;
//                }
//            }
//            this.currentParameter = best;
//            return this.currentParameter;
    }

    @Override
    public Optional<Parameter> findById(Long id) {
        log.info("Fetching an parameter by id :{}",id);
        return this.parameterRepository.findById(id);
    }

    @Override
    public List<Parameter> findAll() {
        log.info("Fetching all parameters");
        return this.parameterRepository.findAll();
    }

    @Override
    public Parameter save(Parameter parameter) {
        log.info("Creating a new parameter : {}",parameter);
        return this.parameterRepository.save(parameter);
    }

    @Override
    public Parameter update(Long id, Parameter parameter) {

        Optional<Parameter> currentParam = this.findById(id);
        if(currentParam.isPresent()){
            log.info("Updating parameter by id : {}",id);
            parameter.setId(currentParam.get().getId());
            return this.parameterRepository.save(parameter);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {

        Optional<Parameter> currentParam = this.findById(id);
        if(currentParam.isPresent()){
            log.info("Deleting parameter by id : {}",id);
            this.parameterRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
