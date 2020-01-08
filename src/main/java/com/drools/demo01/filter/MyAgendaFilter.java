package com.drools.demo01.filter;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

public class MyAgendaFilter implements AgendaFilter {
    private String ruleName;

    public MyAgendaFilter(String ruleName){
        this.ruleName = ruleName;
    }

    @Override
    public boolean accept(Match match) {
        return match.getRule().getName().equals(ruleName) ? true : false;
    }
}
