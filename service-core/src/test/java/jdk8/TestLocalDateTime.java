package jdk8;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TestLocalDateTime {

    @Test
    public void test1() throws InterruptedException {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        LocalDateTime ldt = LocalDateTime.now();

        System.out.println(ld);
        System.out.println(lt);
        System.out.println(ldt);

        Instant ins1 = Instant.now();
        Thread.sleep(1000);
        Instant ins2 = Instant.now();

        long l = Duration.between(ins1, ins2).toMillis();
        Duration between = Duration.between(ins1, ins2);
        System.out.println(between);
        System.out.println(l);

        System.out.println("-----------------------------");
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");

        LocalDateTime time1 = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
        String format = dtf.format(time1);
        String format1 = time1.format(dtf1);

        LocalDateTime parse = time1.parse(format1, dtf1);


        System.out.println(format1);
        System.out.println(parse);
    }
}
