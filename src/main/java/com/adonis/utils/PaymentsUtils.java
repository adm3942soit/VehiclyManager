package com.adonis.utils;

import com.adonis.data.persons.Person;
import com.google.common.collect.Lists;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

import static com.adonis.utils.CollectionUtils.map;
import static com.adonis.utils.DateUtils.addDays;
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
    Properties systemConfig = new Properties();
    Properties config = new Properties();
    GeoService geoService = GeoService.getInstance();

    private static class ResourceHolder {
        private static final PaymentsUtils paymentsUtils = new PaymentsUtils();
    }


    private PaymentsUtils() {

        systemConfig.put("httpsServerUrl", "http://localhost:8080/");
        systemConfig.put("paypal_successUri", "manager/");
        systemConfig.put("paypal_failUri", "manager/");
        systemConfig.put("paypal_accessTokenLiveTime", "28800");
        systemConfig.put("ip", geoService.getIpAdress());
        config.put("clientID", "");
        config.put("clientSecret", "");
        PayPalResource.initConfig(config);

        try {
            updateAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public PaymentsUtils getInstance(){
        return ResourceHolder.paymentsUtils;
    }

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

        accessToken = new OAuthTokenCredential(
                config.getProperty("clientID"),
                config.getProperty("clientSecret"),
                new HashMap(config)).getAccessToken();

        accessTokenLastUse = currentTimeMillis() + Long.valueOf(systemConfig.getProperty("paypal_accessTokenLiveTime"));
    }
    public PaymentHistory getLastPaymentHistory() throws Exception{
        return invoke(()->{

            DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
            String dayBefore = dateFormat.format(addDays(new Date(), -1));

            PaymentHistory hist = Payment.list(accessToken, map(
                    "start_time", dayBefore,
                    "count", "10"));
            return hist;
        });
    }
    public String payWithPaypalAcc(Person user, long val, String accessToken) throws Exception {
        com.adonis.data.persons.Address address = user.getAddress();
        com.adonis.data.payments.CreditCard card = user.getCard();
        if(Objects.isNull(address)) return systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_failUri");
        if(Objects.isNull(card)) return systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_failUri");

        return invoke(()->{
            Address billingAddress = new Address();
            billingAddress.setCity(address.getCity());
            billingAddress.setCountryCode(geoService.getCountryCode(systemConfig.getProperty("ip")));
            billingAddress.setLine1(address.getStreet());//"52 N Main ST");
            billingAddress.setPostalCode(address.getZip());
            billingAddress.setState("NA");

            CreditCard creditCard = new CreditCard();
            creditCard.setBillingAddress(billingAddress);
            creditCard.setCvv2(card.getCvv2());
            creditCard.setExpireMonth(card.getExpireMonth().startsWith("0")?Integer.parseInt(card.getExpireMonth().substring(1)):
                    Integer.parseInt(card.getExpireMonth())
            );
            creditCard.setExpireYear(Integer.parseInt(card.getExpireYear()));
            creditCard.setFirstName(user.getFirstName());
            creditCard.setLastName(user.getLastName());
            creditCard.setNumber(card.getNumber());
            creditCard.setType(card.getType());

            Details details = new Details();
            details.setShipping("0.03");
            details.setSubtotal(String.valueOf(val));
            details.setTax("0.03");

            Amount amount = new Amount();
            amount.setCurrency("EUR");
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
            redirectUrls.setReturnUrl(systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_successUri"));
            redirectUrls.setCancelUrl(systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_failUri"));
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