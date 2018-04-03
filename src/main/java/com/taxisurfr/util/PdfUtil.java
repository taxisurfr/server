package com.taxisurfr.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.taxisurfr.domain.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

public class PdfUtil {

    public final static String FORMDATE = "FormDate";
    public final static String ORDERNO = "OrderNo";
    public final static String NAME = "Name";
    public final static String EMAIL = "Email";
    public final static String PAX = "Passengers";
    public final static String SURFBOARDS = "Surfboards";
    public final static String DATE = "Date";
    public final static String FLIGHTNO = "FlightNo";
    public final static String LANDINGTIME = "LandingTime";
    public final static String PAID = "Paid";
    public final static String OTHER1 = "Other1";
    public final static String OTHER2 = "Other2";
    public final static String OTHER3 = "Other3";
    public final static String ARRIVAL = "Arrival";
    public final static String FLIGHT_HOTEL = "Flight_Hotel";

    static final DateTimeFormatter fmt = DateTimeFormatter.BASIC_ISO_DATE;

    static final float TABLE_WIDTH = 452;
    static final float TABLE_Y = 400;
    static final float INSET = 72;

    static final String CUSTOMER_FEEDBACK = "BTW. You will receive a link for feedback shortly after your trip. Taking a few minutes to answer this will help us greatly. Thank you in advance.";


    public byte[] generateTaxiOrder(String path, Booking booking, Contractor contractor) {
        PdfReader reader;
        try {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(path);

            reader = new PdfReader(IOUtils.toByteArray(inputStream));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, out);
            PdfContentByte canvas = stamper.getOverContent(1);

            PdfPTable table = createBookingTable(booking);
            table.writeSelectedRows(0, 2, 0, 11, INSET, TABLE_Y, canvas);

            Font helvetica20 = new Font(Font.FontFamily.HELVETICA, 20);
            Font helvetica14 = new Font(Font.FontFamily.HELVETICA, 14);
            BaseFont bf_helv = helvetica20.getCalculatedBaseFont(false);

            // route
            int addressY = 430;
            Location start = booking.getPrice().getStartroute();
            Location end = booking.getPrice().getEndroute();
            Chunk chunk = new Chunk(start.getName() + " to " + end.getName(), helvetica20);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(chunk), INSET, addressY, 0);

            addressY = 564;

            // contractor
            if (contractor.getEmail() != null) {
                chunk = new Chunk(contractor.getEmail(), helvetica14);
                ColumnText.showTextAligned(canvas,
                        Element.ALIGN_LEFT, new Phrase(chunk), INSET + 120, addressY, 0);
            }

            String address = contractor.getMobile()!= null ? "+"+ contractor.getMobile():
                    contractor.getEmail();

            addressY -= 42;
            chunk = new Chunk(address, helvetica14);

            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(chunk), INSET, addressY, 0);
            stamper.close();
            reader.close();
            inputStream.close();

            return out.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] get() {

        try {
            return Files.readAllBytes(new File("template/test.pdf").toPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    public PdfPTable createBookingTable(Booking booking) {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(TABLE_WIDTH);
        table.setWidthPercentage(TABLE_WIDTH / 3f);
        try {
            table.setWidths(new int[]{1, 2});
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PickupType pickupType = booking.getPrice().getStartroute().getName().equals("COLOMBO AIRPORT") ? PickupType.AIRPORT : PickupType.HOTEL;
        // the cell object
        PdfPCell cell;
        table.addCell(ORDERNO);
        table.addCell(booking.getRef());
        table.addCell(NAME);
        table.addCell(booking.getName());
        table.addCell(EMAIL);
        table.addCell(booking.getEmail());
        table.addCell(PAX);
        table.addCell("" + booking.getPax());
        table.addCell(SURFBOARDS);
        table.addCell("" + booking.getSurfboards());
        table.addCell(DATE);
        table.addCell(booking.getDateText());
        table.addCell(pickupType.getLocationType());
        table.addCell(booking.getFlightNo());
        table.addCell(pickupType.getTimeType());
        table.addCell(booking.getLandingTime());
        //table.addCell(PAID);

        //table.addCell(booking.getCurrency().symbol + " " + booking.getPaidPrice()/100);

        table.addCell("Other requirements");
        table.addCell(booking.getRequirements());

        return table;
    }

}
