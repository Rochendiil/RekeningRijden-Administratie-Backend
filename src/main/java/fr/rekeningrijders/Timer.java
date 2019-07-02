package fr.rekeningrijders;

import java.time.Month;
import java.util.Calendar;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import fr.rekeningrijders.service.InvoiceService;

@Singleton
public class Timer
{
    @Inject
    private InvoiceService invoiceService;

    @Schedule(dayOfMonth = "last")
    public void generate()
    {
        Calendar calender = Calendar.getInstance();
        short year = (short)calender.get(Calendar.YEAR);
        Month month = Month.of(calender.get(Calendar.MONTH)+1);
        invoiceService.generateInvoices(year, month);
    }
}