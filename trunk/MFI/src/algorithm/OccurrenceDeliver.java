package algorithm;

import model.Bucket;
import model.Dataset;
import model.Item;
import model.Transaction;

/**
 * This class abstracts the occurrence deliver model and its construction
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class OccurrenceDeliver {

     private Bucket[] buckets;

     public OccurrenceDeliver(Dataset dataset) {

        buckets = new Bucket[dataset.getMaxItem() + 1];

        for(Integer item : dataset.getTransactionsItems()) {
             buckets[item] = new Bucket();
        }

        for(Transaction transaction : dataset.getTransactions()) {

            for(Item item : transaction.getItems()) {

                // for each item get it bucket and add the current transaction
                buckets[item.getItem()].getTransactions().add(transaction);

            }

        }
     }

    public Bucket[] getBuckets() {
        return buckets;
    }

    public void clear() {

        buckets = new Bucket[buckets.length];

        for(int i = 0; i < buckets.length; i++) {
          buckets[i] = new Bucket();
        }

    }
}
