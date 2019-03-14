package my_protocol;

import framework.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @version 12-03-2019
 *
 * Copyright University of Twente, 2013-2019
 *
 **************************************************************************
 *                            Copyright notice                            *
 *                                                                        *
 *             This file may ONLY be distributed UNMODIFIED.              *
 * In particular, a correct solution to the challenge must NOT be posted  *
 * in public places, to preserve the learning effect for future students. *
 **************************************************************************
 */
public class MyRoutingProtocol implements IRoutingProtocol {
    private LinkLayer linkLayer;

    // You can use this data structure to store your routing table.
    private HashMap<Integer, MyRoute> myRoutingTable = new HashMap<>();
    private DataTable dtSend = new DataTable(7);
    @Override
    public void init(LinkLayer linkLayer) {
        this.linkLayer = linkLayer;
    }


    @Override
    public void tick(PacketWithLinkCost[] packetsWithLinkCosts) {
        // Get the address of this node
        int myAddress = this.linkLayer.getOwnAddress();

        int currentNeigh = 0;
        int currentDest = 0;
        int currentLinkCost = 0;
        System.out.println("tick; received " + packetsWithLinkCosts.length + " packets");
        int i;

        

        
        // first process the incoming packets; loop over them:
        for (i = 0; i < packetsWithLinkCosts.length; i++) {
            Packet packet = packetsWithLinkCosts[i].getPacket();
            int neighbour = packet.getSourceAddress();             // from whom is the packet?
            currentNeigh = neighbour;
            int linkcost = packetsWithLinkCosts[i].getLinkCost();  // what's the link cost from/to this neighbour?
            currentLinkCost = linkcost;
            DataTable dtReceive = packet.getDataTable();                  // other data contained in the packet
            
           

            
            System.out.printf("received packet from %d with %d rows and %d columns of data%n", neighbour, dtReceive.getNRows(), dtReceive.getNColumns());
  
            // you'll probably want to process the data, update your data structures (myRoutingTable) , etc....

            // reading one cell from the DataTable can be done using the  dt.get(row,column)  method
      
            int dest = packet.getDestinationAddress();

            currentDest = dest;
            
            
            if (myRoutingTable.containsKey(dest)) { //als deze al in routingtable is dan update
 
            	
                int costDestinationToNeighbour = dtReceive.get(neighbour,dest);
                int costDestinationMe = linkcost + costDestinationToNeighbour;
                MyRoute r1 = myRoutingTable.get(dest); //update
                
                if (r1.cost > costDestinationMe) { //update alleen als de score beter is
                	
                	myRoutingTable.remove(dest);
                	
                    r1.cost = costDestinationMe;
                    r1.nextHop = dest;
                    myRoutingTable.put(dest, r1);
                    
                }
                    
                
            } else if (dest < 7 && dest > 0){ //anders new
            	
            	
                MyRoute r1 = new MyRoute();
                r1.nextHop = neighbour;
                r1.cost = linkcost;
                myRoutingTable.put(dest, r1);
                
                
            } else {
            	
                MyRoute r1 = new MyRoute();
                r1.nextHop = neighbour;
                r1.cost = linkcost;
                myRoutingTable.put(neighbour, r1);
                
            }
            
            //Making table?
            if (dtReceive.getNRows()==0) {
                System.out.println("dtReceive empty");
                Integer[] empty = {100,100,100,100,100,100,100};
                while (dtSend.getNRows()!=7) {
                  dtSend.addRow(empty);
                }
              } else {
                dtSend = dtReceive;
                System.out.println("replace sendDT to receivedDT");
              }
        }

        if (myRoutingTable.containsKey(myAddress)) {
        	myRoutingTable.remove(myAddress);
        }
        
        Set<Integer> k = myRoutingTable.keySet();
        System.out.println("RT "+myRoutingTable);
        System.out.println("Keys "+k);
        
        for (Integer n:k) {
        	dtSend.set(myAddress, n, myRoutingTable.get(n).cost); //update dtSend met je eigen kennis 	
        	System.out.println(n +" toegevoegd");
        	
        }
        
        if (dtSend.getNRows() > currentNeigh) {
        	for (int g = 1; g < dtSend.getNColumns(); g++) {
            	int costsNeigh = dtSend.get(currentNeigh, g);
            	int costsMe = dtSend.get(myAddress, g);
            	if ((costsNeigh + currentLinkCost) < costsMe) {
            		dtSend.set(myAddress, currentNeigh, (costsNeigh+currentLinkCost));
            	}
        		

        	}
        }

       if (myRoutingTable.containsKey(currentDest)) {
    	   MyRoute r = myRoutingTable.get(currentDest);
    	   
           Packet pkt = new Packet(myAddress, r.nextHop, dtSend); //dest was 0
           this.linkLayer.transmit(pkt);
       }


        
        Packet pktt = new Packet(myAddress, 0, dtSend); //dest was 0
        this.linkLayer.transmit(pktt);
        // and send out one (or more, if you want) distance vector packets
        // the actual distance vector data must be stored in the DataTable structure
        //DataTable dt = new DataTable(6);   // the 6 is the number of columns, you can change this
        
        // you'll probably want to put some useful information into dt here
        // by using the  dt.set(row, column, value)  method.

        // next, actually send out the packet, with our own address as the source address
        // and 0 as the destination address: that's a broadcast to be received by all neighbours.
//        Packet pkt = new Packet(myAddress, 0, dt);
//        this.linkLayer.transmit(pkt);

        /*
        Instead of using Packet with a DataTable you may also use Packet with
        a byte[] as data part, if you really want to send your own data structure yourself.
        Read the JavaDoc of Packet to see how you can do this.
        PLEASE NOTE! Although we provide this option we do not support it.
        */
    }

    public Map<Integer, Integer> getForwardingTable() {
        // This code extracts from your routing table the forwarding table.
        // The result of this method is send to the server to validate and score your protocol.

        // <Destination, NextHop>
        HashMap<Integer, Integer> ft = new HashMap<>();

        for (Map.Entry<Integer, MyRoute> entry : myRoutingTable.entrySet()) {
            ft.put(entry.getKey(), entry.getValue().nextHop);
        }

        return ft;
    }
}