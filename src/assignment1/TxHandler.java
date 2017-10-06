package assignment1;

import java.util.ArrayList;
import java.util.List;

public class TxHandler {
	private UTXOPool utxoPool;
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
    	if(tx == null) {
    		return false;
    	}
    	
        // for condition number 3 
    	UTXOPool tempUtxoPool = new UTXOPool();
    	double outputTotal = 0;
    	double inputTotal = 0;
    	
    	// check input for cond 1,2,3
    	int idx = 0;
    	for(Transaction.Input i : tx.getInputs()) {
        	UTXO utxo = new UTXO(i.prevTxHash, i.outputIndex);
        	Transaction.Output txOutput = this.utxoPool.getTxOutput(utxo);
        	
        	if(txOutput == null) {
        		continue;
        	}
        	
        	boolean signatureVerified = Crypto.verifySignature(txOutput.address, tx.getRawDataToSign(idx), i.signature);
       
        	if(!this.utxoPool.contains(utxo) || !signatureVerified || tempUtxoPool.contains(utxo) ) {
        		return false;
        	}
        	
        	// add to tempUTXOPool for checking 
        	tempUtxoPool.addUTXO(utxo, txOutput);
        	inputTotal += txOutput.value;
        	idx += 1;
    	}
    	
    	//check output for cond 4,5 
    	for(Transaction.Output o : tx.getOutputs()) {
    		if (o.value < 0) {
    			return false;
    		}
    		outputTotal += o.value;
    	}
        
        return inputTotal >= outputTotal;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        if(possibleTxs == null) {
        	return null;
        }
    	
    	List<Transaction> result = new ArrayList<Transaction>();
        
        for(int i = 0 ; i < possibleTxs.length; i++) {
        	Transaction curTx = possibleTxs[i];
        	if(isValidTx(curTx)) {
        		result.add(curTx);
        		
        		for(Transaction.Input in : curTx.getInputs()) {
        			UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
        			this.utxoPool.removeUTXO(utxo);
        		}
        		
        		int idx = 0;
        		for(Transaction.Output o : curTx.getOutputs()) {
        			UTXO utxo = new UTXO(curTx.getHash(), idx);
        			idx += 1;
        			this.utxoPool.addUTXO(utxo, o);
        		}
        	}
        }
        
        return result.toArray(new Transaction[]{});
        
    }

}
