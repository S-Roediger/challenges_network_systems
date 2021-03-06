package lpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LongestPrefixMatcher {

	Map<Byte, Integer> foundMatches;
	HashMap<Integer, Integer> routingTable;
	Set<Byte> prefixes;
	
  /**
   * You can use this function to initialize variables.
   */
    public LongestPrefixMatcher() {
    	routingTable = new HashMap<>();
    	prefixes = new HashSet<>();
    }
    
    
    /**
     * Looks up an IP address in the routing tables
     * @param ip The IP address to be looked up in integer representation
     * @return The port number this IP maps to
     */
    public int lookup(int ip) {
    	
    	List<Integer> testList = new ArrayList<>();
    	
    	if (routingTable.containsKey(ip)) {
    		return routingTable.get(ip);
    	}
    	
    	for (int i = 1; i < 32; i++) {
    		System.out.println("Ons iiiitje: "+i);
    		int given = ip >> (32 - i);
    		if (routingTable.containsKey(given)) {
    			testList.add(routingTable.get(given));
    			//return routingTable.get(given);
    		}
    	}
    	
    	if (true) {
        	System.out.println("Print even de list" + testList);
        	return testList.get(0);
    	}

    	
    	
//    	for (int i = 0; i < ips.size(); i++) {
//    			
//    		int givenIP = ip >> (32 - prefixLengths.get(i));
//    		int compareIP = ips.get(i) >> (32 - prefixLengths.get(i));
//    		
//    		if (givenIP == compareIP) {
//    			foundMatches.put(prefixLengths.get(i), portNumbers.get(i));
//    		}	
//    	}
    	
//    	if (foundMatches.size() != 0) {
//        	Set<Byte> keys = foundMatches.keySet();
//        		
//        	byte longest = 0;
//        	for (Byte k:keys) {
//        		if (k > longest) { 
//        			longest = k;
//        		}
//        	}      	
//        	return foundMatches.get(longest);
//    	}       
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
    	int ipMap = ip >> (32 - prefixLength);
    	this.routingTable.put(ipMap, portNumber);
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
    
    class TrieNode { 
        public TrieNode(char ch)  { 
            value = ch; 
            children = new HashMap<>(); 
            bIsEnd = false; 
        } 
        public HashMap<Character,TrieNode> getChildren() {   return children;  } 
        public char getValue()                           {   return value;     } 
        public void setIsEnd(boolean val)                {   bIsEnd = val;     } 
        public boolean isEnd()                           {   return bIsEnd;    } 
      
        private char value; 
        private HashMap<Character,TrieNode> children; 
        private boolean bIsEnd; 
    } 
      
    // Implements the actual Trie 
    class Trie { 
        // Constructor 
        public Trie()   {     root = new TrieNode((char)0);       }     
      
        // Method to insert a new word to Trie 
        public void insert(String word)  { 
      
            // Find length of the given word 
            int length = word.length(); 
            TrieNode crawl = root; 
      
            // Traverse through all characters of given word 
            for( int level = 0; level < length; level++) 
            { 
                HashMap<Character,TrieNode> child = crawl.getChildren(); 
                char ch = word.charAt(level); 
      
                // If there is already a child for current character of given word 
                if( child.containsKey(ch)) 
                    crawl = child.get(ch); 
                else   // Else create a child 
                { 
                    TrieNode temp = new TrieNode(ch); 
                    child.put( ch, temp ); 
                    crawl = temp; 
                } 
            } 
      
            // Set bIsEnd true for last character 
            crawl.setIsEnd(true); 
        } 
      
        // The main method that finds out the longest string 'input' 
        public String getMatchingPrefix(String input)  { 
            String result = ""; // Initialize resultant string 
            int length = input.length();  // Find length of the input string        
      
            // Initialize reference to traverse through Trie 
            TrieNode crawl = root;    
      
            // Iterate through all characters of input string 'str' and traverse 
            // down the Trie 
            int level, prevMatch = 0; 
            for( level = 0 ; level < length; level++ ) 
            { 
                // Find current character of str 
                char ch = input.charAt(level);     
      
                // HashMap of current Trie node to traverse down 
                HashMap<Character,TrieNode> child = crawl.getChildren();                         
      
                // See if there is a Trie edge for the current character 
                if( child.containsKey(ch) ) 
                { 
                   result += ch;          //Update result 
                   crawl = child.get(ch); //Update crawl to move down in Trie 
      
                   // If this is end of a word, then update prevMatch 
                   if( crawl.isEnd() ) 
                        prevMatch = level + 1; 
                } 
                else  break; 
            } 
      
            // If the last processed character did not match end of a word, 
            // return the previously matching prefix 
            if( !crawl.isEnd() ) 
                    return result.substring(0, prevMatch);         
      
            else return result; 
        } 
      
        private TrieNode root; 
    } 
    

}
