package lpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LongestPrefixMatcherBasis {
	List<Integer> ips;
	List<Byte> prefixLengths;
	List<Integer> portNumbers;
	Map<Byte, Integer> foundMatches;
	
  /**
   * You can use this function to initialize variables.
   */
    public LongestPrefixMatcherBasis() {
        ips = new ArrayList<>();
        prefixLengths = new ArrayList<>();
        portNumbers = new ArrayList<>();
    }
    
    
    /**
     * Looks up an IP address in the routing tables
     * @param ip The IP address to be looked up in integer representation
     * @return The port number this IP maps to
     */
    public int lookup(int ip) {
    	
    	foundMatches = new HashMap<>();
	
    	for (int i = 0; i < ips.size(); i++) {
    			
    		int givenIP = ip >> (32 - prefixLengths.get(i));
    		int compareIP = ips.get(i) >> (32 - prefixLengths.get(i));
    		
    		if (givenIP == compareIP) {
    			foundMatches.put(prefixLengths.get(i), portNumbers.get(i));
    		}	
    	}
    	
    	if (foundMatches.size() != 0) {
        	Set<Byte> keys = foundMatches.keySet();
        		
        	byte longest = 0;
        	for (Byte k:keys) {
        		if (k > longest) { 
        			longest = k;
        		}
        	}      	
        	return foundMatches.get(longest);
    	}       
        return -1;
    }

    
    
    /**
     * Adds a route to the routing tables
     * @param ip The IP the block starts at in integer representation
     * @param prefixLength The number of bits indicating the network part
     *                     of the address range (notation ip/prefixLength)
     * @param portNumber The port number the IP block should route to
     */
    public void addRoute(int ip, byte prefixLength, int portNumber) {
        // TODO: Store this route for later use in lookup() method
    	ips.add(ip);
    	prefixLengths.add(prefixLength);
    	portNumbers.add(portNumber);	
    }

    /**
     * Converts an integer representation IP to the human readable form
     * @param ip The IP address to convert
     * @return The String representation for the IP (as xxx.xxx.xxx.xxx)
     */
    private String ipToHuman(int ip) {
        return Integer.toString(ip >> 24 & 0xff) + "." +
                Integer.toString(ip >> 16 & 0xff) + "." +
                Integer.toString(ip >> 8 & 0xff) + "." +
                Integer.toString(ip & 0xff);
    }

    /**
     * Parses an IP
     * @param ipString The IP address to convert
     * @return The integer representation for the IP
     */
    private int parseIP(String ipString) {
        String[] ipParts = ipString.split("\\.");

        int ip = 0;
        for (int i = 0; i < 4; i++) {
            ip |= Integer.parseInt(ipParts[i]) << (24 - (8 * i));
        }

        return ip;
    }
}
