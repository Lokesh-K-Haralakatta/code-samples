public class MainApp {

	public static void main(String[] args) {
		NumberSet S = new NumberSet();
		S.addNumber(3); S.addNumber(4); S.addNumber(5); S.addNumber(1);
		S.addNumber(2);
		S.printSubsets();
		
		S.printSumCombinations(5);
	}

}
