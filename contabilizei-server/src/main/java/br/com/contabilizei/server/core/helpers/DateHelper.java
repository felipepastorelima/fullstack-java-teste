package br.com.contabilizei.server.core.helpers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	
	public static String toYYYYMM(Date date) {
		checkNotNull(date, "Data não pode estar vazia!");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(date);
	}
	
	public static Date addTo(Date date, int amount, int type) {
		checkNotNull(date, "Data não pode estar vazia!");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, amount);
		return calendar.getTime();
	}
	
}
