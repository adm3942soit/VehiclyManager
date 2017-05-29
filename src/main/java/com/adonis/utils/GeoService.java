package com.adonis.utils;

/**
 * Created by oksdud on 08.05.2017.
 */

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class GeoService {

    private static class ResourceHolder {
        private static final GeoService geoService = new GeoService();
    }

    private DatabaseReader readerCity;
    private DatabaseReader readerCountry;
    private String ip;
    private InetAddress ipAddress;
    private CountryCodes countryCodes;

    private static CountryResponse countryResponse;

    private GeoService() {
        try {
            readerCity = new DatabaseReader.Builder(new File(this.getClass().getClassLoader().getResource("GeoLite2-City.mmdb").getPath())).build();
            readerCountry = new DatabaseReader.Builder(new File(this.getClass().getClassLoader().getResource("GeoIP2-Country.mmdb").getPath())).build();
            countryCodes = new CountryCodes();
        } catch (Exception e) {
            log.error("Exception:", e);
        }
        try {
            ip = getIpAdress();
            ipAddress = getIpInetAdress();
        } catch (Exception e) {
            log.error("Exception:", e);
        }
    }

    public static GeoService getInstance() {
        return ResourceHolder.geoService;
    }

    public String getCountryCode(String ip) {
        try {
            CountryResponse cr = readerCity.country(InetAddress.getByName(ip));
            return cr.getCountry().getIsoCode();
        } catch (UnknownHostException e) {
            log.error("Exception:", e);
        } catch (IOException e) {
            log.error("Exception:", e);
        } catch (GeoIp2Exception e) {
            log.error("Exception:", e);
        }
        return "--";
    }

    public String getCountryName(String ip) {
        try {
            CountryResponse cr = readerCity.country(InetAddress.getByName(ip));
            return cr.getCountry().getName();
        } catch (UnknownHostException e) {
            log.error("Exception:", e);
        } catch (IOException e) {
            log.error("Exception:", e);
        } catch (GeoIp2Exception e) {
            log.error("Exception:", e);
        }
        return "--";
    }
    public String getCity(String ip){
        try {
            CityResponse cr = readerCity.city(InetAddress.getByName(ip));
            return cr.getCity().getName();
        } catch (UnknownHostException e) {
            log.error("Exception:", e);
            return "";
        } catch (IOException e) {
            log.error("Exception:", e);
            return "";
        } catch (GeoIp2Exception e) {
            log.error("Exception:", e);
            return "";
        }
    }
    public String getCity(InetAddress ip){
        try {
            CityResponse cr = readerCity.city(ip);
            return cr.getCity().getName();
        } catch (UnknownHostException e) {
            log.error("Exception:", e);
            return "Riga";
        } catch (IOException e) {
            log.error("Exception:", e);
            return "Riga";
        } catch (GeoIp2Exception e) {
            log.error("Exception:", e);
            return "Riga";
        }
    }

    public String getCountry(InetAddress ipAddress) {
        try {
            countryResponse = readerCountry.country(ipAddress);

            return countryResponse.getCountry().getName();
        } catch (IOException | GeoIp2Exception ex) {
            log.info("Could not get country for IP " + ipAddress);
            try {
                countryResponse = readerCountry.country(InetAddress.getByName(VaadinUtils.getIpAddress()));
                return countryResponse.getCountry().getName();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeoIp2Exception e) {
                e.printStackTrace();
            }finally {
                return "Latvia";
            }
//            return "ZZZ";
        }

    }
    public String getIpCountry(InetAddress ipAddress) {
        try {
            CountryResponse countryResponse = readerCountry.country(ipAddress);

            return countryResponse.getCountry().getIsoCode();
        } catch (IOException | GeoIp2Exception ex) {
            log.info("Could not get country for IP " + ipAddress);
//            return "ZZZ";
            return countryCodes.map.get("Latvia");
        }
    }
    public EnterpriseResponse getInfo(InetAddress ipAddress){
        try {
           EnterpriseResponse enterpriseResponse = readerCountry.enterprise(ipAddress);
            return enterpriseResponse;
        } catch (IOException e) {
            log.error("Exception:", e);
        } catch (GeoIp2Exception e) {
            log.error("Exception:", e);
        }
        return null;
    }
    public String getIpCountry(String ipAddress) {
        try {
            return getIpCountry(InetAddress.getByName(ipAddress));
        } catch (UnknownHostException ex) {
            log.info("Bad ip address " + ipAddress, ex);
        }
//        return "ZZZ";
        return countryCodes.map.get("Latvia");
    }

    public String getIpAdress(){
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("UnknownHostException:", e);;
            return VaadinUtils.getIpAddress();
        }
    }
    public InetAddress getIpInetAdress(){
        try {
            return Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("UnknownHostException:", e);;
            try {
                InetAddress inetAddress = InetAddress.getByName(VaadinUtils.getIpAddress());
                return  inetAddress;
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public String getIp() {
        return ip;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public CountryResponse getCountryResponse() {
        return countryResponse;
    }
    private class CountryCodes {
        final Map<String, String> map = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        public CountryCodes() {

            map.put("Andorra, Principality Of", "AD");
            map.put("United Arab Emirates", "AE");
            map.put("Afghanistan, Islamic State Of", "AF");
            map.put("Antigua And Barbuda", "AG");
            map.put("Anguilla", "AI");
            map.put("Albania", "AL");
            map.put("Armenia", "AM");
            map.put("Netherlands Antilles", "AN");
            map.put("Angola", "AO");
            map.put("Antarctica", "AQ");
            map.put("Argentina", "AR");
            map.put("American Samoa", "AS");
            map.put("Austria", "AT");
            map.put("Australia", "AU");
            map.put("Aruba", "AW");
            map.put("Azerbaidjan", "AZ");
            map.put("Bosnia-Herzegovina", "BA");
            map.put("Barbados", "BB");
            map.put("Bangladesh", "BD");
            map.put("Belgium", "BE");
            map.put("Burkina Faso", "BF");
            map.put("Bulgaria", "BG");
            map.put("Bahrain", "BH");
            map.put("Burundi", "BI");
            map.put("Benin", "BJ");
            map.put("Bermuda", "BM");
            map.put("Brunei Darussalam", "BN");
            map.put("Bolivia", "BO");
            map.put("Brazil", "BR");
            map.put("Bahamas", "BS");
            map.put("Bhutan", "BT");
            map.put("Bouvet Island", "BV");
            map.put("Botswana", "BW");
            map.put("Belarus", "BY");
            map.put("Belize", "BZ");
            map.put("Canada", "CA");
            map.put("Cocos (Keeling) Islands", "CC");
            map.put("Central African Republic", "CF");
            map.put("Congo, The Democratic Republic Of The", "CD");
            map.put("Congo", "CG");
            map.put("Switzerland", "CH");
            map.put("Ivory Coast (Cote D'Ivoire)", "CI");
            map.put("Cook Islands", "CK");
            map.put("Chile", "CL");
            map.put("Cameroon", "CM");
            map.put("China", "CN");
            map.put("Colombia", "CO");
            map.put("Costa Rica", "CR");
            map.put("Former Czechoslovakia", "CS");
            map.put("Cuba", "CU");
            map.put("Cape Verde", "CV");
            map.put("Christmas Island", "CX");
            map.put("Cyprus", "CY");
            map.put("Czech Republic", "CZ");
            map.put("Germany", "DE");
            map.put("Djibouti", "DJ");
            map.put("Denmark", "DK");
            map.put("Dominica", "DM");
            map.put("Dominican Republic", "DO");
            map.put("Algeria", "DZ");
            map.put("Ecuador", "EC");
            map.put("Estonia", "EE");
            map.put("Egypt", "EG");
            map.put("Western Sahara", "EH");
            map.put("Eritrea", "ER");
            map.put("Spain", "ES");
            map.put("Ethiopia", "ET");
            map.put("Finland", "FI");
            map.put("Fiji", "FJ");
            map.put("Falkland Islands", "FK");
            map.put("Micronesia", "FM");
            map.put("Faroe Islands", "FO");
            map.put("France", "FR");
            map.put("France (European Territory)", "FX");
            map.put("Gabon", "GA");
            map.put("Great Britain", "UK");
            map.put("Grenada", "GD");
            map.put("Georgia", "GE");
            map.put("French Guyana", "GF");
            map.put("Ghana", "GH");
            map.put("Gibraltar", "GI");
            map.put("Greenland", "GL");
            map.put("Gambia", "GM");
            map.put("Guinea", "GN");
            map.put("Guadeloupe (French)", "GP");
            map.put("Equatorial Guinea", "GQ");
            map.put("Greece", "GR");
            map.put("S. Georgia & S. Sandwich Isls.", "GS");
            map.put("Guatemala", "GT");
            map.put("Guam (USA)", "GU");
            map.put("Guinea Bissau", "GW");
            map.put("Guyana", "GY");
            map.put("Hong Kong", "HK");
            map.put("Heard And McDonald Islands", "HM");
            map.put("Honduras", "HN");
            map.put("Croatia", "HR");
            map.put("Haiti", "HT");
            map.put("Hungary", "HU");
            map.put("Indonesia", "ID");
            map.put("Ireland", "IE");
            map.put("Israel", "IL");
            map.put("India", "IN");
            map.put("British Indian Ocean Territory", "IO");
            map.put("Iraq", "IQ");
            map.put("Iran", "IR");
            map.put("Iceland", "IS");
            map.put("Italy", "IT");
            map.put("Jamaica", "JM");
            map.put("Jordan", "JO");
            map.put("Japan", "JP");
            map.put("Kenya", "KE");
            map.put("Kyrgyz Republic (Kyrgyzstan)", "KG");
            map.put("Cambodia, Kingdom Of", "KH");
            map.put("Kiribati", "KI");
            map.put("Comoros", "KM");
            map.put("Saint Kitts & Nevis Anguilla", "KN");
            map.put("North Korea", "KP");
            map.put("South Korea", "KR");
            map.put("Kuwait", "KW");
            map.put("Cayman Islands", "KY");
            map.put("Kazakhstan", "KZ");
            map.put("Laos", "LA");
            map.put("Lebanon", "LB");
            map.put("Saint Lucia", "LC");
            map.put("Liechtenstein", "LI");
            map.put("Sri Lanka", "LK");
            map.put("Liberia", "LR");
            map.put("Lesotho", "LS");
            map.put("Lithuania", "LT");
            map.put("Luxembourg", "LU");
            map.put("Latvia", "LV");
            map.put("Libya", "LY");
            map.put("Morocco", "MA");
            map.put("Monaco", "MC");
            map.put("Moldavia", "MD");
            map.put("Madagascar", "MG");
            map.put("Marshall Islands", "MH");
            map.put("Macedonia", "MK");
            map.put("Mali", "ML");
            map.put("Myanmar", "MM");
            map.put("Mongolia", "MN");
            map.put("Macau", "MO");
            map.put("Northern Mariana Islands", "MP");
            map.put("Martinique (French)", "MQ");
            map.put("Mauritania", "MR");
            map.put("Montserrat", "MS");
            map.put("Malta", "MT");
            map.put("Mauritius", "MU");
            map.put("Maldives", "MV");
            map.put("Malawi", "MW");
            map.put("Mexico", "MX");
            map.put("Malaysia", "MY");
            map.put("Mozambique", "MZ");
            map.put("Namibia", "NA");
            map.put("New Caledonia (French)", "NC");
            map.put("Niger", "NE");
            map.put("Norfolk Island", "NF");
            map.put("Nigeria", "NG");
            map.put("Nicaragua", "NI");
            map.put("Netherlands", "NL");
            map.put("Norway", "NO");
            map.put("Nepal", "NP");
            map.put("Nauru", "NR");
            map.put("Neutral Zone", "NT");
            map.put("Niue", "NU");
            map.put("New Zealand", "NZ");
            map.put("Oman", "OM");
            map.put("Panama", "PA");
            map.put("Peru", "PE");
            map.put("Polynesia (French)", "PF");
            map.put("Papua New Guinea", "PG");
            map.put("Philippines", "PH");
            map.put("Pakistan", "PK");
            map.put("Poland", "PL");
            map.put("Saint Pierre And Miquelon", "PM");
            map.put("Pitcairn Island", "PN");
            map.put("Puerto Rico", "PR");
            map.put("Portugal", "PT");
            map.put("Palau", "PW");
            map.put("Paraguay", "PY");
            map.put("Qatar", "QA");
            map.put("Reunion (French)", "RE");
            map.put("Romania", "RO");
            map.put("Russian Federation", "RU");
            map.put("Rwanda", "RW");
            map.put("Saudi Arabia", "SA");
            map.put("Solomon Islands", "SB");
            map.put("Seychelles", "SC");
            map.put("Sudan", "SD");
            map.put("Sweden", "SE");
            map.put("Singapore", "SG");
            map.put("Saint Helena", "SH");
            map.put("Slovenia", "SI");
            map.put("Svalbard And Jan Mayen Islands", "SJ");
            map.put("Slovak Republic", "SK");
            map.put("Sierra Leone", "SL");
            map.put("San Marino", "SM");
            map.put("Senegal", "SN");
            map.put("Somalia", "SO");
            map.put("Suriname", "SR");
            map.put("Saint Tome (Sao Tome) And Principe", "ST");
            map.put("Former USSR", "SU");
            map.put("El Salvador", "SV");
            map.put("Syria", "SY");
            map.put("Swaziland", "SZ");
            map.put("Turks And Caicos Islands", "TC");
            map.put("Chad", "TD");
            map.put("French Southern Territories", "TF");
            map.put("Togo", "TG");
            map.put("Thailand", "TH");
            map.put("Tadjikistan", "TJ");
            map.put("Tokelau", "TK");
            map.put("Turkmenistan", "TM");
            map.put("Tunisia", "TN");
            map.put("Tonga", "TO");
            map.put("East Timor", "TP");
            map.put("Turkey", "TR");
            map.put("Trinidad And Tobago", "TT");
            map.put("Tuvalu", "TV");
            map.put("Taiwan", "TW");
            map.put("Tanzania", "TZ");
            map.put("Ukraine", "UA");
            map.put("Uganda", "UG");
            map.put("United Kingdom", "UK");
            map.put("USA Minor Outlying Islands", "UM");
            map.put("United States", "US");
            map.put("Uruguay", "UY");
            map.put("Uzbekistan", "UZ");
            map.put("Holy See (Vatican City State)", "VA");
            map.put("Saint Vincent & Grenadines", "VC");
            map.put("Venezuela", "VE");
            map.put("Virgin Islands (British)", "VG");
            map.put("Virgin Islands (USA)", "VI");
            map.put("Vietnam", "VN");
            map.put("Vanuatu", "VU");
            map.put("Wallis And Futuna Islands", "WF");
            map.put("Samoa", "WS");
            map.put("Yemen", "YE");
            map.put("Mayotte", "YT");
            map.put("Yugoslavia", "YU");
            map.put("South Africa", "ZA");
            map.put("Zambia", "ZM");
            map.put("Zaire", "ZR");
            map.put("Zimbabwe", "ZW");

        }

        public String getCode(String country){
            String countryFound = map.get(country);
            if(countryFound==null){
                countryFound="LV";
            }
            return countryFound;
        }
    }

    public CountryCodes getCountryCodes() {
        return countryCodes;
    }
    public String getCountryISOCode(String nameCountry) {
        return countryCodes.getCode(nameCountry);
    }

}