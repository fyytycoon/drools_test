package com.drools.demo01.drools;

import com.drools.demo01.entity.Lover;
import com.drools.demo01.entity.Person;
import com.drools.demo01.filter.MyAgendaFilter;
import com.drools.demo01.utils.KieSessionUtils;
import com.sun.org.apache.bcel.internal.generic.LLOAD;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class test01 {

    @Test
    public void test01() throws Exception {
        Lover lover = new Lover();
        lover.setName("AAA");
        lover.setAge(1);
        System.out.println("drl之前:"+lover.toString());
        KieSession kieSession = KieSessionUtils.getAllRules();
        kieSession.insert(lover);
//        kieSession.getAgenda().getAgendaGroup("abc").setFocus();//激活
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("drl之后:"+lover.toString());


    }

    @Test
    public void test02() throws Exception {
        List<Person> personList = new ArrayList<Person>();
        Iterator<Person> personIterable = personList.iterator();
        while (personIterable.hasNext()){

        }
    }
    @Test
    public void test3() throws Exception {
        KieSession kieSession = KieSessionUtils.getAllRules();
        Lover lover = new Lover();
        lover.setName("AAA");
        kieSession.insert(lover);
        //过滤器
        AgendaFilter agendaFilter = new MyAgendaFilter("rule - g");
        kieSession.fireAllRules(agendaFilter);
//        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println(lover.toString());
    }

    @Test
    public void test4() throws Exception {
        KieSession kieSession = KieSessionUtils.getAllRules();
//        Lover lover = new Lover();
//        lover.setName("AAA");
        Person person = new Person();
        person.setName("AAA");
        kieSession.insert(person);
        //过滤器
        AgendaFilter agendaFilter = new MyAgendaFilter("rule - h");
        kieSession.fireAllRules(agendaFilter);
//        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println(person.toString());
    }

    @Test
    public void test5() throws Exception {
        Person person = new Person();
        person.setName("AAA");

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        Resource resource = kieServices.getResources().newClassPathResource("rules/rule01.drl");
        resource.setResourceType(ResourceType.DRL);
        kfs.write(resource);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
            throw new Exception();
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieBase kBase = kieContainer.getKieBase();
        KieSession kieSession = kBase.newKieSession();
        kieSession.insert(person);
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("drl之后："+person.toString());
    }

    @Test
    public void test6(){
        Person person = new Person();
        person.setName("AAA");

        String rule = "package rules;\n" +
                "dialect  \"java\"\n" +
                "\n" +
                "import com.drools.demo01.entity.*;\n"+"rule \"rule - f\"\n" +
                "    when\n" +
                "        $person : Person(name == \"AAA\");\n" +
                "    then\n" +
                "        $person.setAge(6);\n" +
                "        System.out.println($person.getName());\n" +
                "        $person.setName(\"BBB\");\n" +
                "        $person.setAge(3);\n" +
                "    update($person)\n" +
                "end";
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        //以DRL形式加载规则
        builder.add(ResourceFactory.newByteArrayResource(rule.getBytes()), ResourceType.DRL);
        KnowledgeBuilderErrors errors = builder.getErrors();
        for (KnowledgeBuilderError error : errors) {
            System.out.println(error.getMessage());
        }
        KieBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        ((KnowledgeBase) kBase).addKnowledgePackages(builder.getKnowledgePackages());
        //获取规则引擎会话session
        KieSession kieSession = kBase.newKieSession();
        kieSession.insert(person);
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("drl之后："+person.toString());
    }
}
