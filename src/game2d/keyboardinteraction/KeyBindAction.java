package game2d.keyboardinteraction;

/**
	Interface used to define how lambdas used in keymap are to be used
	usage: keymap.put(KeyCode.A, () -> System.out.println("'a' was pressed!"));
*/
public interface KeyBindAction {
	void run();
}
