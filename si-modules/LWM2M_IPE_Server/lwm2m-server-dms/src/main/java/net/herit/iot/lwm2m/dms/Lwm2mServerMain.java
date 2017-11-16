package net.herit.iot.lwm2m.dms;

import java.math.BigInteger;
import java.net.BindException;
import java.net.URI;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.leshan.core.node.codec.DefaultLwM2mNodeDecoder;
import org.eclipse.leshan.core.node.codec.DefaultLwM2mNodeEncoder;
import org.eclipse.leshan.core.node.codec.LwM2mNodeDecoder;
import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.ClientRegistry;
import org.eclipse.leshan.server.demo.cluster.RedisClientRegistry;
import org.eclipse.leshan.server.demo.cluster.RedisObservationRegistry;
import org.eclipse.leshan.server.demo.cluster.RedisSecurityRegistry;

import net.herit.iot.lwm2m.dms.servlet.ApiServlet;
import net.herit.iot.lwm2m.dms.servlet.ClientServlet;
import net.herit.iot.lwm2m.dms.servlet.EventServlet;
import net.herit.iot.lwm2m.dms.servlet.ObjectSpecServlet;
import net.herit.iot.lwm2m.dms.servlet.SecurityServlet;
import org.eclipse.leshan.server.impl.SecurityRegistryImpl;
import org.eclipse.leshan.server.model.LwM2mModelProvider;
import org.eclipse.leshan.server.model.StandardModelProvider;
import org.eclipse.leshan.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

public class Lwm2mServerMain {
	private static final Logger LOG = LoggerFactory.getLogger(Lwm2mServerMain.class);

    private final static String USAGE = "java -jar leshan-server-demo.jar [OPTION]";
    private final static String FOOTER = "All options could be passed using environment variables.(using long option name in uppercase)";

    public static void main(String[] args) throws Exception {
    	try {
//        	/Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
        	
        	String localAddress = Lwm2mServerConfig.getInstance().getIpeIp();
        	int localPort = Lwm2mServerConfig.getInstance().getIpePortNormal();
        	String secureLocalAddress = Lwm2mServerConfig.getInstance().getIpeIp();
        	int secureLocalPort = Lwm2mServerConfig.getInstance().getIpePortSecure();
        	int webPort = Lwm2mServerConfig.getInstance().getIpePortWeb();
        	String redisUrl = null;
        	
            createAndStartServer(webPort, localAddress, localPort, secureLocalAddress, secureLocalPort, redisUrl);
        } catch (BindException e) {
        	e.printStackTrace();
        } catch (Exception e) {
            LOG.error("Jetty stopped with unexcepted error ...", e);
        }
    	
    }
    
    public static void createAndStartServer(int webPort, String localAddress, int localPort, String secureLocalAddress,
            int secureLocalPort, String redisUrl) throws Exception {
        // Prepare LWM2M server
        LeshanServerBuilder builder = new LeshanServerBuilder();
        builder.setLocalAddress(localAddress, localPort);
        builder.setLocalSecureAddress(secureLocalAddress, secureLocalPort);
        builder.setEncoder(new DefaultLwM2mNodeEncoder());
        LwM2mNodeDecoder decoder = new DefaultLwM2mNodeDecoder();
        builder.setDecoder(decoder);

        // connect to redis if needed
        Pool<Jedis> jedis = null;
        if (redisUrl != null) {
            // TODO: support sentinel pool and make pool configurable
            jedis = new JedisPool(new URI(redisUrl));
        }

        // Get public and private server key
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        try {
            // Get point values
            byte[] publicX = Hex
                    .decodeHex("fcc28728c123b155be410fc1c0651da374fc6ebe7f96606e90d927d188894a73".toCharArray());
            byte[] publicY = Hex
                    .decodeHex("d2ffaa73957d76984633fc1cc54d0b763ca0559a9dff9706e9f4557dacc3f52a".toCharArray());
            byte[] privateS = Hex
                    .decodeHex("1dae121ba406802ef07c193c1ee4df91115aabd79c1ed7f4c0ef7ef6a5449400".toCharArray());

            // Get Elliptic Curve Parameter spec for secp256r1
            AlgorithmParameters algoParameters = AlgorithmParameters.getInstance("EC");
            algoParameters.init(new ECGenParameterSpec("secp256r1"));
            ECParameterSpec parameterSpec = algoParameters.getParameterSpec(ECParameterSpec.class);

            // Create key specs
            KeySpec publicKeySpec = new ECPublicKeySpec(new ECPoint(new BigInteger(publicX), new BigInteger(publicY)),
                    parameterSpec);
            KeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(privateS), parameterSpec);

            // Get keys
            publicKey = KeyFactory.getInstance("EC").generatePublic(publicKeySpec);
            privateKey = KeyFactory.getInstance("EC").generatePrivate(privateKeySpec);

            LwM2mModelProvider modelProvider = new StandardModelProvider();
            builder.setObjectModelProvider(modelProvider);

            if (jedis == null) {
                // in memory security registry (with file persistence)
                builder.setSecurityRegistry(new SecurityRegistryImpl(privateKey, publicKey));
            } else {
                // use Redis
                ClientRegistry clientRegistry = new RedisClientRegistry(jedis);
                builder.setSecurityRegistry(new RedisSecurityRegistry(jedis, privateKey, publicKey));
                builder.setClientRegistry(clientRegistry);
                builder.setObservationRegistry(
                        new RedisObservationRegistry(jedis, clientRegistry, modelProvider, decoder));
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidParameterSpecException e) {
            LOG.error("Unable to initialize RPK.", e);
            System.exit(-1);
        }

        // Create and start LWM2M server
        LeshanServer lwServer = builder.build();

        // Now prepare Jetty
        Server server = new Server(webPort);
        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setResourceBase(Lwm2mServerMain.class.getClassLoader().getResource("webapp").toExternalForm());
        root.setParentLoaderPriority(true);
        server.setHandler(root);

        // Create Servlet
        EventServlet eventServlet = new EventServlet(lwServer, lwServer.getSecureAddress().getPort());
        ServletHolder eventServletHolder = new ServletHolder(eventServlet);
        root.addServlet(eventServletHolder, "/event/*");

        ServletHolder clientServletHolder = new ServletHolder(
                new ClientServlet(lwServer, lwServer.getSecureAddress().getPort()));
        root.addServlet(clientServletHolder, "/api/clients/*");

        ServletHolder securityServletHolder = new ServletHolder(new SecurityServlet(lwServer.getSecurityRegistry()));
        root.addServlet(securityServletHolder, "/api/security/*");

        ServletHolder objectSpecServletHolder = new ServletHolder(new ObjectSpecServlet(lwServer.getModelProvider()));
        root.addServlet(objectSpecServletHolder, "/api/objectspecs/*");
        
        ServletHolder apitServletHolder = new ServletHolder(new ApiServlet(lwServer, lwServer.getSecureAddress().getPort()));
        root.addServlet(apitServletHolder, "/api/hdm/*");

        // Start Jetty & Leshan
        lwServer.start();
        server.start();
        LOG.info("Web server started at {}.", server.getURI());
    }
    
}
