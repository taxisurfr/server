package com.taxisurfr.manager;

import com.taxisurfr.domain.Ip2nation;

import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by peter on 16/04/2017.
 */
@Stateless
public class Ip2NationManager extends AbstractDao<Ip2nation> {
    private static String FILENAME = "/ip2nation_.sql";
    private static final Logger logger = Logger.getLogger(Ip2NationCountriesManager.class.getName());

    public Ip2NationManager() {
        super(Ip2nation.class);
    }

    public void setup() {
        deleteAll();
        long ip;
        String country;
        try {
            String line = null;
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                Scanner s = new Scanner(line).useDelimiter("\\s");
                ip = s.nextLong();
                country = s.next();
                persist(Ip2nation.create(ip, country));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
