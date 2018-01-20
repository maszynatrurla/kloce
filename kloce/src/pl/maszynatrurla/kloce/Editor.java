package pl.maszynatrurla.kloce;

import pl.maszynatrurla.kloce.editor.EditorWindow;

public class Editor {

	public void launch()
	{
		new EditorWindow().open();
	}
	
	public static void main(String[] args) 
	{
		Editor app = new Editor();
		app.launch();
	}

}
