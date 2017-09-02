package com.taxisurfr.rest;

import org.jboss.resteasy.core.ExceptionAdapter;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by peter on 4/09/2016.
 */
public class TaxisurfrImplTest {

    TaxisurfrImpl taxisurfr = new TaxisurfrImpl();
    @Test
    public void should_handle_links() {
        String withLinkAndCountry = "http://app.taxisurfr.com/client/index.html?params=aussieswiss--DE#/form/transport";
        String withoutLink = "http://localhost:3000/#/form/transport";

        assertEquals(taxisurfr.getLink(withLinkAndCountry), new String[]{"aussieswiss","DE"});
        assertEquals(taxisurfr.getLink(withoutLink), new String[]{null,null});
        assertEquals(taxisurfr.getLink("xxxxxxxx"), new String[]{null,null});
    }


}
