package br.com.contabilizei.server.core.api.adapter;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<Long, Date> {
    
	@Override
    public Date unmarshal(Long l) throws Exception {
        return new Date(l);
    }
    @Override
    public Long marshal(Date date) throws Exception {
        return date.getTime();
    }
    
}