package assignment2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {
	private double pGraph, pMalicious, pTxDistribution;
	private int numRounds;
	private List<Integer> listFollowees = new ArrayList<Integer>();
	//assume that all transactions are valid per spec
	private List<Transaction> listPendingTransactions = new ArrayList<Transaction>();
	
	
    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
    	this.pGraph = p_graph;
    	this.pMalicious = p_malicious;
    	this.pTxDistribution = p_txDistribution;
    	this.numRounds = numRounds;
    }

    public void setFollowees(boolean[] followees) {
        if(followees == null) {
        	return;
        }
		this.listFollowees.clear();
		for (int i = 0; i < followees.length; i++) {
			if (followees[i]) {
				this.listFollowees.add(i);
			}
		}
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        if (pendingTransactions == null) {
        	return;
        }
        
        this.listPendingTransactions.clear();
    	for(Transaction tx : pendingTransactions) {
        	this.listPendingTransactions.add(tx);
        }
    }

    public Set<Transaction> sendToFollowers() {
    	Set<Transaction> setTransactions =  new HashSet<Transaction>(this.listPendingTransactions);
    	this.listPendingTransactions.clear();
    	
    	return setTransactions;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        if(candidates == null) {
        	return;
        }
        
        for(Candidate c : candidates){
        	if(isFollowee(c.sender)){
        		this.listPendingTransactions.add(c.tx);
        	}
        }
    }
    
    private boolean isFollowee(int nodeID){
    	if (nodeID > (listFollowees.size()-1) || listFollowees.get(nodeID) == null) {
    		return false;
    	}
    	
    	return true;
    }
}
