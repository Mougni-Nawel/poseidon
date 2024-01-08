package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.h2.bnf.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleName> findAll(){

        return ruleNameRepository.findAll();

    }

    public RuleName save(RuleName ruleName){

        if(ruleName.getId() != 0){

            return ruleNameRepository.save(ruleName);

        }

        return null;

    }

    public RuleName findOne(int id) {

        Optional<RuleName> ruleName = ruleNameRepository.findById(id);

        if(ruleName.isPresent()){

            return ruleName.get();

        }

        return null;

    }

    public RuleName update(int id, RuleName ruleName){

        RuleName existRuleName = this.findOne(id);

        if(existRuleName.getName() != ruleName.getName()){
            existRuleName.setName(ruleName.getName());
        }
        if(existRuleName.getJson() != ruleName.getJson()){
            existRuleName.setJson(ruleName.getJson());
        }
        if(existRuleName.getDescription() != ruleName.getDescription()){
            existRuleName.setDescription(ruleName.getDescription());
        }
        if(existRuleName.getTemplate() != ruleName.getTemplate()){
            existRuleName.setTemplate(ruleName.getTemplate());
        }
        if(existRuleName.getSqlPart() != ruleName.getSqlPart()){
            existRuleName.setSqlPart(ruleName.getSqlPart());
        }
        if(existRuleName.getSqlStr() != ruleName.getSqlStr()){
            existRuleName.setSqlStr(ruleName.getSqlStr());
        }

        RuleName ruleNameUpdated = ruleNameRepository.save(existRuleName);

        return ruleNameUpdated;

    }

    public void delete(int id){

        RuleName deletedRuleName = this.findOne(id);

        ruleNameRepository.delete(deletedRuleName);

    }

}
