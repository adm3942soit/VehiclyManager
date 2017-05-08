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
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class GeoService {

    private static class ResourceHolder {
        private static final GeoService geoService = new GeoService();
    }

    private DatabaseReader readerCity;
    private DatabaseReader readerCountry;

    private GeoService() {
        try {
            readerCity = new DatabaseReader.Builder(new File("GeoLite2-City.mmdb")).build();
            readerCountry = new DatabaseReader.Builder(new File("GeoIP2-Country.mmdb")).build();
        } catch (IOException e) {
            log.error("Exception:", e);
            throw new RuntimeException(e);
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
        } catch (IOException e) {
            log.error("Exception:", e);
        } catch (GeoIp2Exception e) {
            log.error("Exception:", e);
        }
        return "";
    }
    //    public String getIsp(String ip) {
//        try {
//            CityIspOrgResponse ci = readerCity.cityIspOrg(InetAddress.getByName(ip));
//            return ci.getTraits().getIsp();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (GeoIp2Exception e) {
//            e.printStackTrace();
//        }
//        return "--";
//    }
    public String getIpCountry(InetAddress ipAddress) {
        try {
            CountryResponse countryResponse = readerCountry.country(ipAddress);

            return countryResponse.getCountry().getIsoCode();
        } catch (IOException | GeoIp2Exception ex) {
            log.info("Could not get country for IP " + ipAddress);
            return "ZZZ";
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
        return "ZZZ";
    }

    public static String getIpAdress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("UnknownHostException:", e);;
        }
        return "";
    }
    public static InetAddress getIpInetAdress(){
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("UnknownHostException:", e);;
        }
        return null;
    }

}