package com.adonis.ui.renta;

import com.adonis.data.persons.Person;
import com.google.common.collect.Lists;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import static java.lang.System.currentTimeMillis;
import static org.springframework.util.StringUtils.hasText;

/**
 * Created by oksdud on 03.05.2017.
 */
public class PaymentsUtils {
    public static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String ERROR_CODE_401 = "Error code : 401";
    private volatile String accessToken;
    private volatile long accessTokenLastUse;

    public PaymentsUtils() {
        Properties config = new Properties();
//        config.putAll();
        PayPalResource.initConfig(config);

        try {
            updateAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private Props sysProps;
//    private Props conProps;
    private <T> T invoke(Callable<T> body) throws Exception{
        try {

            updateAccessTokenIfNeed();

            return body.call();

        }catch(PayPalRESTException e){
            String msg = e.getMessage();
            if(hasText(msg) && msg.contains(ERROR_CODE_401)){
                updateAccessToken();
                return body.call();
            }
            throw e;
        }
    }
    private void updateAccessTokenIfNeed() throws Exception{

        long curLastTimeToUse = accessTokenLastUse;
        if(currentTimeMillis() < curLastTimeToUse) return;

        updateAccessToken(curLastTimeToUse);
    }

    private void updateAccessToken() throws Exception {
        updateAccessToken(accessTokenLastUse);
    }

    private synchronized void updateAccessToken(long oldLastTime) throws Exception {

        if(accessTokenLastUse != oldLastTime) return;

//        accessToken = new OAuthTokenCredential(
//                conProps.findVal("clientID"),
//                conProps.findVal("clientSecret"),
//                conProps.toMap()).getAccessToken();
//
//        accessTokenLastUse = currentTimeMillis() + sysProps.getLongVal(paypal_accessTokenLiveTime);
    }
    public PaymentHistory getLastPaymentHistory() throws Exception{
        return invoke(()->{

            DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
//            String dayBefore = dateFormat.format(addDays(new Date(), -1));
//
//            PaymentHistory hist = Payment.list(accessToken, map(
//                    "start_time", dayBefore,
//                    "count", "10"));
            PaymentHistory hist = null;
            return hist;
        });
    }
    public String payWithPaypalAcc(Person user, long val, String accessToken) throws Exception {

//        validateState(val > 0, "val");

        return invoke(()->{
            Address billingAddress = new Address();
            billingAddress.setCity("Johnstown");
            billingAddress.setCountryCode("US");
            billingAddress.setLine1("52 N Main ST");
            billingAddress.setPostalCode("43210");
            billingAddress.setState("OH");

            CreditCard creditCard = new CreditCard();
            creditCard.setBillingAddress(billingAddress);
            creditCard.setCvv2(874);
            creditCard.setExpireMonth(11);
            creditCard.setExpireYear(2018);
            creditCard.setFirstName("Joe");
            creditCard.setLastName("Shopper");
            creditCard.setNumber("4417119669820331");
            creditCard.setType("visa");

            Details details = new Details();
            details.setShipping("0.03");
            details.setSubtotal(String.valueOf(val));
            details.setTax("0.03");

            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.valueOf(val));
            amount.setDetails(details);

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Online Chat Payment for User Account: "+user.getLogin());

            List<Transaction> transactions = Lists.newArrayList();//list(transaction);
            transactions.add(transaction);

            FundingInstrument fundingInstrument = new FundingInstrument();
            fundingInstrument.setCreditCard(creditCard);

            List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
            fundingInstruments.add(fundingInstrument);

            Payer payer = new Payer();
//            payer.setPaymentMethod("paypal");
            payer.setFundingInstruments(fundingInstruments);
            payer.setPaymentMethod("credit_card");

            Payment payment = new Payment();
//            payment.setIntent("authorize");
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);


            RedirectUrls redirectUrls = new RedirectUrls();
//            redirectUrls.setReturnUrl(sysProps.findVal(httpsServerUrl) + sysProps.getStrVal(paypal_successUri));
//            redirectUrls.setCancelUrl(sysProps.findVal(httpsServerUrl) + sysProps.getStrVal(paypal_failUri));
            payment.setRedirectUrls(redirectUrls);


            Payment createdPayment = payment.create(new APIContext(accessToken));
            Iterator<Links> links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return link.getHref();
                }
            }

            throw new IllegalStateException("No approval_url in payment resp: "+createdPayment.toJSON());
        });
    }

}
