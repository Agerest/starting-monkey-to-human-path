package RPIS61.Kres.wdad.learn.rmi.server;

import RPIS61.Kres.wdad.data.managers.PreferencesManager;
import RPIS61.Kres.wdad.utils.PreferencesManagerConstants;

import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private static final String bindName = "XmlDataManager";
    private static final int sleepTime = Integer.MAX_VALUE;
    private static int port;
    private static String address;
    private static ShutdownHook shutdownHook;
    private static PreferencesManager pm;

    public static void main(String... args) throws Exception {
        shutdownHook = new ShutdownHook();
        pm = PreferencesManager.getInstance();
        port = Integer.parseInt(pm.getProperty(PreferencesManagerConstants.registryport));
        address = pm.getProperty(PreferencesManagerConstants.registryaddress);
        System.setProperty("java.security.policy", pm.getProperty(PreferencesManagerConstants.policypath));
        System.setProperty("java.rmi.server.useCodebaseOnly", pm.getProperty(PreferencesManagerConstants.usecodebaseonly));
        System.setProperty("java.rmi.server.hostname", address);
        System.setSecurityManager(new SecurityManager());
        try {
            System.out.print("Starting registry...");
            final Registry registry;
            if (pm.getProperty(PreferencesManagerConstants.createregistry).equals("yes")) {
                registry = LocateRegistry.createRegistry(port);
                System.out.println(" OK");
            }
            else {
                registry = LocateRegistry.getRegistry(port);
                System.out.println(" OK");
            }

            final XmlDataManagerImpl service = new XmlDataManagerImpl();
            UnicastRemoteObject.exportObject(service, 0);

            System.out.print("Binding service...");
            registry.bind(bindName, service);
            System.out.println(" OK");

            pm.addBindedObject(bindName,"XmlDataManager");

            System.out.println("Server working...");

        } catch (Exception e) {
            System.out.println(" fail");
            e.printStackTrace();
            System.exit(0);
        }
        while (true) {
            Thread.sleep(sleepTime);
        }
    }

    static class ShutdownHook extends Thread {
        public void run() {
            pm.removeBindedObject(bindName);
        }
    }

}
