package com.adonis.utils.payPal;

import com.adonis.data.persons.Person;
import com.adonis.utils.GeoService;
import com.google.common.collect.Lists;
import com.paypal.api.payments.*;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.core.ConfigManager;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;
import com.paypal.exception.*;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import org.apache.poi.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

import static com.adonis.utils.CollectionUtils.map;
import static com.adonis.utils.DateUtils.addDays;
import static com.adonis.utils.payPal.Configuration.adaptivePaymentsService;
import static java.lang.System.currentTimeMillis;
import static org.springframework.util.StringUtils.hasText;

/**
 * Created by oksdud on 03.05.2017.
 */
public class PaymentsPayPalUtils {
    public static final String DEFAULT_CONFIGURATION_FILE ="sdk_config.properties";
    public static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String ERROR_CODE_401 = "Error code : 401";
    private volatile String accessToken;
    private volatile long accessTokenLastUse;
    Properties systemConfig = new Properties();
    Properties config = new Properties();
    GeoService geoService = GeoService.getInstance();
    static String PAYPAL_ACCESS_TOKEN;
    static String clientID;
    static String clientSecret;
    private static class ResourceHolder {
        private static final PaymentsPayPalUtils paymentsUtils = new PaymentsPayPalUtils();
    }
    private static void initPayPal() {
        InputStream is = null;
        try {
            is = PaymentsPayPalUtils.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIGURATION_FILE);
//                    IS_PRODUCTION ? "/my_paypal_sdk_config.properties" : "/my_paypal_sdk_config_test.properties");
            Properties props = new Properties();
            props.load(is);
            PayPalResource.initConfig(props);
            ConfigManager.getInstance().load(props);
            clientID = ConfigManager.getInstance().getConfigurationMap().get("clientID");
            clientSecret = ConfigManager.getInstance().getConfigurationMap().get("clientSecret");
            Configuration.getAcctAndConfig();
//            AdaptivePaymentsService permissionsService = new AdaptivePaymentsService(Constants.DEFAULT_CONFIGURATION_FILE);
            /*PAYPAL_ACCESS_TOKEN = new OAuthTokenCredential(clientID, clientSecret).getAccessToken();*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    private void initProperties(){
        Map<String, String> customConfigurationMap = new HashMap<String, String>();
        customConfigurationMap.put("mode", "sandbox");
        customConfigurationMap.putAll(ConfigManager.getInstance().getConfigurationMap());
        adaptivePaymentsService = new AdaptivePaymentsService(customConfigurationMap);
//        customConfigurationMap.put("acct1.UserName", "adm3942soit@gmail.com");
//        customConfigurationMap.put("acct1.Password", "libra280963");

    }
    private PaymentsPayPalUtils() {

        systemConfig.put("httpsServerUrl", "http://localhost:8080/");
        systemConfig.put("paypal_successUri", "manager/");
        systemConfig.put("paypal_failUri", "manager/");
        systemConfig.put("paypal_accessTokenLiveTime", "28800");
        systemConfig.put("ip", geoService.getIpAdress());
        initPayPal();
        initProperties();

//        try {
//            updateAccessToken();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public static PaymentsPayPalUtils getInstance(){
        return ResourceHolder.paymentsUtils;
    }

    public static List<String> payment(String receiverEmail, String userName){
        RequestEnvelope env = new RequestEnvelope();
        env.setErrorLanguage("en_US");

        List<Receiver> receiver = new ArrayList<Receiver>();
        Receiver rec = new Receiver();
        rec.setAmount(2.0);
        rec.setEmail(receiverEmail);
        receiver.add(rec);
        ReceiverList receiverlst = new ReceiverList(receiver);

        PayRequest payRequest = new PayRequest();
        payRequest.setReceiverList(receiverlst);
        payRequest.setRequestEnvelope(env);
        List<String> errors = new ArrayList<>();
        try {
            PayResponse payResponse = adaptivePaymentsService.pay(payRequest, userName);

            payResponse.getError().forEach(errorData -> {
                errors.add(errorData.getMessage());
            });
            return errors;//(payResponse.getError().size()==0);
        } catch (SSLConfigurationException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (InvalidCredentialException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (IOException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (HttpErrorException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (InvalidResponseDataException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (ClientActionRequiredException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (MissingCredentialException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (InterruptedException e) {
            errors.add(e.getMessage());
            return errors;
        } catch (OAuthException e) {
            errors.add(e.getMessage());
            return errors;
        }
    }

    private <T> T invoke(Callable<T> body) throws Exception{
        try {

//            updateAccessTokenIfNeed();

            return body.call();

        }catch(PayPalRESTException e){
            String msg = e.getMessage();
            if(hasText(msg) && msg.contains(ERROR_CODE_401)){
//                updateAccessToken();
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
                config.getProperty(Constants.CLIENT_ID),
                config.getProperty(Constants.CLIENT_SECRET),
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
    public boolean payWithPaypalAcc(Person user, long val) throws Exception {//, String accessToken
        if(Objects.isNull(user)){ com.vaadin.ui.Notification.show("User not entered!"); return false;}//systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_failUri");
        com.adonis.data.persons.Address address = user.getAddress();
        com.adonis.data.payments.CreditCard card = user.getCard();
        if(Objects.isNull(address)){ com.vaadin.ui.Notification.show("Please, enter address!"); return false;}//systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_failUri");
        if(Objects.isNull(card)){ com.vaadin.ui.Notification.show("Please, enter credit card!"); return false;}//systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_failUri");

        return !(invoke(()->{
            Address billingAddress = new Address();
            billingAddress.setCity(address.getCity());
            billingAddress.setCountryCode(geoService.getCountryISOCode(address.getCountry()));//geoService.getCountryCode(systemConfig.getProperty("ip")));
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
            payer.setPaymentMethod("paypal");
            payer.setFundingInstruments(fundingInstruments);
//            payer.setPaymentMethod("credit_card");

            Payment payment = new Payment();
//            payment.setIntent("authorize");
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);


            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setReturnUrl(systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_successUri"));
            redirectUrls.setCancelUrl(systemConfig.getProperty("httpsServerUrl")+systemConfig.getProperty("paypal_failUri"));
            payment.setRedirectUrls(redirectUrls);
            APIContext apiContext = new APIContext(clientID, clientSecret, "sandbox");
            Payment createdPayment = null;
            try {
                createdPayment = payment.create(apiContext);
            } catch (com.paypal.base.rest.PayPalRESTException e) {
                throw new IllegalStateException("No access token!");
            }
            Iterator<Links> links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return link.getHref();
                }
            }

            throw new IllegalStateException("No approval_url in payment resp: "+createdPayment.toJSON());
        })!=null);
    }

}
