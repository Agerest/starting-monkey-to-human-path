package RPIS61.Kres.wdad.learn.rmi;

import RPIS61.Kres.wdad.data.managers.PreferencesManager;
import RPIS61.Kres.wdad.utils.PreferencesManagerConstants;

import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
            Test test = new Test(service);
            test.start();
            System.out.println(" OK");
        } catch (Exception e) {
            System.out.println(" fail");
            e.printStackTrace();
        }
    }
}
