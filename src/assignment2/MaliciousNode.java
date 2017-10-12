package assignment2;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class MaliciousNode implements Node {
	private double pGraph;
	private double pMalicious;
	private double pTxDistribution;
	private int numRounds;
	
    public MaliciousNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
    	this.pGraph = p_graph;
    	this.pMalicious = p_malicious;
    	this.pTxDistribution = p_txDistribution;
    	this.numRounds = numRounds;
    }

    public void setFollowees(boolean[] followees) {
        return;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        return;
    }

    public Set<Transaction> sendToFollowers() {
        return new HashSet<Transaction>();
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        return;
    }
}
