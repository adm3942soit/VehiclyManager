package com.adonis.utils;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SaveImageFromUrl {
    public static void main(String[] args) throws Exception {
        String imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
        String destinationFile = VaadinUtils.getInitialPath() + File.separator + "image.jpg";

        saveImage(imageUrl, destinationFile);
    }

    public static String downloadImage(String sourceUrl, String targetDirectory)
            throws MalformedURLException, IOException, FileNotFoundException {

        String resultFileName = sourceUrl.startsWith("http") ?
                FilenameUtils.getNameFileFromUrl(sourceUrl) : FilenameUtils.getName(sourceUrl);
        if (FilenameUtils.getExtension(resultFileName).equals("tmp")) {
            resultFileName = FilenameUtils.renameExtension(resultFileName);
        }
        if (new File(targetDirectory + File.separator + resultFileName).exists()) {
            return resultFileName;
        }

        if (sourceUrl.startsWith("https")) {
            String nameFile = downloadImageFromHttpsURL(sourceUrl, targetDirectory + File.separator + resultFileName);
            return nameFile == null ? null : resultFileName;
        } else if (sourceUrl.startsWith("http")) {
            String nameFile = downloadImageFromHttpURL(sourceUrl, targetDirectory + File.separator + resultFileName);
            return nameFile == null ? null : resultFileName;
//
//            URL imageUrl = new URL(sourceUrl);
//            Map<String, Object> result = checkProxy();
//            InetSocketAddress address=null;
//            if((Boolean) result.get("proxy")){
//                address = (InetSocketAddress) result.get("InetSocketAddress");
//            }
//            try (InputStream imageReader = new BufferedInputStream(
//
//                    imageUrl.openStream());
//                 OutputStream imageWriter = new BufferedOutputStream(
//                         new FileOutputStream(targetDirectory + File.separator + resultFileName));) {
//                int readByte;
//
//                while ((readByte = imageReader.read()) != -1) {
//                    imageWriter.write(readByte);
//                }
//            }
        } else {
            if (new File(sourceUrl).exists()) {
                FileReader.copyFile(sourceUrl, VaadinUtils.getResourcePath() + File.separator + resultFileName);
            } else return null;
        }
        return resultFileName;
    }

    public static String downloadImageFromHttpsURL(String sourceUrl, String destinationFile) {
        // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            return null;
        }

        // And as before now you can use URL and URLConnection
        try {
            URL url = new URL(sourceUrl);
            URLConnection connection;
            InputStream is;
            try {
                connection = url.openConnection();
                 is = connection.getInputStream();
            } catch (Exception e) {
                SocketAddress addr = new InetSocketAddress("10.15.0.37", 8080);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);//(InetSocketAddress)result.get("InetSocketAddress")
                connection = url.openConnection(proxy);
                is = connection.getInputStream();
            }

            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
//        FileReader.copyFile(VaadinUtils.getInitialPath() + File.separator + "image.jpg", destinationFile);
        return destinationFile;
    }

    public static String downloadImageFromHttpURL(String sourceUrl, String destinationFile) {
        // And as before now you can use URL and URLConnection
        try {
            URL url = new URL(sourceUrl);
            URLConnection connection;
//            Map<String, Object> result = checkProxy();
            InputStream is = null;
            try {
                connection = url.openConnection();
                is = connection.getInputStream();
            } catch (Exception e) {
                SocketAddress addr = new InetSocketAddress("10.15.0.37", 8080);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);//(InetSocketAddress)result.get("InetSocketAddress")
                connection = url.openConnection(proxy);
                is = connection.getInputStream();
            }

            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return destinationFile;
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        String destinationCurrentFile = VaadinUtils.getInitialPath() + File.separator + "image.jpg";
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (Exception e) {
            BufferedImage image = ImageIO.read(url);
            ImageIO.write(image, "jpg", new File(destinationFile));
//            FileReader.copyFile(imageUrl, destinationFile);
            return;
        }
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationCurrentFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
        FileReader.copyFile(VaadinUtils.getInitialPath() + File.separator + "image.jpg", destinationFile);
    }

    public static Map<String, Object> checkProxy() {
        Map<String, Object> map = new HashMap<>();
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            List<Proxy> l = ProxySelector.getDefault().select(
                    new URI("http://www.google.com/"));

            for (Iterator<Proxy> iter = l.iterator(); iter.hasNext(); ) {
                Proxy proxy = iter.next();
                System.out.println("proxy hostname : " + proxy.type());
                InetSocketAddress addr = (InetSocketAddress) proxy.address();

                if (addr == null) {
                    map.put("proxy", false);
                    System.out.println("No Proxy");
                } else {
                    map.put("proxy", true);
                    map.put("InetSocketAddress", addr);
                    map.put("hostname", addr.getHostName());
                    map.put("port", addr.getPort());
                    System.setProperty("http.proxyHost", addr.getHostName());
                    System.setProperty("http.proxyPort", String.valueOf(addr.getPort()));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
