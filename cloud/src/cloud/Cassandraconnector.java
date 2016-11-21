package cloud;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class Cassandraconnector {
	 /** Cassandra Cluster. */
	   private Cluster cluster;
	   /** Cassandra Session. */
	   private Session session;
	   /**
	    * Connect to Cassandra Cluster specified by provided node IP
	    * address and port number.
	    *
	    * @param node Cluster node IP address.
	    * @param port Port of cluster host.
	    */
	   public void connect(final String node, final int port)
	   {
	      this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
	      final Metadata metadata = cluster.getMetadata();
	      System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
	      for (final Host host : metadata.getAllHosts())
	      {
	         System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
	            host.getDatacenter(), host.getAddress(), host.getRack());
	      }
	      session = cluster.connect();
	   }
	   /**
	    * Provide my Session.
	    *
	    * @return My session.
	    */
	   public Session getSession()
	   {
	      return this.session;
	   }
	   /** Close cluster. */
	   public void close()
	   {
	      cluster.close();
	   }
	   /**
	    * Main function for demonstrating connecting to Cassandra with host and port.
	    *
	    * @param args Command-line arguments; first argument, if provided, is the
	    *    host and second argument, if provided, is the port.
	    */
	   public static void main(final String[] args)
	   {
	      final Cassandraconnector client = new Cassandraconnector();
	      final String ipAddress = "104.196.144.239";
	      final int port = 9042;
	      //final String ipAddress = args.length > 0 ? args[0] : "localhost";
	     // final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9042;
	      System.out.println("Connecting to IP Address " + ipAddress + ":" + port + "...");
	      client.connect(ipAddress, port);
	      client.close();
	   }

}
