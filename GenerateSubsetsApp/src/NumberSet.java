import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NumberSet {
	//Member to hold given set items
	private List<Integer> set = null;
	//Member to hold generated subsets 
	private Map<Integer,List<Integer>> subSets = null;
	
	//Default constructor
	public NumberSet() {
		set = new ArrayList<Integer>();
		subSets = new HashMap<Integer,List<Integer>>();
	}
	
	public NumberSet(List<Integer> list) {
		set = list;
		subSets = new HashMap<Integer,List<Integer>>();
	}
	
	//Method to add given number to set
	public void addNumber(Integer x) {
		set.add(x);
	}
	
	//Method to remove number from set
	public boolean removeNumber(Integer x) {
		if(set.contains(x)) 
			return set.remove(x);
		else
			return false;
	}
	
	//Private method to generate the subsets
	private void generateSubsets() {
		//Clear off earlier generated subsets
		if(subSets != null && subSets.size() > 0)
			subSets.clear();
		
		//Get number of items in set
		int n = set.size();
		 
        // Run a loop for all 2^n subsets one by one
        for (int i = 0; i < (1<<n); i++)
        {
            // Collect current subset
        	List<Integer> curSubset = new ArrayList<Integer>();
            for (int j = 0; j < n; j++)
 
                // (1<<j) is a number with jth bit 1
                // so when we 'and' them with the
                // subset number we get which numbers
                // are present in the subset and which
                // are not
                if ((i & (1 << j)) > 0) {
                    Integer item = set.get(j);
                    curSubset.add(item);
                }
            if(curSubset.size() == 0)
            	curSubset=null;
            subSets.put(i, curSubset);
        }
	}

	//Method to generate sum combinations using the generated subsets
	private Map<Integer,List<Integer>> generateSumCombinations(int sum){
		Map<Integer,List<Integer>> sumCombs = null;
		
		//Generate subsets if required
		if(subSets == null || subSets.size() == 0)
			generateSubsets();
		
		//Total generated subsets
		int n = subSets.size();
		
		//Allocate memory for sum combination map
		if(n != 0)
			sumCombs = new HashMap<Integer,List<Integer>>();
		
		int j = 0;
		for(int i=0; i<n ; i++) {
			List<Integer> csubSet = subSets.get(i);
			if(csubSet != null && sum != 0)
			{
				int cSum = 0;
				for(Integer x:csubSet)
					cSum += x;
				if(sum == cSum)
					sumCombs.put(j++, csubSet);
			}
		}
		//Return found sum combinations if any
		return sumCombs;
		
	}
	
	//Method to print the generated subsets
	public void printSubsets() {
		generateSubsets();
		//Get number of generated subsets
		int n = subSets.size();
		
		if(n ==0)
			System.out.println("Generated number of subsets : " +n);
		else {
			System.out.println("Generated number of subsets : " +n);
			for(int i=0;i<n;i++) {
				List<Integer> curSubset = subSets.get(i);
				if(curSubset == null)
					System.out.println("{ }");
				else {
					System.out.print("{ ");
					for(Integer x:curSubset)
						System.out.print(x + ",");
					System.out.print(" }");
					System.out.println("");
				}
			}
		}
			
	}
	
	//Method to print the generated sum combinations
	public void printSumCombinations(int G) {
		generateSubsets();
		Map<Integer,List<Integer>> sumCombs = generateSumCombinations(G);
		
		if(sumCombs == null || sumCombs.size() == 0)
			System.out.println("There are no subset with elements sum matching " + G);
		else {
			System.out.println("Number of subsets with sum == " + G + " : " + sumCombs.size());
			for(Integer key: sumCombs.keySet()) {
				System.out.print("Sum Combination - " + (key+1) + " ==> { ");
				List<Integer> list = sumCombs.get(key);
				for(Integer x:list)
					System.out.print(x + " , ");
				System.out.println(" }");
				System.out.println("");
			}
				
		}
	}
}

