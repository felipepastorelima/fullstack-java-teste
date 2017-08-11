package br.com.contabilizei.server.core.api.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

	@Override
	public BigDecimal unmarshal(String v) throws Exception {
		return new BigDecimal(v).setScale(2,RoundingMode.HALF_UP);
	}

	@Override
	public String marshal(BigDecimal v) throws Exception {
		return v.setScale(2,RoundingMode.HALF_UP).toString();
	}
 
}