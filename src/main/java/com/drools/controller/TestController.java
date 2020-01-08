package com.drools.controller;

import com.drools.demo01.entity.Person;
import com.drools.model.Address;
import com.drools.model.fact.AddressCheckResult;
import com.drools.service.ReloadDroolsRulesService;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;


@RequestMapping("/test")
@Controller
public class TestController {

    @Resource
    private ReloadDroolsRulesService rules;

    @ResponseBody
    @RequestMapping("/address")
    public void test(int num){
        Address address = new Address();
        address.setPostcode(generateRandom(num));
        KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

        AddressCheckResult result = new AddressCheckResult();
        kieSession.insert(address);
        kieSession.insert(result);
        int ruleFiredCount = kieSession.fireAllRules();
        kieSession.destroy();
        System.out.println("触发了" + ruleFiredCount + "条规则");

        if(result.isPostCodeResult()){
            System.out.println("规则校验通过");
        }

    }

    @RequestMapping(value = "/jcexecl",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void test2(@RequestParam(value = "file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String rules = compiler.compile(ResourceFactory.newInputStreamResource(inputStream, "UTF-8"), "Sheet1");
        System.err.println("转化rule："+rules);
        KieFileSystem kieFileSystem = KieServices.Factory.get().newKieFileSystem();
        kieFileSystem.write("src/main/resources/rules/rule02.drl",rules.getBytes());
        KieBuilder kieBuilder = KieServices.Factory.get().newKieBuilder(kieFileSystem).buildAll();
        /*if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
            throw new Exception();
        }*/
        KieContainer kieContainer = KieServices.Factory.get().newKieContainer(KieServices.Factory.get().getRepository().getDefaultReleaseId());
        KieBase kBase = kieContainer.getKieBase();
        KieSession kieSession = kBase.newKieSession();
        Person person = new Person();
        person.setName("BBB");
        kieSession.insert(person);
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("drl之后："+person.toString());

    }


    /**
     * 从数据加载最新规则
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/reload")
    public String reload() throws IOException {
//        rules.reload();
        return "ok";
    }


    /**
     * 生成随机数
     * @param num
     * @return
     */
    public String generateRandom(int num) {
        String chars = "0123456789";
        StringBuffer number=new StringBuffer();
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            number=number.append(chars.charAt(rand));
        }
        return number.toString();
    }
}
