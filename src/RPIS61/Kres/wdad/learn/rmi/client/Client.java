package RPIS61.Kres.wdad.learn.rmi.client;

import RPIS61.Kres.wdad.data.managers.PreferencesManager;
import RPIS61.Kres.wdad.data.model.*;
import RPIS61.Kres.wdad.learn.rmi.*;
import RPIS61.Kres.wdad.utils.PreferencesManagerConstants;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static final String BIND_NAME = "XmlDataManager";
    private static int port;
    private static String address;


    public static void main(String... args) {
        PreferencesManager pm = PreferencesManager.getInstance();
        address = pm.getProperty(PreferencesManagerConstants.registryaddress);
        port = Integer.parseInt(pm.getProperty(PreferencesManagerConstants.registryport));
        System.setProperty("java.security.policy", pm.getProperty(PreferencesManagerConstants.policypath));
        System.setProperty("java.rmi.server.useCodebaseOnly", pm.getProperty(PreferencesManagerConstants.usecodebaseonly));
        System.setSecurityManager(new RMISecurityManager());
        try {
            System.out.print("Authorization...");
            Registry registry = LocateRegistry.getRegistry(address, port);
            XmlDataManager service = (XmlDataManager) registry.lookup(BIND_NAME);
            System.out.println(" OK");
            test(service);
        } catch (Exception e) {
            System.out.println(" fail");
            e.printStackTrace();
        }
    }

    private static void test(XmlDataManager xmlDataManager) throws RemoteException {
        xmlDataManager.getReaders().forEach(System.out::println);
    }

}
