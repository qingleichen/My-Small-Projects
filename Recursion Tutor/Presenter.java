import java.util.ArrayList;


public class Presenter {
	private ArrayList<Action> actions;
	private boolean byStep;

	public void display() {
		MergeSortEx ex = new MergeSortEx();
		actions = ex.SortAndDisplay();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
                
			}
		}).start();
	}
}
