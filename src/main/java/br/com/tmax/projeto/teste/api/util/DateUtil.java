package br.com.tmax.projeto.teste.api.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
	public static String format = "dd/MM/yyyy";
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.format);

	public static LocalDate stringToLocalDate(String date) {
		return LocalDate.parse(date, DateUtil.formatter);
	}

	public static String localDateToString(LocalDate date) {
		return date.format(DateUtil.formatter);
	}
	
	public static Integer idade(final LocalDate aniversario) {
	    return Period.between(aniversario, LocalDate.now()).getYears();
	}
}
