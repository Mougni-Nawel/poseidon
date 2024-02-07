package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.RuleNameService;
import org.junit.Assert;
import org.junit.Rule;
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
public class RuleServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    private RuleName ruleName1;
    private RuleName ruleName2;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        ruleName1 = new RuleName();
        ruleName1.setId(1);
        ruleName1.setName("testSand");
        ruleName1.setJson("testFitch");
        ruleName1.setSqlStr("test");
        ruleName2 = new RuleName();
        ruleName2.setId(2);
        ruleName2.setJson("testSand2");
        ruleName2.setName("testFitch2");
        ruleName2.setSqlStr("test");

    }

    @Test
    @WithMockUser
    public void testFindAll(){

        List<RuleName> ruleNameList = Arrays.asList(ruleName1,ruleName2);

        when(ruleNameRepository.findAll()).thenReturn(ruleNameList);

        List<RuleName> result = ruleNameService.findAll();

        Assert.assertEquals(ruleNameList, result);

    }

    @Test
    @WithMockUser
    public void testSave() {

        when(ruleNameRepository.save(ruleName1)).thenReturn(ruleName1);

        RuleName result = ruleNameService.save(new RuleName());

        assertEquals(ruleName1, result);

    }

    @Test
    @WithMockUser
    public void testFindOne(){


        RuleName ruleName3 = new RuleName();
        ruleName3.setId(3);

        when(ruleNameRepository.findById(ruleName3.getId())).thenReturn(Optional.ofNullable(ruleName3));

        RuleName result = ruleNameService.findOne(ruleName3.getId());

        Assert.assertEquals(ruleName3, result);

    }

    @Test
    @WithMockUser
    public void testUpdate() {

        RuleName existingRuleName = new RuleName();
        existingRuleName.setId(4);
        existingRuleName.setDescription("TestDescription");
        RuleName newRuleName = new RuleName();
        newRuleName.setDescription("NewValueDescription");

        when(ruleNameRepository.findById(existingRuleName.getId())).thenReturn(Optional.of(existingRuleName));
        when(ruleNameRepository.save(any())).thenReturn(newRuleName);

        RuleName result = ruleNameService.update(existingRuleName.getId(), newRuleName);

        assertEquals(newRuleName.getDescription(), result.getDescription());

    }

    @Test
    @WithMockUser
    public void testDelete() {


        RuleName ruleName3 = new RuleName();
        ruleName3.setId(3);

        when(ruleNameRepository.findById(ruleName3.getId())).thenReturn(Optional.ofNullable(ruleName3));

        ruleNameService.delete(ruleName3.getId());

        verify(ruleNameRepository, times(1)).findById(ruleName3.getId());
        verify(ruleNameRepository, times(1)).delete(ruleName3);

    }

}
