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

    private Parameter currentParameter;
    private final ParameterRepository parameterRepository;

    public ParameterServiceImpl(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public Parameter getApplicationParameters() {
        if (this.currentParameter != null) {
            return this.currentParameter;
        }
        List<Parameter> allParameters = this.parameterRepository.findAll();
        if (allParameters.isEmpty()) {
            // the database does not have parameters
            return new Parameter(0L, 5, 5, 50, 1, 3);
        } else {
            Parameter best = allParameters.get(0);
            for (Parameter param: allParameters) {
                if (param.getId() > best.getId()) {
                    best = param;
                }
            }
            this.currentParameter = best;
            return this.currentParameter;
        }
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
        this.currentParameter = parameter;
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
